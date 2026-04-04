package server.network;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.UUID;

import common.logging.Log;
import common.network.Packet;
import server.adapters.CommandAdapter;
import server.adapters.ConfigAdapter;
import server.adapters.EventAdapter;
import server.adapters.InventoryAdapter;
import server.adapters.MessageAdapter;
import server.adapters.PermissionAdapter;
import server.adapters.PlayerAdapter;
import server.adapters.ServerAdapter;
import server.adapters.WorldAdapter;
import server.commands.BaseCommandProvider;
import server.commands.Command;
import server.commands.CommandDispatcher;
import server.commands.CommandRegistry;
import server.config.ServerConfig;
import server.entity.EntityManager;
import server.events.EventBus;
import server.gateways.CommandGateway;
import server.gateways.ConfigGateway;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.gateways.InventoryGateway;
import server.gateways.MessageGateway;
import server.gateways.PermissionGateway;
import server.gateways.PlayerGateway;
import server.gateways.ServerGateway;
import server.gateways.WorldGateway;
import server.modules.Module;
import server.permissions.PermissionService;
import server.permissions.SimplePermissionService;
import server.persistance.ChunkRepository;
import server.persistance.FileChunkRepository;
import server.player.ServerPlayer;
import server.scheduler.ServerScheduler;
import server.usecases.UseCaseRegistry;
import server.world.ServerWorld;
import server.world.WorldNetworkSystem;
import server.world.generation.BasicWorldGenerator2;
import server.world.generation.WorldGenerator;

/**
 * The core server class that manages the network lifecycle and the main game loop. It coordinates
 * incoming connections and ensures all game logic is executed synchronously within the main tick
 * loop.
 */
public class GameServer {

  private static final int MAX_PACKETS_PER_TICK = 2000; // TODO this is config later
  private static final int MAX_PACKETS_PER_CONNECTION = 100; // TODO this is config later

  private long tick = 0;

  private final int port;
  private volatile boolean running = true;
  private ServerSocket serverSocket;

  private final ServerScheduler scheduler;

  private final CommandRegistry commandRegistry;
  private final CommandDispatcher commandDispatcher;

  private final EventBus eventBus;
  private final PlayerManager playerManager;
  private final EntityManager entityManager;
  private ServerWorld world;

  private final PermissionService permissionService;

  private UseCaseRegistry useCases;
  private GatewayContext context;

  private final ServerConfig config;

  private final TPSCounter tpsCounter = new TPSCounter();

  public GameServer(int port, ServerConfig config) {
    this.port = port;
    this.config = config;
    this.commandRegistry = new CommandRegistry();
    this.scheduler = new ServerScheduler();

    File worldFolder = new File("world_data");
    ChunkRepository chunkRepository = new FileChunkRepository(worldFolder);

    this.eventBus = new EventBus();
    EventGateway events = new EventAdapter(eventBus);

    this.playerManager = new PlayerManager();
    this.entityManager = new EntityManager(this);

    WorldGenerator worldGenerator = new BasicWorldGenerator2(0);
    this.world = new ServerWorld(worldGenerator, chunkRepository, events);

    //    this.permissionService = new AlwaysGrantPermissionService();
    //    this.permissionService = new AlwaysDenyPermissionService();
    this.permissionService = new SimplePermissionService();

    initUseCases(events);
    registerCommands();

    commandDispatcher = new CommandDispatcher(this, context);

    new WorldNetworkSystem(events, context.messages(), playerManager);
  }

  public void registerModule(Module module) {
    module.registerCommands(commandRegistry, context);
    module.registerEvents(eventBus, context);
    module.onEnable();
  }

