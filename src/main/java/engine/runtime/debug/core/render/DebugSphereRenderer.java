package engine.runtime.debug.core.render;

import engine.runtime.debug.core.command.DebugSphereCommand;
import math.Mathf;
import workspace.ui.Graphics;

class DebugSphereRenderer implements DebugCommandRenderer<DebugSphereCommand> {

  @Override
  public Class<DebugSphereCommand> getCommandType() {
    return DebugSphereCommand.class;
  }

  public void render(Graphics g, DebugSphereCommand command) {
    g.setColor(command.color);

    g.pushMatrix();

    g.translate(command.center.x, command.center.y, command.center.z);
    g.drawOval(-command.radius, -command.radius, command.radius * 2, command.radius * 2);

    g.rotateY(Mathf.HALF_PI);
    g.drawOval(-command.radius, -command.radius, command.radius * 2, command.radius * 2);

    g.rotateX(Mathf.HALF_PI);
    g.drawOval(-command.radius, -command.radius, command.radius * 2, command.radius * 2);

    g.popMatrix();
  }
}
