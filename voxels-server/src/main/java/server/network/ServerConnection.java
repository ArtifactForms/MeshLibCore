package server.network;

import java.net.Socket;
import common.network.Connection;
import common.network.Packet;
import server.player.ServerPlayer;

/**
 * Represents a server-side network connection to a specific client. Extends the base Connection to
 * handle player state and packet queueing.
 */
public class ServerConnection extends Connection {

  private volatile ServerPlayer player; // Initialized as null until the player officially joins
  private final GameServer server;
  private final ServerPacketDispatcher packetDispatcher;

  /**
   * Initializes a new server connection, sets up the dispatcher, and starts the listener thread.
   *
   * @param socket The client socket.
   * @throws Exception If the socket streams cannot be initialized.
   */
  public ServerConnection(GameServer server, Socket socket) throws Exception {
    super(socket);
    this.server = server;
    this.packetDispatcher = new ServerPacketDispatcher(this);

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

  /**
   * Closes the connection and cleans up resources. Unregisters the connection from the
   * PlayerManager to trigger leave events.
   */
  @Override
  public void close() {
    // Close sockets and set running = false via base class
    super.close();

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
