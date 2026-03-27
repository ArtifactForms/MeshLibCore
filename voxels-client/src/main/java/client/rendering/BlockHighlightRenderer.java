package client.rendering;

import common.game.block.BlockShape;
import engine.rendering.Graphics;
import math.Color;

/**
 * Renders a wireframe highlight for the currently targeted block.
 *
 * <p>This renderer is responsible for drawing the selection outline that appears when the player is
 * pointing at a block.
 *
 * <h3>Supported Shapes</h3>
 *
 * <ul>
 *   <li>{@link BlockShape#CUBE} - Standard block outline (wireframe cube)
 *   <li>{@link BlockShape#CROSS} - Cross-shaped blocks (e.g. plants), rendered as two intersecting
 *       vertical quads
 * </ul>
 *
 * <h3>Z-Fighting Prevention</h3>
 *
 * <p>To avoid z-fighting with the actual block geometry, the highlight is rendered slightly larger
 * than the block itself using a small offset.
 *
 * <h3>Coordinate System</h3>
 *
 * <p>The engine uses an inverted Y-axis for rendering. This class assumes that transformations
 * (including Y inversion and world positioning) are already applied externally.
 *
 * <p>This class is stateless and only provides static rendering methods.
 */
public class BlockHighlightRenderer {

  /** Color of the highlight lines (semi-transparent black). */
  private static final Color COLOR = new Color(0, 0, 0, 0.6f);

  /**
   * Small offset added to the block bounds to prevent z-fighting.
   *
   * <p>Slightly larger than half block size (0.5).
   */
  private static final float OFFSET = 0.5001f;

  private BlockHighlightRenderer() {
    // Utility class
  }

  /**
   * Renders a highlight for the given block shape.
   *
   * @param g the graphics context
   * @param shape the block shape to render
   */
  public static void render(Graphics g, BlockShape shape) {
    if (shape == BlockShape.CUBE) {
      renderCube(g);
    } else if (shape == BlockShape.CROSS) {
      renderCross(g);
    }
  }

  /**
   * Renders a cross-shaped highlight.
   *
   * <p>This is used for blocks like plants or grass. The shape consists of two vertical quads that
   * intersect diagonally:
   *
   * <pre>
   * Top View:
   *
   *   \  /
   *    \/
   *    /\
   *   /  \
   * </pre>
   *
   * <p>Each quad is rendered as a wireframe rectangle.
   */
  private static void renderCross(Graphics g) {

    float minY = -OFFSET;
    float maxY = OFFSET;

    float size = OFFSET;

    g.setColor(COLOR);
    g.strokeWeight(2);

    // --- First quad (\ direction) ---
    g.drawLine(-size, minY, -size, size, minY, size);
    g.drawLine(size, minY, size, size, maxY, size);
    g.drawLine(size, maxY, size, -size, maxY, -size);
    g.drawLine(-size, maxY, -size, -size, minY, -size);

    // --- Second quad (/ direction) ---
    g.drawLine(-size, minY, size, size, minY, -size);
    g.drawLine(size, minY, -size, size, maxY, -size);
    g.drawLine(size, maxY, -size, -size, maxY, size);
    g.drawLine(-size, maxY, size, -size, minY, size);
  }

  /**
   * Renders a standard cube-shaped highlight.
   *
   * <p>The cube is slightly enlarged using {@link #OFFSET} to prevent z-fighting.
   */
  private static void renderCube(Graphics g) {

    float minX = -OFFSET;
    float maxX = OFFSET;

    float minY = -OFFSET;
    float maxY = OFFSET;

    float minZ = -OFFSET;
    float maxZ = OFFSET;

    g.setColor(COLOR);
    g.strokeWeight(2);

    // --- Bottom face ---
    g.drawLine(minX, minY, minZ, maxX, minY, minZ);
    g.drawLine(maxX, minY, minZ, maxX, minY, maxZ);
    g.drawLine(maxX, minY, maxZ, minX, minY, maxZ);
    g.drawLine(minX, minY, maxZ, minX, minY, minZ);

    // --- Top face ---
    g.drawLine(minX, maxY, minZ, maxX, maxY, minZ);
    g.drawLine(maxX, maxY, minZ, maxX, maxY, maxZ);
    g.drawLine(maxX, maxY, maxZ, minX, maxY, maxZ);
    g.drawLine(minX, maxY, maxZ, minX, maxY, minZ);

    // --- Vertical edges ---
    g.drawLine(minX, minY, minZ, minX, maxY, minZ);
    g.drawLine(maxX, minY, minZ, maxX, maxY, minZ);
    g.drawLine(maxX, minY, maxZ, maxX, maxY, maxZ);
    g.drawLine(minX, minY, maxZ, minX, maxY, maxZ);
  }
}
