package demos.collision.texture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class CheckerBoardPainter implements Painter {

  private int cellSize;
  private Color darkColor;
  private Color lightColor;
  private BufferedImage texture;

  public CheckerBoardPainter() {
    this(8, new Color(127, 127, 127), new Color(195, 195, 195));
  }

  public CheckerBoardPainter(int cellSize, Color darkColor, Color lightColor) {
    this.cellSize = cellSize;
    this.darkColor = darkColor;
    this.lightColor = lightColor;
    createTexture();
  }

  private void createTexture() {
    texture = new BufferedImage(cellSize * 2, cellSize * 2, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = (Graphics2D) texture.getGraphics();
    g2d.setColor(lightColor);
    g2d.fillRect(0, 0, cellSize, cellSize);
    g2d.fillRect(cellSize, cellSize, cellSize, cellSize);
    g2d.setColor(darkColor);
    g2d.fillRect(cellSize, 0, cellSize, cellSize);
    g2d.fillRect(0, cellSize, cellSize, cellSize);
  }

  @Override
  public void paint(Graphics2D g2d, int x, int y, int width, int height) {
    TexturePaint texturePaint =
        new TexturePaint(
            texture, new Rectangle2D.Double(x, y, texture.getWidth(), texture.getHeight()));
    g2d.setPaint(texturePaint);
    g2d.fillRect(x, y, width, height);
  }

  public int getCellSize() {
    return cellSize;
  }

  public void setCellSize(int cellSize) {
    this.cellSize = cellSize;
    createTexture();
  }

  public Color getDarkColor() {
    return darkColor;
  }

  public void setDarkColor(Color darkColor) {
    this.darkColor = darkColor;
    createTexture();
  }

  public Color getLightColor() {
    return lightColor;
  }

  public void setLightColor(Color lightColor) {
    this.lightColor = lightColor;
    createTexture();
  }
}
