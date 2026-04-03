package common.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Base class for all network connections. Runs a background thread to read incoming packets and
 * provides a synchronized method to send packets.
 */
public abstract class Connection implements Runnable {

  protected Socket socket;
  protected DataInputStream in;
  protected DataOutputStream out;
  protected PacketBuffer buffer;
  protected volatile boolean running = true;

  public Connection(Socket socket) throws Exception {
    this.socket = socket;
    if (socket != null) {
      this.socket.setReuseAddress(true);
      this.socket.setKeepAlive(true);
      initStreams();
    }
  }

  protected void initStreams() throws IOException {
    this.in = new DataInputStream(socket.getInputStream());
    this.out = new DataOutputStream(socket.getOutputStream());
    this.buffer = new PacketBuffer(in, out);
  }

  @Override
  public void run() {
    try {
      while (running && !socket.isClosed()) {
        // Read Packet ID (Blocks until data is available)
        int id = buffer.readInt();

        Packet packet = PacketRegistry.create(id);
        if (packet == null) {
          System.err.println("[Network] Received unknown Packet ID: " + id);
          continue;
        }

        packet.read(buffer);

        handle(packet);
      }
    } catch (IOException e) {
      if (running) {
        System.out.println("[Network] Connection lost: " + e.getMessage());
      }
    } finally {
      close();
    }
  }

  /**
   * Sends a packet to the remote endpoint. Synchronized to prevent packet corruption when called
   * from multiple threads.
   */
  public void send(Packet packet) {
	  if (!running || buffer == null) return;

	  synchronized (buffer) {
	    try {
	      buffer.writeInt(packet.getId());
	      packet.write(buffer);

	      buffer.flush();
	      out.flush();
	    } catch (Exception e) {
	      System.err.println("[Network] Failed to send packet " + packet.getId());
	      close();
	    }
	  }
	}

  /**
   * Handover point for received packets. Implementations should typically queue the packet for the
   * main thread.
   */
  protected abstract void handle(Packet packet);

  public void close() {
    if (!running) return;
    running = false;
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
      System.out.println("[Network] Connection resources cleaned up.");
    } catch (IOException ignored) {
    }
    onClose();
  }

  public void close(Packet finalPacket) {
    if (!running) return;

    try {
      send(finalPacket);
      out.flush();
      socket.shutdownOutput();
      Thread.sleep(50);
    } catch (Exception e) {
      System.err.println("[Network] Error during graceful close: " + e.getMessage());
    } finally {
      close();
    }
  }

  public void onClose() {
    // Implementation is left to subclasses
  }

  /** @return true if the connection is active and the read-loop is running. */
  public boolean isRunning() {
    return running;
  }
}
