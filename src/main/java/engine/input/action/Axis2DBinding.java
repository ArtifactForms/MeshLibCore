package engine.input.action;

import engine.input.Key;
import math.Vector2f;

/** Maps keys to a 2D axis contribution. Example: W -> (0, +1), A -> (-1, 0) */
public class Axis2DBinding {

  public final Action action;
  public final Key key;
  public final Vector2f value;

  public Axis2DBinding(Action action, Key key, Vector2f value) {
    this.action = action;
    this.key = key;
    this.value = value;
  }
}
