package engine.debug;

public class PerformanceMonitor {

  private static PerformanceMonitor instance;

  private int sceneNodeCount = 0;

  private int meshCount = 0;

  private int lightCount = 0;

  private PerformanceMonitor() {}

  public static PerformanceMonitor getInstance() {
    if (instance == null) {
      instance = new PerformanceMonitor();
    }
    return instance;
  }

  public void incrementSceneNodeCount() {
    sceneNodeCount++;
  }

  public void decrementSceneNodeCount() {
    sceneNodeCount--;
  }

  public void incrementMeshCount() {
    meshCount++;
  }

  public void decrementMeshCount() {
    meshCount--;
  }

  public void incrementLightCount() {
    lightCount++;
  }

  public void decrementLightCount() {
    lightCount--;
  }

  public int getSceneNodeCount() {
    return sceneNodeCount;
  }

  public String getStatistics() {
    return String.format(
        "Scene Nodes: %d, Meshes: %d, Lights: %d", sceneNodeCount, meshCount, lightCount);
  }
}
