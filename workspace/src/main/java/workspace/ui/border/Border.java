package workspace.ui.border;

import engine.rendering.Graphics;

/**
 * Represents a customizable border for UI elements.
 * 
 * A `Border` defines how to visually render a boundary around a UI element and
 * specifies the spacing (insets) it requires.
 */
public interface Border {

	/**
	 * Renders the border using the provided Graphics context.
	 *
	 * @param g      The Graphics context for rendering.
	 * @param x      The x-coordinate of the border's position.
	 * @param y      The y-coordinate of the border's position.
	 * @param width  The width of the area to render the border around.
	 * @param height The height of the area to render the border around.
	 */
	void renderBorder(Graphics g, int x, int y, int width, int height);

	/**
	 * Returns the insets defined by the border.
	 * 
	 * Insets specify the spacing required between the border and the content of
	 * the UI element. These insets are typically used in layout calculations.
	 * 
	 * @return An {@link Insets} object representing the top, left, bottom, and
	 *         right insets of the border.
	 */
	Insets getInsets();

}