package engine.runtime.input;

import engine.runtime.input.action.InputActions;

public interface Input extends KeyInput, MouseInput, GamepadInput {

  /**
   * Updates all input devices and advances the input state by one frame. Must be called exactly
   * once per frame.
   */
  void update();

  /**
   * Provides access to high-level, semantic input actions. Actions are resolved from the current
   * input state.
   */
  InputActions getActions();
}
