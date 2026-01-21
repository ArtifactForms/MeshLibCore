package engine.application;

public final class Viewport {

  private int x;
  private int y;
  private int width;
  private int height;

  public Viewport(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public float getAspectRatio() {
    return (float) width / (float) height;
  }

  public boolean contains(int px, int py) {
    return px >= x && py >= y && px < x + width && py < y + height;
  }
}
