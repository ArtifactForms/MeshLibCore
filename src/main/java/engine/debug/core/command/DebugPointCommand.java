package engine.debug.core.command;

import math.Color;
import math.Vector3f;

public class DebugPointCommand extends DebugCommand {

  public float size;
  public Vector3f position;
  public Color color;

  public DebugPointCommand(float size, Vector3f position, Color color) {
    this.size = size;
    this.position = position;
    this.color = color;
  }
}
