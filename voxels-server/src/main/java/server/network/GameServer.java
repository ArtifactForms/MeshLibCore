package server.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import common.logging.Log;
import server.commands.Command;
import server.commands.CommandRegistry;
import server.commands.commands.PrivateMessageCommand;
import server.commands.commands.StopCommand;
import server.commands.commands.TeleportCommand;
import server.entity.EntityManager;
import server.events.EventBus;
import server.gateways.EventAdapter;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.gateways.InventoryAdapter;
import server.gateways.InventoryGateway;
import server.gateways.PermissionAdapter;
import server.gateways.PermissionGateway;
import server.gateways.WorldAdapter;
import server.gateways.WorldGateway;
import server.permissions.AlwaysGrantPermissionService;
import server.permissions.PermissionService;
import server.scheduler.ServerScheduler;
import server.usecases.UseCaseRegistry;
import server.world.ServerWorld;

/**
 * The core server class that manages the network lifecycle and the main game loop. It coordinates
 * incoming connections and ensures all game logic is executed synchronously within the main tick
 * loop.
 */
public class GameServer {

  private long tick = 0;

  private final int port;
  private volatile boolean running = true;
  private ServerSocket serverSocket;

  private final ServerScheduler scheduler;
  private final CommandRegistry commandRegistry;
  private final EventBus eventBus;
  private final PlayerManager playerManager;
  private final EntityManager entityManager;
  private ServerWorld world;

  private final PermissionService permissionService;

  private UseCaseRegistry useCases;

  /**
   * * Thread-safe queue for incoming packets. Packets are added by individual client threads and
   * consumed by the main game thread.
   */
  private final Queue<QueuedPacket> packetQueue = new ConcurrentLinkedQueue<>();

  public GameServer(int port) {
    this.port = port;

    this.scheduler = new ServerScheduler();
    this.commandRegistry = new CommandRegistry();
    this.eventBus = new EventBus();
    this.playerManager = new PlayerManager();
    this.entityManager = new EntityManager(this);
    this.world = new ServerWorld(this);

    this.permissionService = new AlwaysGrantPermissionService();
    //    this.permissionService = new AlwaysDenyPermissionService();

    initUseCases();
    registerCommands();
  }

  private void initUseCases() {
    WorldGateway worldGateway = new WorldAdapter(world);
    EventGateway eventGateway = new EventAdapter(eventBus);
    PermissionGateway permissionGateway = new PermissionAdapter(permissionService);
    InventoryGateway inventoryGateway = new InventoryAdapter(playerManager);
    GatewayContext context =
        new GatewayContext(worldGateway, eventGateway, permissionGateway, inventoryGateway);

    this.useCases = new UseCaseRegistry(context);
  }

  private void registerCommands() {
    registerCommand(new StopCommand());
    registerCommand(new TeleportCommand());
    registerCommand(new PrivateMessageCommand());
  }

  private void registerCommand(Command command) {
    commandRegistry.register(command);
    Log.info("Registered command: " + command.getName());
  }

  /**
   * Starts the server by initializing the socket, starting the network acceptor thread, and
   * entering the main game loop.
   */
  public void start() throws Exception {
    serverSocket = new ServerSocket(port);
    serverSocket.setReuseAddress(true);

    Log.info("Server running on port " + port);

    // THREAD 1: Network Acceptor Loop
    // Dedicated thread to listen for new TCP connections without blocking the game logic.
    Thread acceptorThread = new Thread(this::acceptLoop, "Network-Acceptor");
    acceptorThread.setDaemon(true);
    acceptorThread.start();

    // THREAD 2: Main Game Loop
    // Handles game logic and packet processing in synchronous ticks.
    gameLoop();
  }

  /** Continuously listens for new client connections. */
  private void acceptLoop() {
    try {
      while (running) {
        Socket socket = serverSocket.accept();

        Log.info("New connection: " + socket.getInetAddress());

        // ServerConnection starts its own internal read-thread upon instantiation
        new ServerConnection(this, socket, useCases);
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

  /** Processes all logic for a single tick, including packet dispatching. */
  private void update() {
    // Process all packets that have arrived since the last tick.
    // This ensures all handler logic runs on the main thread, avoiding race conditions.
    QueuedPacket qp;

    while ((qp = packetQueue.poll()) != null) {

      if (qp.connection().isRunning()) {
        qp.connection().getPacketDispatcher().dispatch(qp.packet());
      }
    }

    scheduler.tick(tick);

    entityManager.update(playerManager.getAllPlayers());

    // Placeholder for world logic (Physics, AI, Tile Entities, etc.)
    if (world != null) {
      world.tick();
    }
  }

  public void shutdown() {

    running = false;

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

  public CommandRegistry getCommandRegistry() {
    return commandRegistry;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public Queue<QueuedPacket> getPacketQueue() {
    return packetQueue;
  }

  public boolean hasPermission(UUID player, String permission) {
    return permissionService.hasPermission(player, permission);
  }
}
