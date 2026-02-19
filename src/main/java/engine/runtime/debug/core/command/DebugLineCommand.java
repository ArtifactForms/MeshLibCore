package engine.runtime.debug.core.command;

import math.Color;
import math.Vector3f;

public final class DebugLineCommand extends DebugCommand {

  public final Vector3f a;
  public final Vector3f b;
  public final Color color;

  public DebugLineCommand(Vector3f a, Vector3f b, Color color) {
    this.a = new Vector3f(a);
    this.b = new Vector3f(b);
    this.color = color;
  }
}
