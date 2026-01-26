package engine.debug.core.render;

import engine.debug.core.command.DebugBoundsCommand;
import math.Vector3f;
import workspace.ui.Graphics;

public class DebugBoundsRenderer implements DebugCommandRenderer<DebugBoundsCommand> {

  @Override
  public Class<DebugBoundsCommand> getCommandType() {
    return DebugBoundsCommand.class;
  }

  @Override
  public void render(Graphics g, DebugBoundsCommand command) {
    Vector3f min = command.bounds.getMin();
    Vector3f max = command.bounds.getMax();

    Vector3f v000 = new Vector3f(min.x, min.y, min.z);
    Vector3f v100 = new Vector3f(max.x, min.y, min.z);
    Vector3f v101 = new Vector3f(max.x, min.y, max.z);
    Vector3f v001 = new Vector3f(min.x, min.y, max.z);

    Vector3f v010 = new Vector3f(min.x, max.y, min.z);
    Vector3f v110 = new Vector3f(max.x, max.y, min.z);
    Vector3f v111 = new Vector3f(max.x, max.y, max.z);
    Vector3f v011 = new Vector3f(min.x, max.y, max.z);

    g.setColor(command.color);

    g.drawLine(v000, v100);
    g.drawLine(v100, v101);
    g.drawLine(v101, v001);
    g.drawLine(v001, v000);

    g.drawLine(v010, v110);
    g.drawLine(v110, v111);
    g.drawLine(v111, v011);
    g.drawLine(v011, v010);

    g.drawLine(v000, v010);
    g.drawLine(v100, v110);
    g.drawLine(v101, v111);
    g.drawLine(v001, v011);
  }
}
