package engine.input;

public interface Input extends KeyInput, MouseInput, GamepadInput{

  void update(); // Calls both `updateKeyState` and `updateMouseState`
}
