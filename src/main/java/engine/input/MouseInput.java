package engine.input;

public interface MouseInput {

  float getScreenWidth();

  float getScreenHeight();

  boolean isMousePressed(int button);

  boolean isMouseReleased(int button);

  float getMouseX();

  float getMouseY();

  float getLastMouseX();

  float getLastMouseY();

  float getMouseWheelDelta();

  float getMouseDeltaX();

  float getMouseDeltaY();

  void updateMouseState(); // To track mouse-specific states
}
