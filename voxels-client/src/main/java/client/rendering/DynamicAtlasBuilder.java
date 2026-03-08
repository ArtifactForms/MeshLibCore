package client.rendering;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import common.world.BlockType;

/**
 * Dynamically constructs a texture atlas at runtime by combining individual block textures.
 * Supports multiple faces per block (Top, Bottom, Sides) and an optional emission mode for
 * glow-in-the-dark effects.
 */
public class DynamicAtlasBuilder {

  private final int tileSize;
  private final String resourcePath = "/assets/textures/blocks/";

  public DynamicAtlasBuilder(int tileSize) {
    this.tileSize = tileSize;
  }

  /**
   * Builds the texture atlas image. Each block type occupies one row. Columns are organized as:
   * 0:Top, 1:Bottom, 2-5:Sides. * @param emissionMode If true, loads emission maps (_e.png) instead
   * of diffuse textures.
   *
   * @return A completed BufferedImage containing all registered block textures.
   */
  public BufferedImage build(boolean emissionMode) {
    int rows = BlockType.values().length;
    int columns = 6;

    BufferedImage atlas =
        new BufferedImage(columns * tileSize, rows * tileSize, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = atlas.createGraphics();

    for (BlockType type : BlockType.values()) {
      if (type == BlockType.AIR) continue;

      int row = type.getId();
      for (int col = 0; col < columns; col++) {
        BufferedImage texture = loadTextureFor(type, col, emissionMode);

        int x = col * tileSize;
        int y = row * tileSize;

        if (texture != null) {
          g.drawImage(texture, x, y, tileSize, tileSize, null);
        } else if (!emissionMode) {
          // Only draw the error pattern in diffuse mode to avoid
          // magenta squares in the glow/emission map.
          drawErrorPattern(g, x, y);
        }
      }
    }

    g.dispose();
    return atlas;
  }

  /**
   * Attempts to load a texture file for a specific block and face. Logic follows a priority: 1.
   * Specific face (e.g., grass_top.png) 2. General fallback (e.g., grass.png)
   */
  private BufferedImage loadTextureFor(BlockType type, int side, boolean emissionMode) {
    String name = type.name().toLowerCase();
    String suffix = (side == 0) ? "_top" : (side == 1) ? "_bottom" : "_side";
    String ext = emissionMode ? "_e.png" : ".png";

    String path = resourcePath + name + suffix + ext;
    String fallbackPath = resourcePath + name + ext;

    try {
      // Attempt 1: Load specific side texture
      InputStream is = getClass().getResourceAsStream(path);

      // Attempt 2: Load generic fallback texture
      if (is == null) {
        is = getClass().getResourceAsStream(fallbackPath);
      }

      if (is != null) {
        BufferedImage img = ImageIO.read(is);
        is.close();
        return img;
      }
    } catch (IOException e) {
      System.err.println("[AtlasBuilder] Error reading texture: " + path);
    }
    return null;
  }

  /** Draws the classic pink/black checkerboard pattern used to indicate a missing texture. */
  private void drawErrorPattern(Graphics2D g, int x, int y) {
    g.setColor(Color.MAGENTA);
    g.fillRect(x, y, tileSize / 2, tileSize / 2);
    g.fillRect(x + tileSize / 2, y + tileSize / 2, tileSize / 2, tileSize / 2);

    g.setColor(Color.BLACK);
    g.fillRect(x + tileSize / 2, y, tileSize / 2, tileSize / 2);
    g.fillRect(x, y + tileSize / 2, tileSize / 2, tileSize / 2);
  }
}
