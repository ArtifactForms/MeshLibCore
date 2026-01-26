package engine.debug.core.render;

import engine.debug.core.command.DebugGridCommand;
import math.Color;
import math.Vector3f;
import workspace.ui.Graphics;

public final class DebugGridRenderer implements DebugCommandRenderer<DebugGridCommand> {

  @Override
  public Class<DebugGridCommand> getCommandType() {
    return DebugGridCommand.class;
  }

  int cells;
  Vector3f origin;
  float spacing;
  float majorLineEvery;
  Color majorColor;
  Color minorColor;

  public void render(Graphics g, DebugGridCommand cmd) {
    set(cmd);

    float half = cells * spacing * 0.5f;
    float y = origin.y - 0.01f; // slight offset against z-fighting

    // vertical lines (constant X, along Z)
    for (int x = 0; x <= cells; x++) {

      boolean major = (x % majorLineEvery) == 0;
      Color color = major ? majorColor : minorColor;

      float px = origin.x - half + x * spacing;

      g.setColor(color);
      g.drawLine(new Vector3f(px, y, origin.z - half), new Vector3f(px, y, origin.z + half));
    }

    // horizontal lines (constant Z, along X)
    for (int z = 0; z <= cells; z++) {

      boolean major = (z % majorLineEvery) == 0;
      Color color = major ? majorColor : minorColor;

      float pz = origin.z - half + z * spacing;

      g.setColor(color);
      g.drawLine(new Vector3f(origin.x - half, y, pz), new Vector3f(origin.x + half, y, pz));
    }
  }

  private void set(DebugGridCommand cmd) {
    cells = cmd.cells;
    origin = cmd.origin;
    spacing = cmd.spacing;
    majorLineEvery = cmd.majorLineEvery;
    majorColor = cmd.majorColor;
    minorColor = cmd.minorColor;
  }
}
