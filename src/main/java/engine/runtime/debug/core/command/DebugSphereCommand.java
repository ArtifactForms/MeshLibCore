package engine.runtime.debug.core.command;

import math.Color;
import math.Vector3f;

public final class DebugSphereCommand extends DebugCommand {
  public float radius;
  public Vector3f center;
  public Color color;

  public DebugSphereCommand(float radius, Vector3f center, Color color) {
    this.radius = radius;
    this.center = center;
    this.color = color;
  }
}
