package client.resources;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import client.settings.GameSettings;
import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.game.block.Blocks;
import engine.resources.Texture;
import engine.resources.TextureManager;
import math.PerlinNoise;
import math.Vector2f;

public class TextureAtlas {

  private static final Color DIRT_COLOR = new Color(151, 109, 76);

  private boolean useNoise = GameSettings.textureNoise;
  private boolean drawDebugText = GameSettings.textureDebugText;
  private boolean fillTextureBackground = GameSettings.textureBackground;
  private float epsilon = 0.002f; // Small margin to prevent texture bleeding
  public static int tileSize = 16;
  private int columns = 6; // 6 faces per block
  private int rows = BlockRegistry.size();
  private int width = tileSize * columns;
  private int height = tileSize * rows;
  private ArrayList<Vector2f> uvCoordinates;
  private BufferedImage image;
  private BufferedImage overlay;
  private Texture texture;
  private PerlinNoise noise = new PerlinNoise(0);

  private boolean useAtlasImage = false;

  public TextureAtlas() {

    if (useAtlasImage) {
      texture = TextureManager.getInstance().loadTexture("atlas.png");
      width = texture.getWidth();
      height = texture.getHeight();
      rows = height / tileSize;
      createAllUVCoordinates();
    } else {
      createOverlay();
      createTextureImage();
      createAllUVCoordinates();
      createTexture();
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getTileSize() {
    return tileSize;
  }

  private void createOverlay() {
    overlay = createNoiseOverlay();
  }

  private void createTexture() {
    texture = TextureManager.getInstance().createTexture(image);
  }

  private void createAllUVCoordinates() {
    uvCoordinates = new ArrayList<>();

    float uStep = 1f / (width / (float) tileSize);
    float vStep = 1f / (height / (float) tileSize);

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        float uStart = col * uStep + epsilon;
        float uEnd = (col + 1) * uStep - epsilon;
        float vStart = row * vStep + epsilon;
        float vEnd = (row + 1) * vStep - epsilon;

        // Add UV coordinates with epsilon adjustment
        Vector2f uv0 = new Vector2f(uEnd, vEnd); // Top-right
        Vector2f uv1 = new Vector2f(uStart, vEnd); // Top-left
        Vector2f uv2 = new Vector2f(uStart, vStart); // Bottom-left
        Vector2f uv3 = new Vector2f(uEnd, vStart); // Bottom-right

        uvCoordinates.add(uv0);
        uvCoordinates.add(uv1);
        uvCoordinates.add(uv2);
        uvCoordinates.add(uv3);
      }
    }
  }

  private void createTextureImage() {
    if (GameSettings.dynamicTextures) {
      image = new DynamicAtlasBuilder(tileSize).build(false);
      return;
    }

    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = (Graphics2D) image.getGraphics();

    g2d.setColor(Color.GRAY);
    if (fillTextureBackground) g2d.fillRect(0, 0, width, height);

    fill(g2d, Blocks.AIR, new Color(0, 0, 0, 0));
    fill(g2d, Blocks.STONE, new Color(123, 123, 123));
    fill(g2d, Blocks.COBBLE_STONE, new Color(108, 108, 108));
    fill(g2d, Blocks.GRASS_BLOCK, new Color(117, 175, 74));
    fill(g2d, Blocks.GRASS, new Color(37, 82, 12));
    fill(g2d, Blocks.LEAF, new Color(66, 175, 29));
    fill(g2d, Blocks.OAK_WOOD, new Color(111, 87, 51));
    fill(g2d, Blocks.BIRCH_WOOD, new Color(255, 255, 255));
    fill(g2d, Blocks.DIRT, DIRT_COLOR);
    //    fill(g2d, BlockType.WATER, new Color(45, 137, 212, 160));
    //    fill(g2d, Blocks.WATER, new Color(45, 137, 212, 120));
    fill(g2d, Blocks.SNOW, new Color(253, 253, 253));
    fill(g2d, Blocks.SAND, new Color(194, 189, 134));
    fill(g2d, Blocks.CACTUS, new Color(96, 142, 50));
    fill(g2d, Blocks.GRAVEL, new Color(180, 180, 180));
    fill(g2d, Blocks.SPRUCE_WOOD, new Color(91, 49, 56));
    fill(g2d, Blocks.SPRUCE_LEAF, new Color(35, 103, 78));
    fill(g2d, Blocks.BEDROCK, new Color(51, 57, 65));

    // TERRACOTTA
    fill(g2d, Blocks.BLACK_TERRACOTTA, new Color(16, 18, 28)); // AAP64 color
    fill(g2d, Blocks.RED_TERRACOTTA, new Color(172, 40, 71)); // AAP64 color
    fill(g2d, Blocks.GREEN_TERRACOTTA, new Color(0, 101, 84)); // AAP64 color
    fill(g2d, Blocks.BROWN_TERRACOTTA, new Color(110, 76, 48)); // AAP64 color

    drawDebugText(g2d);
  }

