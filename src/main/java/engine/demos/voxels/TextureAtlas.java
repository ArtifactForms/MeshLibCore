package engine.demos.voxels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.resources.Texture;
import engine.resources.TextureManager;
import math.PerlinNoise;
import math.Vector2f;

public class TextureAtlas {

  private static final Color DIRT_COLOR = new Color(151, 109, 76);

  private boolean useNoise = GameSettings.textureNoise;
  private boolean drawDebugText = GameSettings.textureDebugText;
  private boolean fillTextureBackground = GameSettings.textureBackground;
  private float epsilon = 0.001f; // Small margin to prevent texture bleeding
  private int tileSize = 128;
  private int columns = 6; // 6 faces per block
  private int rows = BlockType.values().length;
  private int width = tileSize * columns;
  private int height = tileSize * rows;
  private ArrayList<Vector2f> uvCoordinates;
  private BufferedImage image;
  private BufferedImage overlay;
  private Texture texture;

  public TextureAtlas() {
    createOverlay();
    createTextureImage();
    createAllUVCoordinates();
    createTexture();
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
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = (Graphics2D) image.getGraphics();

    g2d.setColor(Color.GRAY);
    if (fillTextureBackground) g2d.fillRect(0, 0, width, height);

    fill(g2d, BlockType.STONE, new Color(123, 123, 123));
    fill(g2d, BlockType.COBBLE_STONE, new Color(108, 108, 108));
    fill(g2d, BlockType.GRASS_BLOCK, new Color(117, 175, 74));
    fill(g2d, BlockType.GRASS, new Color(37, 82, 12));
    fill(g2d, BlockType.LEAF, new Color(66, 175, 29));
    fill(g2d, BlockType.OAK_WOOD, new Color(111, 87, 51));
    fill(g2d, BlockType.BIRCH_WOOD, new Color(255, 255, 255));
    fill(g2d, BlockType.DIRT, DIRT_COLOR);
    fill(g2d, BlockType.WATER, new Color(0, 0, 128, 128));
    fill(g2d, BlockType.SNOW, new Color(253, 253, 253));
    fill(g2d, BlockType.SAND, new Color(216, 207, 156));
    fill(g2d, BlockType.SAND, new Color(194, 189, 134));
    fill(g2d, BlockType.CACTUS, new Color(96, 142, 50));
    fill(g2d, BlockType.GRAVEL, new Color(180, 180, 180));

    drawDebugText(g2d);
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
        g2d.drawString(BlockType.fromId((short) row) + "", x + 2, y + 24);
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

        int alpha = (int) (((noise1 * 2) + 1) * 20);
        image.setRGB(x, y, new Color(0, 0, 0, alpha).getRGB());
      }
    }
    return image;
  }

  private void fill(Graphics2D g2d, BlockType blockType, Color color) {
    int row = blockType.getId();
    int baseY = row * tileSize;

    for (int col = 0; col < 6; col++) {
      int baseX = col * tileSize;

      // Base color
      g2d.setColor(color);
      g2d.fillRect(baseX, baseY, tileSize, tileSize);

      if (blockType == BlockType.GRASS_BLOCK && col > 0) {
        // Grass block sides
        g2d.setColor(DIRT_COLOR);
        g2d.fillRect(baseX, baseY + (tileSize / 4), tileSize, tileSize - (tileSize / 4));
      }

      // Noise
      g2d.drawImage(overlay, baseX, baseY, tileSize, tileSize, null);

      // Fake light
      g2d.setColor(new Color(0, 0, 0, 255 / 6 * col));
      g2d.fillRect(baseX, baseY, tileSize, tileSize);
    }
  }

  public int[] getUVIndices(int blockId, int ordinal) {
    int index = (blockId * columns + ordinal) * 4;
    return new int[] {index + 3, index + 2, index + 1, index};
  }

  public Vector2f[] getUVCoordinates(int blockId, int ordinal) {
    int[] uvIndices = getUVIndices(blockId, ordinal);
    Vector2f uv0 = uvCoordinates.get(uvIndices[0]);
    Vector2f uv1 = uvCoordinates.get(uvIndices[1]);
    Vector2f uv2 = uvCoordinates.get(uvIndices[2]);
    Vector2f uv3 = uvCoordinates.get(uvIndices[3]);
    return new Vector2f[] {uv0, uv1, uv2, uv3};
  }

  public ArrayList<Vector2f> getUVCoordinates() {
    return uvCoordinates;
  }

  public Texture getTexture() {
    return texture;
  }
}
