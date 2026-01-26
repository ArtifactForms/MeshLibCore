package engine.debug.core.render;

import engine.debug.core.command.DebugLineCommand;
import workspace.ui.Graphics;

public final class DebugLineRenderer implements DebugCommandRenderer<DebugLineCommand> {

  @Override
  public Class<DebugLineCommand> getCommandType() {
    return DebugLineCommand.class;
  }

  @Override
  public void render(Graphics g, DebugLineCommand command) {
    g.setColor(command.color);
    g.drawLine(command.a.x, command.a.y, command.a.z, command.b.x, command.b.y, command.b.z);
  }
}
