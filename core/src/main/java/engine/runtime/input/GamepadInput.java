package engine.runtime.input;

import java.util.Collection;

/**
 * Interface for managing gamepad input in the engine. Gamepads are polled devices with buttons and
 * analog axes.
 */
public interface GamepadInput {

  /** Updates the state of all connected gamepads. Should be called once per game loop iteration. */
  void updateGamepadState();

  /** Returns all currently connected gamepads. */
  Collection<Gamepad> getGamepads();

  /** Returns a gamepad by index (0 = first connected). */
  Gamepad getGamepad(int index);

  void addGamepadListener(GamepadListener listener);

  void removeGamepadListener(GamepadListener listener);
}
