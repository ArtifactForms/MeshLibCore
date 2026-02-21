package workspace.ui.border;

import engine.rendering.Graphics;

/**
 * A border implementation that does not render any visual element but provides
 * space defined by its insets.
 */
public class EmptyBorder implements Border {

	private final Insets insets;

	/**
	 * Creates an EmptyBorder with the specified insets.
	 * 
	 * @param top    The inset size at the top.
	 * @param left   The inset size at the left.
	 * @param bottom The inset size at the bottom.
	 * @param right  The inset size at the right.
	 * @throws IllegalArgumentException if any of the inset values are negative.
	 */
	public EmptyBorder(int top, int left, int bottom, int right) {
		if (top < 0 || left < 0 || bottom < 0 || right < 0) {
			throw new IllegalArgumentException("Insets cannot be negative.");
		}
		insets = new Insets(top, left, bottom, right);
	}

	/**
	 * Does not render anything, as this border is visually empty.
	 * 
	 * @param g2     The Graphics context for rendering.
	 * @param x      The x-coordinate of the top-left corner.
	 * @param y      The y-coordinate of the top-left corner.
	 * @param width  The width of the area to render the border around.
	 * @param height The height of the area to render the border around.
	 */
	@Override
	public void renderBorder(Graphics g, int x, int y, int width, int height) {
		// No rendering logic, as this border is empty.
	}

	/**
	 * Returns the insets of the border.
	 * 
	 * @return A new Insets object representing the border's insets.
	 */
	@Override
	public Insets getInsets() {
		return new Insets(insets);
	}
	
}