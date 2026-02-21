package engine.runtime.debug.core.render;

import java.util.List;

import engine.render.Graphics;
import engine.runtime.debug.core.command.DebugCommand;

public interface DebugCommandRenderer<T extends DebugCommand> {

  Class<T> getCommandType();

  void render(Graphics g, T command);

  default void renderBatch(Graphics g, List<T> commands) {
    for (T c : commands) {
      render(g, c);
    }
  }
}
