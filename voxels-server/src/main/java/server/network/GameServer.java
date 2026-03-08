package server.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import server.events.EventBus;
import server.world.ServerWorld;

/**
 * The core server class that manages the network lifecycle and the main game loop. It coordinates
 * incoming connections and ensures all game logic is executed synchronously within the main tick
 * loop.
 */
public class GameServer {

  private final int port;
  private boolean running = true;
  private ServerSocket serverSocket;
  private static ServerWorld world;

  private static final EventBus EVENT_BUS = new EventBus();

  /**
   * * Thread-safe queue for incoming packets. Packets are added by individual client threads and
   * consumed by the main game thread.
   */
  public static final Queue<QueuedPacket> packetQueue = new ConcurrentLinkedQueue<>();

  public GameServer(int port) {
    this.port = port;
  }

  /**
   * Starts the server by initializing the socket, starting the network acceptor thread, and
   * entering the main game loop.
   */
  public void start() throws Exception {
    serverSocket = new ServerSocket(port);
    serverSocket.setReuseAddress(true);
    System.out.println("[Server] Running on port " + port);

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
        System.out.println("[Network] New connection: " + socket.getInetAddress());

        // ServerConnection starts its own internal read-thread upon instantiation
        new ServerConnection(socket);
      }
    } catch (Exception e) {
      if (running) e.printStackTrace();
    }
  }

  /** The heartbeat of the server. Regulates the tick rate (default: 20 TPS). */
  private void gameLoop() {
    System.out.println("[Server] Game Loop started.");

    while (running) {
      long startTime = System.currentTimeMillis();

      // Execute the tick update
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
        System.err.println("[Server] Can't keep up! Tick took " + delta + "ms");
      }
    }
  }

  /** Processes all logic for a single tick, including packet dispatching. */
  private void update() {
    // Process all packets that have arrived since the last tick.
    // This ensures all handler logic runs on the main thread, avoiding race conditions.
    while (!packetQueue.isEmpty()) {
      QueuedPacket qp = packetQueue.poll();

      if (qp != null && qp.connection().isRunning()) {
        // The Dispatcher routes the passive Packet object to its specific logic
        qp.connection().getPacketDispatcher().dispatch(qp.packet());
      }
    }
    
    server.entity.EntityManager.update(PlayerManager.getAllPlayers());

    // Placeholder for world logic (Physics, AI, Tile Entities, etc.)
    if (world != null) {
      // world.tick();
    }
  }

  public static ServerWorld getWorld() {
    return world;
  }

  public static void setWorld(ServerWorld world) {
    GameServer.world = world;
  }

  public static EventBus getEventBus() {
    return EVENT_BUS;
  }
}
