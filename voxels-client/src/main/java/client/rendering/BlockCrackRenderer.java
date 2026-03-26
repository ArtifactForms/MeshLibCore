package client.rendering;

import engine.rendering.Graphics;
import math.Color;

public class BlockCrackRenderer {

  private static final Color color = new Color(0, 0, 0, 1f);

  private BlockCrackRenderer() {
    // No instances
  }

  private static final float OFFSET = 0.505f;

  public static void render(Graphics g, float progress) {

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
