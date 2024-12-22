package engine.input;

public interface Input extends KeyInput, MouseInput {

  void update(); // Calls both `updateKeyState` and `updateMouseState`
}
