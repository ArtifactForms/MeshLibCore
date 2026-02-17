package engine.debug.core.render;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.debug.core.command.DebugCommand;
import engine.debug.core.config.DebugDepthMode;
import workspace.ui.Graphics;

public final class DebugRenderer {

  private static final class BatchKey {

    final Class<? extends DebugCommand> type;
    final DebugDepthMode depthMode;

    BatchKey(Class<? extends DebugCommand> type, DebugDepthMode depthMode) {
      this.type = type;
      this.depthMode = depthMode;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof BatchKey)) {
        return false;
      }
      BatchKey other = (BatchKey) o;
      return type == other.type && depthMode == other.depthMode;
    }

    @Override
    public int hashCode() {
      int result = type.hashCode();
      result = 31 * result + depthMode.hashCode();
      return result;
    }
  }

  private final Map<Class<? extends DebugCommand>, DebugCommandRenderer<?>> renderers =
      new HashMap<>();

  public DebugRenderer() {
    register(new DebugLineRenderer());
    register(new DebugGridRenderer());
    register(new DebugSphereRenderer());
    register(new DebugPointRenderer());
    register(new DebugRayRenderer());
    register(new DebugAxisRenderer());
    register(new DebugBoundsRenderer());
    register(new DebugCapsuleRenderer());
  }

  public <T extends DebugCommand> void register(DebugCommandRenderer<T> renderer) {
    renderers.put(renderer.getCommandType(), renderer);
  }

  public void render(Graphics g, List<DebugCommand> commands) {

    Map<BatchKey, List<DebugCommand>> batches = new HashMap<>();

    // 1️Gruppieren
    for (DebugCommand cmd : commands) {
      BatchKey key = new BatchKey(cmd.getClass(), cmd.depthMode);
      batches.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(cmd);
    }

    // Rendern pro Batch
    DebugDepthMode currentDepthMode = null;

    for (Map.Entry<BatchKey, List<DebugCommand>> entry : batches.entrySet()) {

      BatchKey key = entry.getKey();
      List<DebugCommand> batch = entry.getValue();

      DebugCommandRenderer renderer = renderers.get(key.type);
      if (renderer == null) continue;

      if (key.depthMode != currentDepthMode) {
        applyDepthMode(g, key.depthMode);
        currentDepthMode = key.depthMode;
      }

      renderer.renderBatch(g, batch);
    }

    g.enableDepthTest(); // Reset
  }

  private void applyDepthMode(Graphics g, DebugDepthMode mode) {
    switch (mode) {
      case NO_DEPTH:
        g.disableDepthTest();
        break;
      case DEPTH_TESTED:
        g.enableDepthTest();
        break;
    }
  }
}
