package engine.application;

/**
 * Represents a rectangular viewport region within a larger render surface or window.
 *
 * <p>A {@code Viewport} defines an axis-aligned rectangle using integer coordinates and dimensions.
 * It is commonly used for:
 *
 * <ul>
 *   <li>Defining render areas (e.g. split-screen, UI regions)
 *   <li>Mapping input coordinates to a specific screen region
 *   <li>Camera or rendering sub-views
 * </ul>
 *
 * <p>The coordinate system assumes the origin ({@code x}, {@code y}) is located at the top-left
 * corner of the viewport.
 */
public final class Viewport {

  private int x;
  private int y;
  private int width;
  private int height;

  /**
   * Creates a new viewport with the given position and size.
   *
   * @param x the x-coordinate of the viewport's top-left corner
   * @param y the y-coordinate of the viewport's top-left corner
   * @param width the width of the viewport in pixels
   * @param height the height of the viewport in pixels
   */
  public Viewport(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /** @return the x-coordinate of the viewport's top-left corner */
  public int getX() {
    return x;
  }

  /** @return the y-coordinate of the viewport's top-left corner */
  public int getY() {
    return y;
  }

  /** @return the width of the viewport in pixels */
  public int getWidth() {
    return width;
  }

  /** @return the height of the viewport in pixels */
  public int getHeight() {
    return height;
  }

  /**
   * Returns the aspect ratio of the viewport.
   *
   * <p>The aspect ratio is defined as {@code width / height}.
   *
   * @return the aspect ratio as a floating-point value
   */
  public float getAspectRatio() {
    return (float) width / (float) height;
  }

  /**
   * Checks whether the given point lies within the viewport.
   *
   * <p>The left and top edges are inclusive, while the right and bottom edges are exclusive.
   *
   * @param px the x-coordinate of the point to test
   * @param py the y-coordinate of the point to test
   * @return {@code true} if the point is inside the viewport, {@code false} otherwise
   */
  public boolean contains(int px, int py) {
    return px >= x && py >= y && px < x + width && py < y + height;
  }
}
