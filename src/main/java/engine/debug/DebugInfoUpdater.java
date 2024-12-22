package engine.debug;

import java.lang.management.ManagementFactory;
import java.util.Collection;

import com.sun.management.OperatingSystemMXBean;

import engine.Timer;
import engine.input.Input;
import engine.input.Key;
import engine.scene.Scene;

/**
 * The {@code DebugInfoUpdater} class is responsible for updating debug information displayed by a
 * {@code DebugOverlay}. It collects information from various sources such as the {@code Timer},
 * {@code Scene}, and {@code Input}.
 */
public class DebugInfoUpdater {

  private static final String CATEGORY_TIME = "Time";

  private static final String CATEGORY_INPUT = "Input";

  private static final String CATEGORY_SCENE = "Scene";

  private static final String CATEGORY_SYSTEM = "System";
  
  private static final String CATEGORY_OS = "OS";

  private final DebugOverlay debugOverlay;

  private final PerformanceMetrics performanceMetrics = new PerformanceMetrics();

  private final MemoryMetrics memoryMetrics = new MemoryMetrics();

  private final DrawCallCounter drawCallCounter = new DrawCallCounter();

  private final OperatingSystemMXBean osBean =
      (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

  /**
   * Constructs a new {@code DebugInfoUpdater} with the given {@code DebugOverlay}.
   *
   * @param debugOverlay the overlay where debug information will be displayed.
   * @throws IllegalArgumentException if {@code debugOverlay} is {@code null}.
   */
  public DebugInfoUpdater(DebugOverlay debugOverlay) {
    if (debugOverlay == null) {
      throw new IllegalArgumentException("DebugOverlay cannot be null.");
    }
    this.debugOverlay = debugOverlay;
  }

  private void setInfo(String category, String key, String value) {
    debugOverlay.setDebugItem(category, key, value);
  }

  private void setInfo(String category, String key, float value) {
    setInfo(category, key, String.valueOf(value));
  }

  private void setInfo(String category, String key, boolean value) {
    setInfo(category, key, String.valueOf(value));
  }

  private void setInfo(String category, String key, int value) {
    setInfo(category, key, String.valueOf(value));
  }

  /**
   * Updates debug information by collecting data from the given sources.
   *
   * @param timer the {@code Timer} providing time-related debug information.
   * @param activeScene the active {@code Scene}, or {@code null} if no scene is active.
   * @param input the {@code Input} providing input-related debug information.
   */
  public void update(Timer timer, Scene activeScene, Input input) {
    updateTimeMetrics(timer);
    updatePerformanceMetrics();
    updateInputMetrics(input);
    if (activeScene != null) {
      updateSceneMetrics(activeScene);
    }
    updateOsMetrics();
  }

  private String keysToString(Collection<Key> keys) {
    String pressedKeys = "";
    for (Key key : keys) {
      pressedKeys += key + " ";
    }
    return pressedKeys;
  }
  
  private void updateOsMetrics() {
      setInfo(CATEGORY_OS, "Name", osBean.getName());
      setInfo(CATEGORY_OS, "Arch", osBean.getArch());
      setInfo(CATEGORY_OS, "Processors", osBean.getAvailableProcessors());
      setInfo(CATEGORY_OS, "Version", osBean.getVersion());
  }

  private void updateInputMetrics(Input input) {
    setInfo(CATEGORY_INPUT, "Mouse", "x=" + input.getMouseX() + ", y=" + input.getMouseY());
    setInfo(CATEGORY_INPUT, "Keys pressed", keysToString(input.getPressedKeys()));
  }

  private void updateSceneMetrics(Scene activeScene) {
    setInfo(CATEGORY_SCENE, "Scene", activeScene.getName());
    setInfo(CATEGORY_SCENE, "Root count", activeScene.getRootCount());
    setInfo(CATEGORY_SCENE, "Lights count", activeScene.getLightCount());
    setInfo(CATEGORY_SCENE, "Wireframe mode", activeScene.isWireframeMode());
  }

  private void updateTimeMetrics(Timer timer) {
    setInfo(CATEGORY_TIME, "Time per frame (tpf)", timer.getTimePerFrame());
    setInfo(CATEGORY_TIME, "Frames per second (fps)", timer.getFrameRate());
    setInfo(CATEGORY_TIME, "Frames rendered", timer.getFrameCount());
    setInfo(CATEGORY_TIME, "Total time (hh:mm:ss)", timer.getFormattedTotalTime());
    setInfo(CATEGORY_TIME, "Time scale", timer.getTimeScale());
  }

  private void updatePerformanceMetrics() {
    setInfo(CATEGORY_SYSTEM, "CPU Usage", performanceMetrics.getCpuUsage() + "%");
    setInfo(CATEGORY_SYSTEM, "Draw Calls", drawCallCounter.getCount());
    setInfo(CATEGORY_SYSTEM, "Memory", memoryMetrics.getMemoryInfo());
    setInfo(CATEGORY_SYSTEM, "Memory Used", memoryMetrics.getUsedMemory() / (1024 * 1024) + " MB");
  }
}
