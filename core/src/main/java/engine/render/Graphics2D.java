package engine.render;

import engine.resources.Font;
import engine.resources.Image;
import math.Color;

/**
 * Defines the 2D rendering context and operations for a 2D rendering system.
 *
 * <p>This interface provides methods for basic 2D graphics rendering operations such as drawing
 * geometric shapes (rectangles, ovals, and lines), text rendering, color setting, transformations
 * (translate, scale, rotate), and text metrics calculations. It serves as the foundation for
 * implementing 2D rendering capabilities in a graphics pipeline.
 *
 * <p>Implementations of this interface are responsible for providing concrete rendering logic with
 * support for transformations and state management (e.g., push and pop matrix operations).
 */
public interface Graphics2D {

  /**
   * Retrieves the current width of the rendering context's viewport.
   *
   * @return The width in pixels.
   */
  int getWidth();

  /**
   * Retrieves the current height of the rendering context's viewport.
   *
   * @return The height in pixels.
   */
  int getHeight();

  /**
   * Sets the current drawing color using a math-defined {@link math.Color}.
   *
   * @param color The math-defined color to use for rendering operations.
   */
  void setColor(Color color);

  /**
   * Sets the current drawing color using RGB integer values.
   *
   * @param red The red channel value (0-255).
   * @param green The green channel value (0-255).
   * @param blue The blue channel value (0-255).
   */
  void setColor(int red, int green, int blue);

  /**
   * Sets the thickness of strokes (lines) used in subsequent drawing commands.
   *
   * @param weight The stroke weight to apply (e.g., 1.0 for standard line thickness).
   */
  void strokeWeight(float weight);

  /**
   * Returns the thickness of strokes (lines) currently used in subsequent drawing commands.
   *
   * @return The currently set stroke weight.
   */
  float getStrokeWeight();

  /**
   * Saves the current transformation matrix onto a stack for future restoration.
   *
   * <p>This allows temporary transformations without permanently altering the rendering state.
   */
  void pushMatrix();

  /**
   * Restores the last saved transformation matrix from the stack.
   *
   * <p>This undoes any temporary transformations applied since the last pushMatrix().
   */
  void popMatrix();

  /**
   * Translates the rendering context by a specified distance in 2D space.
   *
   * @param x The amount to translate along the x-axis.
   * @param y The amount to translate along the y-axis.
   */
  void translate(float x, float y);

  /**
   * Scales the rendering context by specified scaling factors along the x and y axes.
   *
   * @param sx The scaling factor along the x-axis.
   * @param sy The scaling factor along the y-axis.
   */
  void scale(float sx, float sy);

  /**
   * Rotates the rendering context by the given angle in radians.
   *
   * @param angle The angle to rotate by, in radians.
   */
  void rotate(float angle);

  /**
   * Draws an unfilled rectangle at the specified coordinates with the given dimensions.
   *
   * @param x The x-coordinate of the top-left corner of the rectangle.
   * @param y The y-coordinate of the top-left corner of the rectangle.
   * @param width The width of the rectangle.
   * @param height The height of the rectangle.
   */
  void drawRect(float x, float y, float width, float height);

  /**
   * Draws a filled rectangle at the specified coordinates with the given dimensions.
   *
   * @param x The x-coordinate of the top-left corner of the rectangle.
   * @param y The y-coordinate of the top-left corner of the rectangle.
   * @param width The width of the rectangle.
   * @param height The height of the rectangle.
   */
  void fillRect(float x, float y, float width, float height);

  /**
   * Draws the outline of a rounded rectangle with specified position, dimensions, and corner
   * radius.
   *
   * <p>The rectangle is defined by its top-left corner (x, y), its width, and its height. The
   * "radii" parameter specifies the corner radius, which determines how rounded the corners appear.
   * If the corner radius is 0, this method behaves like {@link #drawRect(float, float, float,
   * float)}.
   *
   * @param x The x-coordinate of the top-left corner of the rectangle.
   * @param y The y-coordinate of the top-left corner of the rectangle.
   * @param width The width of the rectangle.
   * @param height The height of the rectangle.
   * @param radii The corner radius for rounding the corners of the rectangle. A larger value
   *     results in more rounded corners.
   */
  void drawRoundRect(float x, float y, float width, float height, float radii);

  /**
   * Draws a filled rounded rectangle with specified position, dimensions, and corner radius.
   *
   * <p>The rectangle is defined by its top-left corner (x, y), its width, and its height. The
   * "radii" parameter specifies the corner radius, which determines how rounded the corners appear.
   * If the corner radius is 0, this method behaves like {@link #fillRect(float, float, float,
   * float)}.
   *
   * @param x The x-coordinate of the top-left corner of the rectangle.
   * @param y The y-coordinate of the top-left corner of the rectangle.
   * @param width The width of the rectangle.
   * @param height The height of the rectangle.
   * @param radii The corner radius for rounding the corners of the rectangle. A larger value
   *     results in more rounded corners.
   */
  void fillRoundRect(float x, float y, float width, float height, float radii);

