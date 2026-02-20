package engine.runtime.debug.core.render;

import engine.runtime.debug.core.command.DebugRayCommand;
import math.Ray3f;
import workspace.ui.Graphics;

public class DebugRayRenderer implements DebugCommandRenderer<DebugRayCommand> {

  @Override
  public Class<DebugRayCommand> getCommandType() {
      return DebugRayCommand.class;
  }

  @Override
  public void render(Graphics g, DebugRayCommand command) {
      Ray3f ray = new Ray3f(command.origin, command.direction);
      g.setColor(command.color);
      g.drawLine(command.origin, ray.getPointAt(command.length));
  }
}
