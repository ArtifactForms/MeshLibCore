package server.network;

public class MetricsHistory {

  private final long[] tickDurations;

  private int index = 0;

  public MetricsHistory(int size) {
    this.tickDurations = new long[size];
  }

  public void add(long duration) {
    tickDurations[index++ % tickDurations.length] = duration;
  }

  public long getAvg() {
    long sum = 0;
    for (long d : tickDurations) sum += d;
    return sum / tickDurations.length;
  }

  public long getMax() {
    long max = 0;
    for (long d : tickDurations) max = Math.max(max, d);
    return max;
  }
}
