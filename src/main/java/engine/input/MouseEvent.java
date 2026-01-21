package engine.input;

public class MouseEvent {

  private final int x;
  private final int y;
  private final int button;

  public MouseEvent(int x, int y, int button) {
    this.x = x;
    this.y = y;
    this.button = button;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getButton() {
    return button;
  }
}
