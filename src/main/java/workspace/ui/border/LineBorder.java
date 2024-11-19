package workspace.ui.border;

import workspace.ui.Color;
import workspace.ui.Graphics;

public class LineBorder extends AbstractBorder {

	/**
	 * The width of this border in pixels.
	 */
	int width;

	/**
	 * The color of this border.
	 */
	Color color;

	/**
	 * Constructs a new instance of this border with the specified color and a width
	 * of 1 pixel.
	 * 
	 * @param color the color of this border
	 */
	public LineBorder(Color color) {
		this(color, 1);
	}

	/**
	 * Constructs a new instance of this border with the specified color and width.
	 * 
	 * @param width the width of this border in pixels
	 * @param color the color of this border
	 */
	public LineBorder(Color color, int width) {
		super();
		this.color = color;
		this.width = width;
	}

	/**
	 * Returns the width of this border in pixels.
	 * 
	 * @return the width of this border
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width of this border to the specified new value.
	 * 
	 * @param width the new width for this border
	 */
	public void setWidth(int width) {
		// FIXME Providing a setter may not be that good, cause the component using
		// this border has to change it's layout (insets)
		this.width = width;
	}

	/**
	 * Returns the color of this border.
	 * 
	 * @return the color of this border
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color of this border to the specified new value.
	 * 
	 * @param color the new color for this border
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Insets getInsets() {
		return new Insets(width);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawBorder(Graphics g, int x, int y, int width, int height) {
		g.strokeWeight(1);
		g.setColor(color);
		for (int i = 0; i < this.width; i++) {
			g.drawRect(x + i, y + i, width - i - i - 1, height - i - i - 1);
		}
	}

}
