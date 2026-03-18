package server.network;

import java.net.Socket;

import common.network.Connection;
import common.network.Packet;
import server.player.ServerPlayer;
import server.usecases.UseCaseRegistry;

/**
 * Represents a server-side network connection to a specific client. Extends the base Connection to
 * handle player state and packet queueing.
 */
public class ServerConnection extends Connection {

  private static final int MAX_OUTBOUND_BATCH_SIZE = 64;
  private static final long WRITER_POLL_TIMEOUT_MS = 10L;

  private volatile ServerPlayer player; // Initialized as null until the player officially joins
  private final GameServer server;
  private final ServerPacketDispatcher packetDispatcher;
  private final java.util.concurrent.LinkedBlockingQueue<Packet> outboundQueue =
      new java.util.concurrent.LinkedBlockingQueue<>();
  private final Thread writerThread;

  /**
   * Initializes a new server connection, sets up the dispatcher, and starts the listener thread.
   *
   * @param socket The client socket.
   * @throws Exception If the socket streams cannot be initialized.
   */
  public ServerConnection(GameServer server, Socket socket, UseCaseRegistry useCases)
      throws Exception {
    super(socket);
    this.server = server;
    this.packetDispatcher = new ServerPacketDispatcher(this, useCases);

    this.writerThread =
        new Thread(this::writeLoop, "Server-Client-Writer-" + socket.getInetAddress());
    this.writerThread.setDaemon(true);
    this.writerThread.start();

    // Start the background thread for reading incoming data (Connection.run())
    Thread thread = new Thread(this, "Server-Client-" + socket.getInetAddress());
    thread.setDaemon(true);
    thread.start();
  }

  /**
   * Called by the base Connection class when a packet is received. Instead of processing it
   * immediately, we queue it for the main game thread.
   *
   * @param packet The received packet.
   */
  @Override
  protected void handle(Packet packet) {
    // Enqueue the packet to ensure thread-safe processing in the Main Game Loop
    server.getPacketQueue().add(new QueuedPacket(this, packet));
  }

  @Override
  public void send(Packet packet) {
    if (!running || packet == null) return;
    outboundQueue.offer(packet);
  }

  private void writeLoop() {
    java.util.List<Packet> batch = new java.util.ArrayList<>(MAX_OUTBOUND_BATCH_SIZE);

    try {
      while (running || !outboundQueue.isEmpty()) {
        Packet first =
            outboundQueue.poll(
                WRITER_POLL_TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS);
        if (first == null) {
          continue;
        }

        batch.add(first);
        outboundQueue.drainTo(batch, MAX_OUTBOUND_BATCH_SIZE - 1);
        writeBatch(batch);
        batch.clear();
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      if (!batch.isEmpty()) {
        try {
          writeBatch(batch);
        } catch (Exception ignored) {
        }
      }
    }
  }

  private void writeBatch(java.util.List<Packet> batch) {
    if (batch.isEmpty() || buffer == null) return;

    synchronized (buffer) {
      try {
        for (Packet packet : batch) {
          writePacket(packet);
        }
        flushOutput();
      } catch (Exception e) {
        Packet failedPacket = batch.get(0);
        System.err.println("[Network] Failed to send packet " + failedPacket.getId());
        close();
      }
    }
  }

  /**
   * Closes the connection and cleans up resources. Unregisters the connection from the
   * PlayerManager to trigger leave events.
   */
  @Override
  public void close() {
    // Close sockets and set running = false via base class
    super.close();
    writerThread.interrupt();

    // Notify manager to handle player logout/cleanup
    server.getPlayerManager().removeConnection(this);
  }

  /** @return true if the connection is still active and listening. */
  public boolean isRunning() {
    return running;
  }

  /** @return The dispatcher responsible for routing this connection's packets. */
  public ServerPacketDispatcher getPacketDispatcher() {
    return packetDispatcher;
  }

  public ServerPlayer getPlayer() {
    return player;
  }

  public void setPlayer(ServerPlayer player) {
    this.player = player;
  }

  public GameServer getServer() {
    return server;
  }
}