  private void initUseCases(EventGateway eventGateway) {
    WorldGateway worldGateway = new WorldAdapter(world);
    PermissionGateway permissionGateway = new PermissionAdapter(permissionService);
    InventoryGateway inventoryGateway = new InventoryAdapter(playerManager);
    ConfigGateway configGateway = new ConfigAdapter(config);
    CommandGateway commandGateway = new CommandAdapter(commandRegistry);
    PlayerGateway playerGateway = new PlayerAdapter(playerManager);
    MessageGateway messages = new MessageAdapter(playerManager, config.getServerPrefix());
    ServerGateway server = new ServerAdapter(this);

    context =
        new GatewayContext(
            worldGateway,
            eventGateway,
            permissionGateway,
            inventoryGateway,
            configGateway,
            commandGateway,
            playerGateway,
            messages,
            server);

    this.useCases = new UseCaseRegistry(context);
  }

  private void registerCommands() {
    new BaseCommandProvider().registerCommands(commandRegistry, context);
  }

  /**
   * Starts the server by initializing the socket, starting the network acceptor thread, and
   * entering the main game loop.
   */
  public void start() throws Exception {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  Log.info("Shutdown Hook triggered. Emergency save starting...");
                  this.shutdown();
                },
                "Shutdown-Thread"));

    serverSocket = new ServerSocket(port);
    serverSocket.setReuseAddress(true);

    Log.info("Server running on port " + port);

    // THREAD 1: Network Acceptor Loop
    // Dedicated thread to listen for new TCP connections without blocking the game logic.
    Thread acceptorThread = new Thread(this::acceptLoop, "Network-Acceptor");
    acceptorThread.setDaemon(true);
    acceptorThread.start();

    // THREAD 2: Console Thread
    Thread consoleThread = new Thread(this::consoleLoop, "Console-Reader");
    consoleThread.setDaemon(true);
    consoleThread.start();

    // THREAD 3: Main Game Loop
    // Handles game logic and packet processing in synchronous ticks.
    gameLoop();
  }

  private void consoleLoop() {
    java.util.Scanner scanner = new java.util.Scanner(System.in);

    while (running) {
      try {
        String line = scanner.nextLine();

        if (line == null || line.isEmpty()) continue;

        handleConsoleCommand(line);

      } catch (Exception e) {
        Log.error("Console error: " + e.getMessage());
      }
    }
  }

  private void handleConsoleCommand(String input) {
    runNextTick(() -> executeConsoleCommand(input));
  }

  private void executeConsoleCommand(String input) {
    dispatchCommand(null, input);
  }

  public void dispatchCommand(UUID playerId, String input) {
    commandDispatcher.dispatchCommand(playerId, input);
  }

  /** Continuously listens for new client connections. */
  private void acceptLoop() {
    try {
      while (running) {
        Socket socket = serverSocket.accept();

        Log.info("New connection: " + socket.getInetAddress());

        // ServerConnection starts its own internal read-thread upon instantiation
        new ServerConnection(this, socket, useCases, context);
      }
    } catch (Exception e) {
      if (running) e.printStackTrace();
    }
  }

  /** The heartbeat of the server. Regulates the tick rate (default: 20 TPS). */
  private void gameLoop() {
    Log.info("Game loop started.");

    while (running) {
      long startTime = System.currentTimeMillis();

      // Execute the tick update
      tick++;
      update();
      tpsCounter.update(tick);

      // Calculate timing for a steady 20 Ticks/s (50ms per tick)
      long delta = System.currentTimeMillis() - startTime;
      long sleepTime = 50 - delta;

      if (sleepTime > 0) {
        try {
          Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      } else if (delta > 100) {
        Log.error("Can't keep up! Tick took " + delta + "ms");
      }
    }
  }

  private void processIncomingPackets() {
    int totalProcessed = 0;

    for (ServerConnection conn : playerManager.getConnections()) {

      if (!conn.isRunning()) continue;

      int processed = 0;
      Packet packet;

      while (processed < MAX_PACKETS_PER_CONNECTION
          && totalProcessed < MAX_PACKETS_PER_TICK
          && (packet = conn.pollPacket()) != null) {

        conn.getPacketDispatcher().dispatch(packet);

        processed++;
        totalProcessed++;
      }

      // Debug (optional, but useful)
      if (processed == MAX_PACKETS_PER_CONNECTION && conn.getQueueSize() > 0) {
        //        Log.warn("[NET] connection limit reached: " + conn.getQueueSize());
      }
    }

    // Global Debug
    if (totalProcessed == MAX_PACKETS_PER_TICK) {
      Log.warn("[NET] global packet limit reached");
    }
  }

  private void updatePlayers() {
    for (ServerPlayer player : playerManager.getAllPlayers()) {
      player.broadcastUpdate();
    }
  }

  private void updateWorld() {
    // Placeholder for world logic (Physics, AI, Tile Entities, etc.)
    if (world != null) {
      world.tick();
    }
  }

  private void updateChunkStreaming() {
    for (ServerPlayer player : playerManager.getAllPlayers()) {
      player.updateStreaming();
      player.processStreaming();
    }
  }

  private void flushNetwork() {
    int MAX_OUTBOUND_PER_TICK = 10; // TODO config
    for (ServerConnection conn : getPlayerManager().getConnections()) {
      conn.flushOutbound(MAX_OUTBOUND_PER_TICK);
    }
  }

  private void unloadUnusedChunks() {
    if (tick % 80 != 0) return;
    java.util.Set<Long> allRequiredChunks = new java.util.HashSet<>();
    for (ServerPlayer player : playerManager.getAllPlayers()) {
      allRequiredChunks.addAll(player.getLoadedChunks());
      allRequiredChunks.addAll(player.getEnqueuedChunks());
    }
    //    Log.info("Start unload..." + allRequiredChunks.size());
    //    Log.info("Required chunks: " + allRequiredChunks.size());
    world.unloadUnusedChunks(allRequiredChunks);
  }

  /** Processes all logic for a single tick, including packet dispatching. */
  private void update() {
    // INPUT
    processIncomingPackets();

    // GAME LOGIC update Game State
    updatePlayers();
    updateWorld();

    // STREAMING CHUNKS
    updateChunkStreaming();

    // ENTITIES
    entityManager.update(playerManager.getAllPlayers());

    // UNLOAD (cleanup)
    unloadUnusedChunks();

    scheduler.tick(tick);

    flushNetwork();

    //    // TODO DEBUG Remove later
    //    if (tick % 100 == 0) {
    //      playerManager.broadcast(new ChatMessagePacket("tps: " + tpsCounter.getTps() + ""));
    //    }
  }

  public void shutdown() {
    if (!running && tick > 0) return;

    running = false;

    Log.info("Saving world before shutdown...");
    world.saveDirtyChunks();
    Log.info("World saved.");

    try {
      if (serverSocket != null && !serverSocket.isClosed()) {
        serverSocket.close();
      }
    } catch (Exception ignored) {
    }
  }

  // ---------------------------------------------------------------------------

  public long getTick() {
    return tick;
  }

  public ServerScheduler getScheduler() {
    return scheduler;
  }

  public void runTaskLater(long delay, Runnable task) {
    scheduler.schedule(tick, delay, task);
  }

  public void runTaskTimer(long delay, long period, Runnable task) {
    scheduler.scheduleRepeating(tick, delay, period, task);
  }

  public void runNextTick(Runnable task) {
    scheduler.schedule(tick, 1, task);
  }

  // ---------------------------------------------------------------------------

  public ServerWorld getWorld() {
    return world;
  }

  public EventBus getEventBus() {
    return eventBus;
  }

  public PlayerManager getPlayerManager() {
    return playerManager;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  //  public void broadcastMessage(String message) {
  //    getPlayerManager().broadcast(new ChatMessagePacket(message));
  //  }

  //  public boolean hasPermission(UUID playerId, String permission) {
  //    return permissionService.hasPermission(playerId, permission);
  //  }

  public Collection<Command> getCommands() {
    return commandRegistry.getCommands();
  }

  public UseCaseRegistry getUseCases() {
    return useCases;
  }
}
