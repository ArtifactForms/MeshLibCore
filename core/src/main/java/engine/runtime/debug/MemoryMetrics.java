package engine.runtime.debug;

public class MemoryMetrics {

  private final Runtime runtime = Runtime.getRuntime();

  public long getUsedMemory() {
    return runtime.totalMemory() - runtime.freeMemory();
  }

  public long getTotalMemory() {
    return runtime.totalMemory();
  }

  public long getMaxMemory() {
    return runtime.maxMemory();
  }

  public long getFreeMemory() {
    return runtime.freeMemory();
  }

  public String getMemoryInfo() {
    long usedMB = (getTotalMemory() - getFreeMemory()) / 1024 / 1024;
    long totalMemory = getTotalMemory() / 1024 / 1024;

    int percentage = 0;
    if (totalMemory > 0) {
      percentage = (int) (usedMB * 100 / totalMemory);
    }

    return percentage + "% (" + usedMB + ") of " + totalMemory + "MB";
  }
}
