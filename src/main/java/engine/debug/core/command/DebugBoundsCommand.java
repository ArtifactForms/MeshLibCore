package engine.debug.core.command;

import math.Bounds;
import math.Color;

public class DebugBoundsCommand extends DebugCommand {

  public Bounds bounds;
  public Color color;

  public DebugBoundsCommand(Bounds bounds, Color color) {
    this.bounds = bounds;
    this.color = color;
  }
}
