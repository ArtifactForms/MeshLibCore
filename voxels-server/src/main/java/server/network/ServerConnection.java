package server.network;

import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import common.network.Connection;
import common.network.Packet;
import server.gateways.GatewayContext;
import server.player.ServerPlayer;
import server.usecases.UseCaseRegistry;

/**
 * Represents a server-side network connection to a specific client. Handles inbound and outbound
 * packet queues to support thread-safe sending and processing.
 */
public class ServerConnection extends Connection {

  private volatile ServerPlayer player; // Initialized after player joins
  private final GameServer server;
  private final ServerPacketDispatcher packetDispatcher;

  /** Thread-safe queue for incoming packets (main thread will poll) */
  private final Queue<Packet> incomingPackets = new ConcurrentLinkedQueue<>();

  /** Thread-safe queue for outbound packets (sending delayed / throttled) */
  private final Queue<Packet> outgoingPackets = new ConcurrentLinkedQueue<>();

  public ServerConnection(
      GameServer server, Socket socket, UseCaseRegistry useCases, GatewayContext context)
      throws Exception {
    super(socket);
    this.server = server;
    this.packetDispatcher = new ServerPacketDispatcher(this, useCases, context);

    // Register this connection with PlayerManager
    server.getPlayerManager().addConnection(this);

    // Start listening for incoming data
    Thread thread = new Thread(this, "Server-Client-" + socket.getInetAddress());
    thread.setDaemon(true);
    thread.start();
  }

  // ============================
  // Inbound Handling
  // ============================

  /** Called by base class when a packet is received */
  @Override
  protected void handle(Packet packet) {
    incomingPackets.add(packet);
  }

  /** Polls the next inbound packet (main game thread should call this) */
  public Packet pollPacket() {
    return incomingPackets.poll();
  }

  public int getQueueSize() {
    return incomingPackets.size();
  }

//  @Override
//  public void send(Packet packet) {
//    if (!running || packet == null) return;
//    enqueueOutbound(packet);
//  }

  // ============================
  // Outbound Handling
  // ============================

  /** Adds a packet to the outbound queue. Thread-safe, can be called from any thread. */
  public void enqueueOutbound(Packet packet) {
    if (packet != null && running) {
      outgoingPackets.add(packet);
    }
  }
  
  @Override
  public void send(Packet packet) {
	  enqueueOutbound(packet);
  }

  /**
   * Sends queued outbound packets to the client. Should be called once per server tick (can apply
   * throttling).
   */
  public void flushOutbound(int maxPacketsPerTick) {
    int sent = 0;

    while (sent < maxPacketsPerTick && running && !outgoingPackets.isEmpty()) {
      Packet packet = outgoingPackets.poll();
      if (packet != null) {
        try {
          super.send(packet); // Uses base Connection send logic
          sent++;
        } catch (Exception e) {
          System.err.println("[ServerConnection] Failed to send packet: " + e.getMessage());
          close(); // Close connection on fatal send error
          break;
        }
      }
    }
  }

  /** Flush all remaining packets without limit (for shutdown or immediate sync) */
  public void flushAllOutbound() {
    while (running && !outgoingPackets.isEmpty()) {
      Packet packet = outgoingPackets.poll();
      if (packet != null) {
        try {
          super.send(packet);
        } catch (Exception e) {
          System.err.println(
              "[ServerConnection] Failed to send packet during flushAll: " + e.getMessage());
          close();
          break;
        }
      }
    }
  }

  // ============================
  // Player & Server Accessors
  // ============================

  public ServerPlayer getPlayer() {
    return player;
  }
  
  public boolean hasPlayer() {
	  return player!= null;
  }

  public void setPlayer(ServerPlayer player) {
    this.player = player;
  }

  public GameServer getServer() {
    return server;
  }

  public ServerPacketDispatcher getPacketDispatcher() {
    return packetDispatcher;
  }

  public boolean isRunning() {
    return running;
  }

  // ============================
  // Connection Lifecycle
  // ============================

  @Override
  public void close() {
    super.close(); // Closes socket and stops reading thread
    server.getPlayerManager().removeConnection(this);
  }
}
