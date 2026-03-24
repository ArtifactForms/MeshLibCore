package client.rendering;

import engine.rendering.Graphics;
import math.Color;

/**
 * Renders the wireframe highlight around the currently targeted block.
 *
 * <p>This is the visual outline shown when the player points at a block (similar to Minecraft's
 * block selection box).
 *
 * <p>The renderer draws a slightly enlarged wireframe cube around the block to avoid z-fighting
 * with the block's actual mesh. This is achieved using a small {@link #OFFSET}.
 *
 * <p>Important coordinate note: The engine uses an inverted Y-axis for rendering, therefore the
 * block Y coordinate is negated before rendering.
 *
 * <p>This class is stateless and designed as a simple static renderer.
 */
public class BlockHighlightRenderer {

  /** Color of the highlight lines (semi-transparent white). */
  private static final Color color = new Color(1, 1, 1, 0.4f);

  /**
   * Small offset added to the cube bounds to prevent z-fighting with the block surface. A value
   * slightly larger than the block half-size (0.5) ensures the wireframe is rendered just outside
   * the block.
   */
  private static final float OFFSET = 0.504f;

  private BlockHighlightRenderer() {
    // No instances
  }

  /**
   * Renders the highlight box for a block at the given world coordinates.
   *
   * @param g the graphics context
   * @param x world block X coordinate
   * @param y world block Y coordinate
   * @param z world block Z coordinate
   */
  public static void render(Graphics g) {

    float minX = -OFFSET;
    float maxX = OFFSET;

    float minY = -OFFSET;
    float maxY = OFFSET;

    float minZ = -OFFSET;
    float maxZ = OFFSET;

    g.setColor(color);
    g.strokeWeight(3);

    // bottom
    g.drawLine(minX, minY, minZ, maxX, minY, minZ);
    g.drawLine(maxX, minY, minZ, maxX, minY, maxZ);
    g.drawLine(maxX, minY, maxZ, minX, minY, maxZ);
    g.drawLine(minX, minY, maxZ, minX, minY, minZ);

    // top
    g.drawLine(minX, maxY, minZ, maxX, maxY, minZ);
    g.drawLine(maxX, maxY, minZ, maxX, maxY, maxZ);
    g.drawLine(maxX, maxY, maxZ, minX, maxY, maxZ);
    g.drawLine(minX, maxY, maxZ, minX, maxY, minZ);

    // vertical
    g.drawLine(minX, minY, minZ, minX, maxY, minZ);
    g.drawLine(maxX, minY, minZ, maxX, maxY, minZ);
    g.drawLine(maxX, minY, maxZ, maxX, maxY, maxZ);
    g.drawLine(minX, minY, maxZ, minX, maxY, maxZ);
  }
}
