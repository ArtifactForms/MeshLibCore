package engine.input;

import java.util.Collection;

public interface Gamepad {

  /**
   * Human-readable name (e.g. "Xbox Controller").
   */
  String getName();

  /**
   * Checks if a button is currently pressed.
   */
  boolean isButtonPressed(GamepadButton button);

  /**
   * Returns the current value of an axis in range [-1, 1].
   */
  float getAxis(GamepadAxis axis);

  /**
   * Returns all currently pressed buttons.
   */
  Collection<GamepadButton> getPressedButtons();
}