package engine.runtime.debug.core.render;

import engine.components.Transform;
import engine.rendering.Graphics;
import engine.runtime.debug.core.command.DebugAxisCommand;
import math.Color;
import math.Vector3f;

public class DebugAxisRenderer implements DebugCommandRenderer<DebugAxisCommand> {

  @Override
  public Class<DebugAxisCommand> getCommandType() {
    return DebugAxisCommand.class;
  }

  @Override
  public void render(Graphics g, DebugAxisCommand command) {
    float size = command.size;
    Transform t = command.transform;
    Vector3f origin = t.getPosition();

    Vector3f right = t.getRight().mult(size);
    Vector3f up = t.getUp().mult(size);
    Vector3f forward = t.getForward().mult(size);

    g.setColor(Color.RED);
    g.drawLine(origin, origin.add(right));

    g.setColor(Color.GREEN);
    g.drawLine(origin, origin.add(up));

    g.setColor(Color.BLUE);
    g.drawLine(origin, origin.add(forward));
  }
}
