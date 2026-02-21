package engine.runtime.debug.core.command;

import math.Color;
import math.Vector3f;

public class DebugRayCommand extends DebugCommand {

  public Vector3f origin;
  public Vector3f direction;
  public float length;
  public Color color;

  public DebugRayCommand(Vector3f origin, Vector3f direction, float length, Color color) {
    this.origin = origin;
    this.direction = direction;
    this.length = length;
    this.color = color;
  }
}
