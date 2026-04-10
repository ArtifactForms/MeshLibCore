package client.network;

import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import client.app.GameClient;
import client.scene.ConnectionLostScene;
import common.network.Connection;
import common.network.Packet;
import common.network.packets.system.PongPacket;

/**
 * Manages the client-side network connection. This class inherits from {@link Connection} and
 * implements a packet queue system to ensure network data is processed safely on the main rendering
 * thread.
 */
public class ClientNetwork extends Connection {

  private final GameClient client;

  /** Dispatcher responsible for routing packets to their respective handlers */
  private final ClientPacketDispatcher packetDispatcher;

  /**
   * * Thread-safe queue for incoming packets. Packets are received on a background thread but
   * processed on the main thread.
   */
  private final Queue<Packet> packetQueue = new ConcurrentLinkedQueue<>();

  public ClientNetwork(GameClient client) throws Exception {
    // Initialize the base connection without a socket (socket is set in connect())
    super(null);
    this.client = client;
    this.packetDispatcher = new ClientPacketDispatcher(client);
  }

  /**
   * Establishes a connection to the game server. * @param host The server IP address or hostname.
   *
   * @throws Exception if the connection fails or streams cannot be initialized.
   */
  public void connect(String host, int port) throws Exception {
    this.socket = new Socket(host, port);
    this.initStreams();

    this.running = true;

    // Start the background listening thread
    Thread thread = new Thread(this, "Client-Network-Thread");
    thread.setDaemon(true); // Ensures the thread stops when the app closes
    thread.start();

    System.out.println("[Client] Successfully connected to server: " + host);
  }

  /**
   * Receives packets from the network thread and stores them in the queue. This prevents concurrent
   * modification issues by delaying processing until the main thread is ready. * @param packet The
   * packet received from the server.
   */
  @Override
  protected void handle(Packet packet) {
	  if (packet instanceof PongPacket pong) {
		  client.getPingTracker().update(pong.getTime());
		    return;
	  }
	  
    // Do not process logic here! Add to queue for main-thread synchronization.
    packetQueue.add(packet);
  }

  /**
   * Processes all queued packets. This method must be called once per frame from the Main Render
   * Loop.
   */
  public void update() {
    while (!packetQueue.isEmpty()) {
      Packet packet = packetQueue.poll();
      if (packet != null) {
        packetDispatcher.dispatch(packet);
      }
    }
  }

  @Override
  public void onClose() {
	  String message = "Connection timed out or server closed.";
	    if (!(client.getSceneManager().getActiveScene() instanceof ConnectionLostScene)) {
	        client.getSceneManager().setActiveScene(new ConnectionLostScene(client, message));
	    }
    client.onConnectionClosed();
  }
}
