package engine.runtime.input;

public interface GamepadListener {

  void onButtonPressed(Gamepad gamepad, GamepadButton button);

  void onButtonReleased(Gamepad gamepad, GamepadButton button);
}