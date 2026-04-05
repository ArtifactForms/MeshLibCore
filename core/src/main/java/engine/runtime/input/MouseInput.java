package engine.runtime.input;

public interface MouseInput {

  static final int LEFT = 0;

  static final int RIGHT = 1;

  static final int CENTER = 2;

  float getScreenWidth();

  float getScreenHeight();

  boolean isMouseDown(int button);

  boolean isMousePressed(int button);

  boolean isMouseReleased(int button);

  float getMouseX();

  float getMouseY();

  float getLastMouseX();

  float getLastMouseY();

  float getMouseWheelDelta();

  float getMouseDeltaX();

  float getMouseDeltaY();

  void updateMouseState();

  void addMouseListener(MouseListener listener);

  void removeMouseListener(MouseListener listener);

  void setMouseMode(MouseMode mode);

  MouseMode getMouseMode();
}
