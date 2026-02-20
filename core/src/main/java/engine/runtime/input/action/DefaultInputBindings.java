package engine.runtime.input.action;

import engine.runtime.input.Key;
import math.Vector2f;

public final class DefaultInputBindings {

  private DefaultInputBindings() {}

  public static void apply(InputActions actions) {

    // Movement
    actions.bindAxis2D(Action.MOVE, Key.W, Vector2f.DOWN);
    actions.bindAxis2D(Action.MOVE, Key.S, Vector2f.UP);
    actions.bindAxis2D(Action.MOVE, Key.A, Vector2f.LEFT);
    actions.bindAxis2D(Action.MOVE, Key.D, Vector2f.RIGHT);

    // Buttons
    actions.bindButton(Action.JUMP, Key.SPACE);
    actions.bindButton(Action.SPRINT, Key.SHIFT);
  }
}
