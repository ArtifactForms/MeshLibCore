package engine.runtime.debug.core.render;

import engine.render.Graphics;
import engine.runtime.debug.core.command.DebugPointCommand;
import math.Vector3f;

public class DebugPointRenderer implements DebugCommandRenderer<DebugPointCommand> {

  @Override
  public Class<DebugPointCommand> getCommandType() {
    return DebugPointCommand.class;
  }

  @Override
  public void render(Graphics g, DebugPointCommand command) {
    float h = command.size * 0.5f;
    Vector3f p = command.position;

    g.setColor(command.color);

    g.drawLine(new Vector3f(p.x - h, p.y, p.z), new Vector3f(p.x + h, p.y, p.z));
    g.drawLine(new Vector3f(p.x, p.y - h, p.z), new Vector3f(p.x, p.y + h, p.z));
    g.drawLine(new Vector3f(p.x, p.y, p.z - h), new Vector3f(p.x, p.y, p.z + h));
  }
}
