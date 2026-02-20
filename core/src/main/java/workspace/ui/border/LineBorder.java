package workspace.ui.border;

import math.Color;
import workspace.ui.Graphics;

/**
 * A border implementation that draws a solid line around a UI element. The
 * thickness and color of the border can be customized.
 */
public class LineBorder implements Border {

	private final int size;

	private final Color color;

	/**
	 * Creates a LineBorder with the specified thickness and color.
	 * 
	 * @param size  The thickness of the border. Must be non-negative.
	 * @param color The color of the border. Cannot be null.
	 * @throws IllegalArgumentException if size is negative.
	 * @throws NullPointerException     if color is null.
	 */
	public LineBorder(int size, Color color) {
		if (size < 0) {
			throw new IllegalArgumentException("Border size must be non-negative.");
		}
		if (color == null) {
			throw new NullPointerException("Color cannot be null.");
		}
		this.size = size;
		this.color = color;
	}

	/**
	 * Renders the border by drawing nested rectangles to achieve the desired
	 * thickness.
	 * 
	 * @param g      The Graphics context for rendering.
	 * @param x      The x-coordinate of the top-left corner.
	 * @param y      The y-coordinate of the top-left corner.
	 * @param width  The width of the area to render the border around.
	 * @param height The height of the area to render the border around.
	 */
	@Override
	public void renderBorder(Graphics g, int x, int y, int width, int height) {
		g.setColor(color);
		g.fillRect(x, y, width, size); // Top
		g.fillRect(x, y + size, size, height - size * 2); // Left
		g.fillRect(x + width - size, y + size, size, height - size * 2); // Right
		g.fillRect(x, y + height - size, width, size); // Bottom
	}

	/**
	 * Returns the insets required for this border, which are equal to the border
	 * thickness on all sides.
	 * 
	 * @return An Insets object with the border thickness.
	 */
	@Override
	public Insets getInsets() {
		return new Insets(size, size, size, size);
	}

	/**
	 * Gets the size (thickness) of the border.
	 * 
	 * @return The border thickness.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the color of the border.
	 * 
	 * @return The border color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Returns a string representation of the LineBorder for debugging.
	 * 
	 * @return A string with the border's properties.
	 */
	@Override
	public String toString() {
		return "LineBorder[size=" + size + ", color=" + color + "]";
	}

}