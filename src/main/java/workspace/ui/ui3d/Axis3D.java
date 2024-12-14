package workspace.ui.ui3d;

import workspace.laf.UiConstants;
import workspace.laf.UiValues;
import workspace.ui.Graphics;

/**
 * Represents a 3D axis visualization that can be rendered with customizable
 * visibility for each axis. Each axis is drawn with its corresponding color, as
 * defined by the UI constants, and can be toggled on or off individually.
 * <p>
 * The axis lines are rendered in the X, Y, and Z directions, with their lengths
 * determined by the specified size parameter.
 * </p>
 */
public class Axis3D {

	/**
	 * Visibility flag for the X-axis.
	 */
	private boolean xAxisVisible;

	/**
	 * Visibility flag for the Y-axis.
	 */
	private boolean yAxisVisible;

	/**
	 * Visibility flag for the Z-axis.
	 */
	private boolean zAxisVisible;

	/**
	 * The length of each axis, extending equally in both positive and negative
	 * directions.
	 */
	private float size;

	/**
	 * Constructs an {@code Axis3D} with the specified size. All axes are visible
	 * by default.
	 *
	 * @param size the length of each axis; must be positive
	 * @throws IllegalArgumentException if the size is not positive
	 */
	public Axis3D(float size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be positive.");
		}
		this.size = size;
		this.xAxisVisible = true;
		this.yAxisVisible = true;
		this.zAxisVisible = true;
	}

	/**
	 * Renders the 3D axis using the provided {@link Graphics} context. The axis
	 * lines are drawn based on their visibility flags and scaled according to the
	 * specified stroke weight factor.
	 *
	 * @param g     the {@link Graphics} context to draw on
	 * @param scale the scale factor for adjusting the stroke weight; must be
	 *              positive
	 */
	public void render(Graphics g, float scale) {
		if (scale <= 0) {
			throw new IllegalArgumentException("Scale must be positive.");
		}
		g.pushMatrix();
		g.strokeWeight(1.5f / scale);
		renderXAxis(g);
		renderYAxis(g);
		renderZAxis(g);
		g.popMatrix();
	}

	/**
	 * Renders the X-axis if it is visible. The X-axis is drawn in the positive
	 * and negative X directions with the color defined by
	 * {@link UiConstants#KEY_AXIS_X_COLOR}.
	 *
	 * @param g the {@link Graphics} context to draw on
	 */
	private void renderXAxis(Graphics g) {
		if (!xAxisVisible) {
			return;
		}
		g.setColor(UiValues.getColor(UiConstants.KEY_AXIS_X_COLOR));
		g.drawLine(size, 0, 0, 0, 0, 0);
		g.drawLine(-size, 0, 0, 0, 0, 0);
	}

	/**
	 * Renders the Y-axis if it is visible. The Y-axis is drawn in the positive
	 * and negative Y directions with the color defined by
	 * {@link UiConstants#KEY_AXIS_Y_COLOR}.
	 *
	 * @param g the {@link Graphics} context to draw on
	 */
	private void renderYAxis(Graphics g) {
		if (!yAxisVisible) {
			return;
		}
		g.setColor(UiValues.getColor(UiConstants.KEY_AXIS_Y_COLOR));
		g.drawLine(0, size, 0, 0, 0, 0);
		g.drawLine(0, -size, 0, 0, 0, 0);
	}

	/**
	 * Renders the Z-axis if it is visible. The Z-axis is drawn in the positive
	 * and negative Z directions with the color defined by
	 * {@link UiConstants#KEY_AXIS_Z_COLOR}.
	 *
	 * @param g the {@link Graphics} context to draw on
	 */
	private void renderZAxis(Graphics g) {
		if (!zAxisVisible) {
			return;
		}
		g.setColor(UiValues.getColor(UiConstants.KEY_AXIS_Z_COLOR));
		g.drawLine(0, 0, size, 0, 0, 0);
		g.drawLine(0, 0, -size, 0, 0, 0);
	}

	/**
	 * Returns whether the X-axis is visible.
	 *
	 * @return {@code true} if the X-axis is visible; {@code false} otherwise
	 */
	public boolean isXAxisVisible() {
		return xAxisVisible;
	}

	/**
	 * Sets the visibility of the X-axis.
	 *
	 * @param xAxisVisible {@code true} to make the X-axis visible; {@code false}
	 *                     to hide it
	 */
	public void setXAxisVisible(boolean xAxisVisible) {
		this.xAxisVisible = xAxisVisible;
	}

	/**
	 * Returns whether the Y-axis is visible.
	 *
	 * @return {@code true} if the Y-axis is visible; {@code false} otherwise
	 */
	public boolean isYAxisVisible() {
		return yAxisVisible;
	}

	/**
	 * Sets the visibility of the Y-axis.
	 *
	 * @param yAxisVisible {@code true} to make the Y-axis visible; {@code false}
	 *                     to hide it
	 */
	public void setYAxisVisible(boolean yAxisVisible) {
		this.yAxisVisible = yAxisVisible;
	}

	/**
	 * Returns whether the Z-axis is visible.
	 *
	 * @return {@code true} if the Z-axis is visible; {@code false} otherwise
	 */
	public boolean isZAxisVisible() {
		return zAxisVisible;
	}

	/**
	 * Sets the visibility of the Z-axis.
	 *
	 * @param zAxisVisible {@code true} to make the Z-axis visible; {@code false}
	 *                     to hide it
	 */
	public void setZAxisVisible(boolean zAxisVisible) {
		this.zAxisVisible = zAxisVisible;
	}

	/**
	 * Sets the length of each axis, extending equally in both positive and
	 * negative directions.
	 *
	 * @param newSize the new size of the axes; must be positive
	 * @throws IllegalArgumentException if the new size is not positive
	 */
	public void setSize(float newSize) {
		if (newSize <= 0) {
			throw new IllegalArgumentException("Size must be positive.");
		}
		this.size = newSize;
	}

}