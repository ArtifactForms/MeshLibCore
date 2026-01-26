package engine.debug.core.command;

import math.Color;
import math.Vector3f;

public class DebugGridCommand extends DebugCommand {

  public int cells;
  public float spacing;
  public int majorLineEvery;
  public Vector3f origin;
  public Color majorColor;
  public Color minorColor;

  public DebugGridCommand(
      int cells,
      float spacing,
      int majorLineEvery,
      Vector3f origin,
      Color majorColor,
      Color minorColor) {
    this.cells = cells;
    this.spacing = spacing;
    this.majorLineEvery = majorLineEvery;
    this.origin = origin;
    this.majorColor = majorColor;
    this.minorColor = minorColor;
  }
}
