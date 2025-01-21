package engine.demos.voxels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.resources.Texture;
import engine.resources.TextureManager;
import math.Vector2f;

public class TextureAtlas {

  private int tileSize = 16;
  private int width = 320;
  private int height = tileSize;
  private ArrayList<Vector2f> uvCoordinates;
  private BufferedImage image;
  private Texture texture;

  public TextureAtlas() {
    createImage();
    createAllUVCoordinates();
    texture = TextureManager.getInstance().createTexture(image);
    //    texture = TextureManager.getInstance().loadTexture("atlas.png");
  }

  private void createAllUVCoordinates() {
    uvCoordinates = new ArrayList<Vector2f>();

    float uStep = 1f / (width / (float) tileSize);

    for (int i = 0; i < width / tileSize; i++) {
      Vector2f uv2 = new Vector2f((i + 0) * uStep, 0);
      Vector2f uv3 = new Vector2f((i + 1) * uStep, 0);
      Vector2f uv0 = new Vector2f((i + 1) * uStep, 1);
      Vector2f uv1 = new Vector2f((i + 0) * uStep, 1);
      uvCoordinates.add(uv0);
      uvCoordinates.add(uv1);
      uvCoordinates.add(uv2);
      uvCoordinates.add(uv3);
    }
  }

  private void createImage() {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = (Graphics2D) image.getGraphics();
    g2d.setColor(Color.GRAY);
    g2d.fillRect(0, 0, width, height);
    g2d.setColor(new Color(200, 200, 200));

    for (int i = 0; i < width / tileSize; i++) {
      g2d.drawRect(i * tileSize, 0, tileSize - 1, tileSize - 1);
      g2d.drawString("" + i, i * tileSize, 12);
    }

    g2d.setColor(new Color(255, 255, 0, 128));
    g2d.fillRect(tileSize, 0, tileSize, tileSize);
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
