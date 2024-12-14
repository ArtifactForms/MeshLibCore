package workspace.ui.border;

import workspace.ui.Graphics;

/**
 * A border implementation that combines two borders: an outer border and an
 * inner border. The outer border is rendered first, followed by the inner
 * border, which is adjusted to fit within the insets of the outer border.
 */
public class CompoundBorder implements Border {

	private final Border outerBorder;

	private final Border innerBorder;

	/**
	 * Creates a CompoundBorder with the specified outer and inner borders.
	 *
	 * @param outerBorder The outer border. Cannot be null.
	 * @param innerBorder The inner border. Cannot be null.
	 * @throws NullPointerException if either outerBorder or innerBorder is null.
	 */
	public CompoundBorder(Border outerBorder, Border innerBorder) {
		if (outerBorder == null) {
			throw new NullPointerException("Outer border cannot be null.");
		}
		if (innerBorder == null) {
			throw new NullPointerException("Inner border cannot be null.");
		}
		this.outerBorder = outerBorder;
		this.innerBorder = innerBorder;
	}

	/**
	 * Returns the combined insets of the outer and inner borders.
	 *
	 * @return An Insets object representing the total insets of the compound
	 *         border.
	 */
	@Override
	public Insets getInsets() {
		Insets insets = new Insets(outerBorder.getInsets());
		insets.add(innerBorder.getInsets());
		return insets;
	}

	/**
	 * Renders the outer border and adjusts the coordinates and dimensions to
	 * render the inner border within the remaining area.
	 *
	 * @param g      The Graphics context for rendering.
	 * @param x      The x-coordinate of the top-left corner.
	 * @param y      The y-coordinate of the top-left corner.
	 * @param width  The width of the area to render the border around.
	 * @param height The height of the area to render the border around.
	 */
	@Override
	public void renderBorder(Graphics g, int x, int y, int width, int height) {
		renderOuterBorder(g, x, y, width, height);
		renderInnerBorder(g, x, y, width, height);
	}

	/**
	 * Renders the outer border using the provided Graphics context.
	 *
	 * @param g      The Graphics context for rendering.
	 * @param x      The x-coordinate of the top-left corner.
	 * @param y      The y-coordinate of the top-left corner.
	 * @param width  The width of the area to render the border around.
	 * @param height The height of the area to render the border around.
	 */
	private void renderOuterBorder(Graphics g, int x, int y, int width,
	    int height) {
		outerBorder.renderBorder(g, x, y, width, height);
	}

	/**
	 * Renders the inner border by adjusting for the insets of the outer border.
	 *
	 * @param g      The Graphics context for rendering.
	 * @param x      The x-coordinate of the top-left corner.
	 * @param y      The y-coordinate of the top-left corner.
	 * @param width  The width of the area to render the border around.
	 * @param height The height of the area to render the border around.
	 */
	private void renderInnerBorder(Graphics g, int x, int y, int width,
	    int height) {
		Insets outerInsets = outerBorder.getInsets();
		x += outerInsets.getLeft();
		y += outerInsets.getTop();
		width -= outerInsets.getHorizontalInsets();
		height -= outerInsets.getVerticalInsets();
		innerBorder.renderBorder(g, x, y, width, height);
	}

}