  //  private void createTextureImage() {
  //	    if (GameSettings.dynamicTextures) {
  //	        image = new DynamicAtlasBuilder(tileSize).build(false);
  //	        return;
  //	    }
  //
  //	    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  //	    Graphics2D g2d = (Graphics2D) image.getGraphics();
  //
  //	    // 1. Optional: Fill background if enabled
  //	    if (fillTextureBackground) {
  //	        g2d.setColor(Color.GRAY);
  //	        g2d.fillRect(0, 0, width, height);
  //	    }
  //
  //	    // 2. Iterate through all blocks from the registry
  //	    for (BlockType block : BlockRegistry.getAll()) {
  //
  //	        Color color;
  //
  //	        // Special case for AIR: always fully transparent
  //	        if (block.getId() == 0) {
  //	            color = new Color(0, 0, 0, 0);
  //	        } else {
  //	            // Create color using your getR(), getG(), getB() methods
  //	            color = new Color(block.getR(), block.getG(), block.getB());
  //	        }
  //
  //	        // Use your existing fill method to draw the block into the atlas
  //	        fill(g2d, block, color);
  //	    }
  //
  //	    drawDebugText(g2d);
  //
  //	    // 3. Clean up
  //	    g2d.dispose();
  //	}

  private void drawGrassTexture(Graphics2D g2d, int x, int y) {

    int size = tileSize;

    g2d.setComposite(java.awt.AlphaComposite.Clear);
    g2d.fillRect(x, y, size, size);

    g2d.setComposite(java.awt.AlphaComposite.SrcOver);

    Color base = new Color(117, 175, 74);

    java.util.Random rand = new java.util.Random(x * 928371 + y * 123123);

    for (int i = 0; i < size * 4; i++) {

      int px = rand.nextInt(size);
      int height = 6 + rand.nextInt(size / 2);
      int py = y + size - height;

      // Variation
      int variation = rand.nextInt(50) - 25;

      int r = clamp(base.getRed() + variation, 0, 255);
      int g = clamp(base.getGreen() + variation + 30, 0, 255);
      int b = clamp(base.getBlue() + variation, 0, 255);

      g2d.setColor(new Color(r, g, b, 200));

      int width = rand.nextBoolean() ? 1 : 2;

      g2d.fillRect(x + px, py, width, height);
    }

    for (int i = 0; i < size; i++) {
      int px = rand.nextInt(size);
      int py = y + size - rand.nextInt(size / 2);

      g2d.setColor(new Color(60, 140, 40, 180));

      g2d.drawLine(x + px, py, x + px + rand.nextInt(3) - 1, py - 4);
    }
  }

  private int clamp(int v, int min, int max) {
    return Math.max(min, Math.min(max, v));
  }

