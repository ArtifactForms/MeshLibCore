package workspace.ui.border;

import engine.render.Graphics;
import math.Color;

/**
 * A custom border implementation that mimics pixel art-style UI borders with
 * missing pixels at each corner. The "size" parameter defines the scale of
 * these gaps, creating a distinctive visual effect similar to pixel art
 * designs.
 * <p>
 * This border has gaps (or empty space) at each corner, allowing it to fit
 * stylistically into a pixel art-themed user interface.
 * </p>
 */
public class CornerGapBorder implements Border {

	private int x;

	private int y;

	private int width;

	private int height;

	private int size;

	private Color color;

	private Graphics g;

	/**
	 * Creates a new instance of a CornerGapBorder.
	 * 
	 * @param size  Defines the scale of the missing gaps at each corner (in
	 *              pixels). Larger values increase the size of these gaps,
	 *              influencing how much of the border is visually omitted at the
	 *              corners.
	 * @param color The color used for the visible parts of the border.
	 * @throws NullPointerException if the provided color is null.
	 */
	public CornerGapBorder(int size, Color color) {
		this.size = size;
		this.color = color;
	}

	/**
	 * Renders the border around a specified rectangle area with gaps at each
	 * corner, giving it the appearance of a pixel art-style border.
	 * 
	 * @param g      The graphics context used to draw the border.
	 * @param x      The x-coordinate of the top-left corner of the area to draw.
	 * @param y      The y-coordinate of the top-left corner of the area to draw.
	 * @param width  The width of the rectangular area to render the border
	 *               around.
	 * @param height The height of the rectangular area to render the border
	 *               around.
	 */
	@Override
	public void renderBorder(Graphics g, int x, int y, int width, int height) {
		setGraphicsContext(g);
		setBorderFrame(x, y, width, height);
		setColor();
		renderRectangles();
	}

	/**
	 * Draws the visual rectangles forming the North, West, South, and East sides
	 * of the border, each leaving a gap at the corners to achieve the pixel art
	 * aesthetic.
	 */
	private void renderRectangles() {
		renderNorthRectangle();
		renderWestRectangle();
		renderSouthRectangle();
		renderEastRectangle();
	}

	/** Renders the North side of the border with a missing corner gap. */
	private void renderNorthRectangle() {
		renderRectangle(x + size, y, getRectangleWidth(), size);
	}

	/** Renders the West side of the border with a missing corner gap. */
	private void renderWestRectangle() {
		renderRectangle(x, y + size, size, getRectangleHeight());
	}

	/** Renders the South side of the border with a missing corner gap. */
	private void renderSouthRectangle() {
		renderRectangle(x + size, y + height - size, getRectangleWidth(), size);
	}

	/** Renders the East side of the border with a missing corner gap. */
	private void renderEastRectangle() {
		renderRectangle(x + width - size, y + size, size, getRectangleHeight());
	}

	/**
	 * Renders a rectangle using the provided graphics context.
	 * 
	 * @param x      The x-coordinate for the top-left corner of the rectangle.
	 * @param y      The y-coordinate for the top-left corner of the rectangle.
	 * @param width  The width of the rectangle to draw.
	 * @param height The height of the rectangle to draw.
	 */
	private void renderRectangle(int x, int y, int width, int height) {
		g.fillRect(x, y, width, height);
	}

	/**
	 * Calculates the visible rectangle's width by excluding the corner gaps.
	 * 
	 * @return The calculated width of the visible rectangle.
	 */
	private int getRectangleWidth() {
		return width - size - size;
	}

	/**
	 * Calculates the visible rectangle's height by excluding the corner gaps.
	 * 
	 * @return The calculated height of the visible rectangle.
	 */
	private int getRectangleHeight() {
		return height - size - size;
	}

	/**
	 * Sets the color for rendering using the graphics context.
	 */
	private void setColor() {
		g.setColor(color);
	}

	/**
	 * Configures the rectangle's frame to know the drawing boundaries for
	 * rendering.
	 * 
	 * @param x      The x-coordinate of the top-left corner.
	 * @param y      The y-coordinate of the top-left corner.
	 * @param width  The width of the area to draw.
	 * @param height The height of the area to draw.
	 */
	private void setBorderFrame(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Sets the graphics context to the current instance for use during rendering.
	 * 
	 * @param g The provided graphics context.
	 */
	private void setGraphicsContext(Graphics g) {
		this.g = g;
	}

	/**
	 * Retrieves the Insets required for this border. Insets define the amount of
	 * space the border occupies on all four sides.
	 * 
	 * @return Insets representing the "size" of the gaps in all directions.
	 */
	@Override
	public Insets getInsets() {
		return new Insets(size, size, size, size);
	}

}