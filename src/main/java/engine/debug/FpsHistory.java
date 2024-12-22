package engine.debug;

import java.util.LinkedList;
import java.util.Queue;

public class FpsHistory {

  private final Queue<Float> fpsHistory = new LinkedList<>();

  private final int maxSamples = 300; // Max samples to display in the graph

  public void addSample(float fps) {
    if (fpsHistory.size() >= maxSamples) {
      fpsHistory.poll(); // Remove the oldest FPS value
    }
    fpsHistory.add(fps);
  }

  public Queue<Float> getHistory() {
    return fpsHistory;
  }

  public float getMaxFps() {
    return fpsHistory.stream().max(Float::compare).orElse(60.0f);
  }

  public int getMaxSamples() {
    return maxSamples;
  }
}
