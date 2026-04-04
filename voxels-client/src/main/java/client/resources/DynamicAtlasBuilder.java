package client.resources;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.game.block.Blocks;

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
   * Builds the texture atlas image. Each block type occupies one row.
   *
   * <p>Columns: 0 = Top 1 = Bottom 2-5 = Sides
   */
  public BufferedImage build(boolean emissionMode) {

    int rows = BlockRegistry.size();
    int columns = 6;

    BufferedImage atlas =
        new BufferedImage(columns * tileSize, rows * tileSize, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g = atlas.createGraphics();

    for (BlockType block : BlockRegistry.getAll()) {

      if (block == null) continue;
      if (block == Blocks.AIR) continue;

      int row = block.getId();

      for (int col = 0; col < columns; col++) {

        BufferedImage texture = loadTextureFor(block, col, emissionMode);

        int x = col * tileSize;
        int y = row * tileSize;

        if (texture != null) {
          g.drawImage(texture, x, y, tileSize, tileSize, null);
        } else if (!emissionMode) {
          drawErrorPattern(g, x, y);
        }
      }
    }

    g.dispose();
    return atlas;
  }

  /**
   * Attempts to load a texture file for a specific block and face.
   *
   * <p>Priority: 1. grass_top.png 2. grass.png
   */
  private BufferedImage loadTextureFor(BlockType block, int side, boolean emissionMode) {

    String name = block.getName();

    // Remove namespace (core:stone -> stone)
    if (name.contains(":")) {
      name = name.split(":")[1];
    }

    String suffix = (side == 0) ? "_top" : (side == 1) ? "_bottom" : "_side";
    String ext = emissionMode ? "_e.png" : ".png";

    String path = resourcePath + name + suffix + ext;
    String fallbackPath = resourcePath + name + ext;

    try {

      InputStream is = getClass().getResourceAsStream(path);

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

  /** Draws the classic magenta/black checkerboard pattern used for missing textures. */
  private void drawErrorPattern(Graphics2D g, int x, int y) {

    g.setColor(Color.MAGENTA);
    g.fillRect(x, y, tileSize / 2, tileSize / 2);
    g.fillRect(x + tileSize / 2, y + tileSize / 2, tileSize / 2, tileSize / 2);

    g.setColor(Color.BLACK);
    g.fillRect(x + tileSize / 2, y, tileSize / 2, tileSize / 2);
    g.fillRect(x, y + tileSize / 2, tileSize / 2, tileSize / 2);
  }
}