  /**
   * Draws an unfilled oval at the specified coordinates with the given dimensions.
   *
   * @param x The x-coordinate of the top-left corner of the bounding box of the oval.
   * @param y The y-coordinate of the top-left corner of the bounding box of the oval.
   * @param width The width of the bounding box.
   * @param height The height of the bounding box.
   */
  void drawOval(float x, float y, float width, float height);

  /**
   * Draws a filled oval at the specified coordinates with the given dimensions.
   *
   * @param x The x-coordinate of the top-left corner of the bounding box of the oval.
   * @param y The y-coordinate of the top-left corner of the bounding box of the oval.
   * @param width The width of the bounding box.
   * @param height The height of the bounding box.
   */
  void fillOval(float x, float y, float width, float height);

  /**
   * Draws a line from (x1, y1) to (x2, y2).
   *
   * @param x1 Starting x-coordinate.
   * @param y1 Starting y-coordinate.
   * @param x2 Ending x-coordinate.
   * @param y2 Ending y-coordinate.
   */
  void drawLine(float x1, float y1, float x2, float y2);

  /**
   * Sets the size of text to render for subsequent text rendering operations.
   *
   * @param size The desired text size.
   */
  void textSize(float size);

  /**
   * Retrieves the current text size used by text rendering operations.
   *
   * @return The current text size.
   */
  float getTextSize();

  /**
   * Computes the width of the given text string at the current text size.
   *
   * @param text The text to compute the width for.
   * @return The width of the rendered text.
   */
  float textWidth(String text);

  /**
   * Retrieves the ascent of text (the portion of text above the baseline).
   *
   * @return The ascent value of the text.
   */
  float textAscent();

  /**
   * Retrieves the descent of text (the portion of text below the baseline).
   *
   * @return The descent value of the text.
   */
  float textDescent();

  /**
   * Renders text at the given screen coordinates.
   *
   * <p>The position refers to the baseline of the text, as defined by the current font metrics.
   *
   * @param text The text to render.
   * @param x The x-coordinate in screen space where the text starts.
   * @param y The y-coordinate (baseline) in screen space.
   */
  void text(String text, float x, float y);

  /**
   * Renders text horizontally centered on the screen at the given vertical position.
   *
   * <p>The text will be centered along the X-axis based on the current screen width and font
   * metrics. The Y position is not centered and must be provided explicitly.
   *
   * @param text The text to render.
   * @param yOffset The y-coordinate (baseline) in screen space.
   */
  void textCentered(String text, float yOffset);

  /**
   * Renders text centered both horizontally and vertically on the screen.
   *
   * <p>The text is centered along both axes using the current screen dimensions and font metrics.
   * This is useful for titles, splash screens, and modal UI elements.
   *
   * @param text The text to render.
   */
  void textCenteredBoth(String text);

  /**
   * Sets the current font used for text rendering operations.
   *
   * <p>This method changes the font that will be applied to subsequent text rendering commands. The
   * provided {@link Font} object determines the style, weight, and size of the text that will be
   * drawn on the rendering surface.
   *
   * <p>Passing a {@code null} value will reset the font to the default system font.
   *
   * @param font The {@link Font} to set. If {@code null}, the default font will be used.
   */
  void setFont(Font font);

  /**
   * Clears the rendering context with the specified color.
   *
   * <p>This method fills the entire viewport with the provided color, effectively resetting the
   * drawing surface to a blank state. Any previously drawn content is overwritten.
   *
   * @param color The {@link math.Color} to use for clearing the viewport. The color is applied
   *     uniformly across the entire rendering surface.
   */
  void clear(math.Color color);

  /**
   * Draws an image at the specified coordinates (x, y).
   *
   * <p>This method renders the provided image at the given position (x, y) on the screen, using the
   * image's natural dimensions without scaling.
   *
   * @param image The {@link Image} object to render.
   * @param x The x-coordinate where the image's top-left corner will be placed.
   * @param y The y-coordinate where the image's top-left corner will be placed.
   */
  void drawImage(Image image, float x, float y);

  /**
   * Draws an image at the specified coordinates (x, y) and scales it to the specified width and
   * height.
   *
   * <p>This method renders the provided image at the given position (x, y) on the screen, scaling
   * the image to fit the specified width and height. The aspect ratio of the image may be distorted
   * if the aspect ratio of the width and height differs from the image's natural dimensions.
   *
   * @param image The {@link Image} object to render.
   * @param x The x-coordinate where the image's top-left corner will be placed.
   * @param y The y-coordinate where the image's top-left corner will be placed.
   * @param width The width to scale the image to.
   * @param height The height to scale the image to.
   */
  void drawImage(Image image, float x, float y, float width, float height);
}
