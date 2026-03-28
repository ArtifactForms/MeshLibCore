package client.network;

public class PingTracker {

  private int ping;

  public void update(long sentTime) {
    int newPing = (int) ((System.nanoTime() - sentTime) / 1_000_000);

    if (newPing < 0 || newPing > 10_000) return;

    if (ping == 0) {
      ping = newPing;
      return;
    }

    ping = (int) (ping * 0.8 + newPing * 0.2);
  }

  public int getPing() {
    return ping;
  }
}
