package engine.debug.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import engine.debug.core.command.DebugCommand;
import engine.debug.core.render.DebugRenderer;
import workspace.ui.Graphics;

public final class DebugContext {

  private boolean visible = true;
  private final List<DebugCommand> commands = new ArrayList<>();
  private final DebugRenderer renderer = new DebugRenderer();

  public void submit(DebugCommand cmd) {
    commands.add(cmd);
  }

  public void update(float tpf) {
    Iterator<DebugCommand> it = commands.iterator();
    while (it.hasNext()) {
      DebugCommand c = it.next();
      if (c.ttl > 0f) {
        c.ttl -= tpf;
        if (c.ttl <= 0f) {
          it.remove();
        }
      }
    }
  }

  public void render(Graphics g) {
    if (!visible) return;
    renderer.render(g, commands);
  }

  /**
   * ttl == 0 → exakt 1 Frame
   *
   * <p>ttl > 0 → korrekt zeitbasiert
   *
   * <p>ttl < 0 → lebt ewig
   */
  public void clearFrameCommands() {
    commands.removeIf(c -> c.ttl == DebugCommand.ONE_FRAME);
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }
}
