package engine.debug.core.command;

import math.Color;
import math.Vector3f;

public class DebugCapsuleCommand extends DebugCommand {

  public float radius;
  public float halfHeight;
  public Vector3f center;
  public Color color;

  public DebugCapsuleCommand(
      float radius, float halfHeight, Vector3f center, Color color) {
    this.radius = radius;
    this.halfHeight = halfHeight;
    this.center = center;
    this.color = color;
  }
}
