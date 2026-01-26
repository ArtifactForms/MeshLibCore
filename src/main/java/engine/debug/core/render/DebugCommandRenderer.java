package engine.debug.core.render;

import engine.debug.core.command.DebugCommand;
import workspace.ui.Graphics;

public interface DebugCommandRenderer<T extends DebugCommand> {

  Class<T> getCommandType();

  void render(Graphics g, T command);
}
