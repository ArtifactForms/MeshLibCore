package server.network;

public class TPSCounter {

  private long totalTicks;

  private long lastTpsTime = System.currentTimeMillis();

  private int ticksThisSecond = 0;

  private double currentTps = 20.0;

  public void update(long tick) {
    this.totalTicks = tick;

    ticksThisSecond++;

    long now = System.currentTimeMillis();
    long elapsed = now - lastTpsTime;

    if (elapsed >= 1000) {
      currentTps = ticksThisSecond * (1000.0 / elapsed);
      ticksThisSecond = 0;
      lastTpsTime = now;

      //      if (tick % 100 == 0) {
      //  	    Log.info(String.format("TPS: %.2f", currentTps));
      //      }
    }
  }

  public double getTps() {
    return currentTps;
  }
}