  private void drawDebugText(Graphics2D g2d) {
    if (!drawDebugText) return;

    g2d.setColor(new Color(255, 255, 255, 60));
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        int x = col * tileSize;
        int y = row * tileSize;
        if (GameSettings.textureBorder) g2d.drawRect(x, y, tileSize - 1, tileSize - 1);
        g2d.drawString("Row " + row + " Col " + col, x + 2, y + 12);
        g2d.drawString(BlockRegistry.get((short) row) + "", x + 2, y + 24);
        g2d.drawString("Type Id: " + row, x + 2, y + 36);
      }
    }
  }

  private BufferedImage createNoiseOverlay() {
    BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

    if (!useNoise) return image;

    PerlinNoise noise = new PerlinNoise(0);

    for (int x = 0; x < 16; x++) {
      for (int y = 0; y < 16; y++) {
        float noise1 = (float) noise.noise(x * 0.6f, y * 0.6f);

        int alpha = (int) (((noise1 * 2) + 1) * 10);
        image.setRGB(x, y, new Color(0, 0, 0, alpha).getRGB());
      }
    }
    return image;
  }

  //  private void fill(Graphics2D g2d, BlockType blockType, Color color) {
  //    int row = blockType.getId();
  //    int baseY = row * tileSize;
  //
  //    for (int col = 0; col < 6; col++) {
  //      int baseX = col * tileSize;
  //
  //      // Base color
  //      g2d.setColor(color);
  //      g2d.fillRect(baseX, baseY, tileSize, tileSize);
  //
  //      if (blockType == Blocks.GRASS_BLOCK && col > 0) {
  //        // Grass block sides
  //        g2d.setColor(DIRT_COLOR);
  //        g2d.fillRect(baseX, baseY + (tileSize / 4), tileSize, tileSize - (tileSize / 4));
  //      }
  //
  //      // Noise
  //      g2d.drawImage(overlay, baseX, baseY, tileSize, tileSize, null);
  //
  //      // Fake light
  //      //            g2d.setColor(new Color(0, 0, 0, 255 / 6 * col));
  //      //            g2d.fillRect(baseX, baseY, tileSize, tileSize);
  //    }
  //  }

  private void fill(Graphics2D g2d, BlockType blockType, Color color) {
    int row = blockType.getId();
    int baseY = row * tileSize;

    for (int col = 0; col < 6; col++) {
      int baseX = col * tileSize;

      if (blockType == Blocks.GRASS) {
        drawGrassTexture(g2d, baseX, baseY);
        continue;
      }

      g2d.setColor(color);
      g2d.fillRect(baseX, baseY, tileSize, tileSize);

      if (blockType.getName().toUpperCase().contains("TERRACOTTA")) {
        drawTerracottaPattern(g2d, baseX, baseY, color);
      }

      if (blockType == Blocks.GRASS_BLOCK && col > 0) {
        g2d.setColor(DIRT_COLOR);
        g2d.fillRect(baseX, baseY + (tileSize / 4), tileSize, tileSize - (tileSize / 4));
      }

      if (useNoise) {
        g2d.drawImage(overlay, baseX, baseY, tileSize, tileSize, null);
      }
    }
  }

  private void drawTerracottaPattern(Graphics2D g2d, int x, int y, Color base) {
    Color darkTone =
        new Color(
            Math.max(0, (int) (base.getRed() * 0.8)),
            Math.max(0, (int) (base.getGreen() * 0.8)),
            Math.max(0, (int) (base.getBlue() * 0.8)),
            120 // Alpha für sanftes Blending
            );

    Color lightTone =
        new Color(
            Math.min(255, (int) (base.getRed() * 1.1)),
            Math.min(255, (int) (base.getGreen() * 1.1)),
            Math.min(255, (int) (base.getBlue() * 1.1)),
            80);

    java.util.Random rand = new java.util.Random(x + y);

    for (int i = 0; i < tileSize; i++) {
      int px = rand.nextInt(tileSize - 4);
      int py = rand.nextInt(tileSize - 1);

      g2d.setColor(rand.nextBoolean() ? darkTone : lightTone);
      int length = 2 + rand.nextInt(5);
      g2d.fillRect(x + px, y + py, length, 1);
    }
  }

  public int[] getUVIndices(int blockId, int ordinal) {
    int index = (blockId * columns + ordinal) * 4;
    return new int[] {index + 3, index + 2, index + 1, index};
  }

  public Vector2f[] getUVCoordinates(int blockId, int face) {

    int col = face;
    int row = blockId;

    float tileU = 1f / columns;
    float tileV = 1f / rows;

    float u0 = col * tileU;
    float v0 = row * tileV;

    float u1 = u0 + tileU;
    float v1 = v0 + tileV;

    u0 += epsilon;
    v0 += epsilon;
    u1 -= epsilon;
    v1 -= epsilon;

    return new Vector2f[] {
      new Vector2f(u1, v1), new Vector2f(u0, v1), new Vector2f(u0, v0), new Vector2f(u1, v0)
    };
  }

  public ArrayList<Vector2f> getUVCoordinates() {
    return uvCoordinates;
  }

  public Texture getTexture() {
    return texture;
  }
}
