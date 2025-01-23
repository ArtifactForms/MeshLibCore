package engine.demos.voxels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.resources.Texture;
import engine.resources.TextureManager;
import math.Vector2f;

public class TextureAtlas {

  private float epsilon = 0.0001f; // Small margin to prevent texture bleeding
  private int tileSize = 128;
  private int width = tileSize * BlockType.values().length;
  private int height = tileSize;
  private ArrayList<Vector2f> uvCoordinates;
  private BufferedImage image;
  private Texture texture;

  public TextureAtlas() {
    createImage();
    createAllUVCoordinates();
    createTexture();
  }

  private void createTexture() {
    texture = TextureManager.getInstance().createTexture(image);
  }

  private void createAllUVCoordinates() {
    uvCoordinates = new ArrayList<>();

    float uStep = 1f / (width / (float) tileSize);
    float vStep = 1f / (height / (float) tileSize);

    for (int i = 0; i < width / tileSize; i++) {
      float uStart = i * uStep + epsilon;
      float uEnd = (i + 1) * uStep - epsilon;
      float vStart = 0 + epsilon;
      float vEnd = 1 - epsilon;

      // Add UV coordinates with epsilon adjustment
      Vector2f uv0 = new Vector2f(uEnd, 1 - vEnd); // Top-right
      Vector2f uv1 = new Vector2f(uStart, 1 - vEnd); // Top-left
      Vector2f uv2 = new Vector2f(uStart, 1 - vStart); // Bottom-left
      Vector2f uv3 = new Vector2f(uEnd, 1 - vStart); // Bottom-right

      uvCoordinates.add(uv0);
      uvCoordinates.add(uv1);
      uvCoordinates.add(uv2);
      uvCoordinates.add(uv3);
    }
  }

  private void createImage() {
    int id = 0;

    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = (Graphics2D) image.getGraphics();
    g2d.setColor(Color.GRAY);
    g2d.fillRect(0, 0, width, height);

    // Stone
    id = BlockType.STONE.getId();
    g2d.setColor(new Color(128, 128, 128));
    g2d.fillRect(tileSize * id, 0, tileSize, tileSize);

    // Grass Block
    id = BlockType.GRASS_BLOCK.getId();
    g2d.setColor(new Color(117, 175, 74));
    g2d.fillRect(tileSize * id, 0, tileSize, tileSize);

    // Grass
    id = BlockType.GRASS.getId();
    g2d.setColor(new Color(37, 82, 12));
    g2d.fillOval(tileSize * id, 0, tileSize, tileSize);

    // Leaf
    id = BlockType.LEAF.getId();
    g2d.setColor(new Color(66, 175, 29));
    g2d.fillRect(tileSize * id, 0, tileSize, tileSize);

    // Wood
    id = BlockType.WOOD.getId();
    g2d.setColor(new Color(111, 87, 51));
    g2d.fillRect(tileSize * id, 0, tileSize, tileSize);

    // Dirt
    id = BlockType.DIRT.getId();
    g2d.setColor(new Color(151, 109, 76));
    g2d.fillRect(tileSize * id, 0, tileSize, tileSize);

    // Water
    id = BlockType.WATER.getId();
    g2d.setColor(new Color(0, 0, 128, 128));
    g2d.fillRect(tileSize * id, 0, tileSize, tileSize);
    
    // Snow
    id = BlockType.SNOW.getId();
    g2d.setColor(new Color(253, 253, 253));
    g2d.fillRect(tileSize * id, 0, tileSize, tileSize);

    // Info text
    g2d.setColor(new Color(255, 255, 255, 60));
    for (int i = 0; i < width / tileSize; i++) {
      BlockType type = BlockType.fromId(i);
      g2d.drawRect(i * tileSize, 0, tileSize - 1, tileSize - 1);
      g2d.drawString("" + i + " " + type, i * tileSize + 2, 12);
    }
  }

  public int[] getUVIndices(int blockId) {
    int index = blockId * 4;
    return new int[] {index, index + 1, index + 2, index + 3};
  }

  public ArrayList<Vector2f> getUVCoordinates() {
    return uvCoordinates;
  }

  public Texture getTexture() {
    return texture;
  }
}
