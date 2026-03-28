package client.network;

import common.network.packets.system.PingPacket;

public class PingSystem {

  private final ClientNetwork network;

  private long lastPingTime;
  private static final long INTERVAL = 2_000_000_000L; // 2 sec in ns

  public PingSystem(ClientNetwork network) {
    this.network = network;
  }

  public void update() {
    long now = System.nanoTime();

    if (now - lastPingTime >= INTERVAL) {
      sendPing(now);
      lastPingTime = now;
    }
  }

  private void sendPing(long time) {
    network.send(new PingPacket(time));
  }
}
