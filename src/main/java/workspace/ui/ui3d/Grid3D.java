package workspace.ui.ui3d;

import math.Mathf;
import workspace.laf.UiConstants;
import workspace.laf.UiValues;
import workspace.ui.Graphics;

/**
 * Represents a 3D grid visualization that can be rendered in a 3D UI space.
 * <p>
 * This class provides the logic for creating and rendering a 3D spatial grid
 * with a given number of rows, columns, and cell size. It is designed to work
 * with a custom 3D graphics rendering context, allowing visualization of UI
 * layouts or spatial relationships.
 * </p>
 */
public class Grid3D {

	/** The number of rows in the 3D grid. */
	private int rows;

	/** The number of columns in the 3D grid. */
	private int cols;

	/** The size of each individual cell in the 3D grid. */
	private float size;

	/**
	 * Indicates whether the 3D grid should currently be visible when rendered.
	 */
	private boolean visible;

	/**
	 * Constructs a new {@code Grid3D} instance with the specified number of rows,
	 * columns, and cell size.
	 *
	 * @param rows The number of rows to create in the 3D grid.
	 * @param cols The number of columns to create in the 3D grid.
	 * @param size The size (width/height) of each individual cell in the 3D grid.
	 */
	public Grid3D(int rows, int cols, float size) {
		this.rows = rows;
		this.cols = cols;
		this.size = size;
		this.visible = true;
	}

	/**
	 * Renders the 3D grid visualization using the provided graphics context.
	 * <p>
	 * This method only performs rendering if the grid is currently visible. The
	 * rendering logic draws a series of rectangles in a 3D space to represent the
	 * cells of the grid. It applies necessary transformations for proper
	 * alignment and orientation in a 3D context.
	 * </p>
	 *
	 * @param g The graphics context used for rendering the 3D grid.
	 */
	public void render(Graphics g) {
		if (!isVisible()) {
			return;
		}
		g.setColor(UiValues.getColor(UiConstants.KEY_GRID_COLOR));
		g.pushMatrix();
		g.rotateX(-Mathf.HALF_PI);
		g.translate(-cols / 2.0f, -rows / 2.0f);
		renderGridLines(g);
		g.popMatrix();
	}

	/**
	 * Renders the grid lines.
	 * 
	 * @param g The graphics context used for rendering.
	 */
	private void renderGridLines(Graphics g) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				g.drawRect(j * size, i * size, size, size);
			}
		}
	}

	/**
	 * Checks whether the 3D grid visualization is currently visible.
	 *
	 * @return {@code true} if the 3D grid is visible; {@code false} otherwise.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the visibility state of the 3D grid visualization.
	 * <p>
	 * When set to {@code true}, the grid will be rendered during the next
	 * invocation of the {@code render} method. If set to {@code false}, rendering
	 * will be skipped.
	 * </p>
	 *
	 * @param visible The new visibility state of the 3D grid.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}