package engine.debug;

import java.util.LinkedList;
import java.util.Queue;

public class MetricHistory {

  private final Queue<Long> memoryHistory = new LinkedList<>();

  private final int maxSamples = 300;

  public void addSample(long memoryUsage) {
    if (memoryHistory.size() >= maxSamples) {
      memoryHistory.poll(); // Remove the oldest sample
    }
    memoryHistory.add(memoryUsage);
  }

  public Queue<Long> getHistory() {
    return memoryHistory;
  }
}
