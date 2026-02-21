package demos.collision.texture;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class PrototypeGridTexture {

  private int width;
  private int height;

  private int labelFontSize = 16;
  private int subLabelFontSize = 12;
  private String label = "Prototype";
  private String subLabel;

  private int majorCellSize = 128;
  private int gridCellSize = 16;

  private float majorCheckerAlpha = 0.2f;
  private float minorCheckerAlpha = 0.2f;

  private Color backgroundColor = Color.LIGHT_GRAY;
  private Color borderColor = Color.WHITE;

  private boolean antialias;

  private final CheckerBoardPainter painter = new CheckerBoardPainter();

  public PrototypeGridTexture() {
    this(1024, 1024);
  }

  public PrototypeGridTexture(int width, int height) {
    this.width = width;
    this.height = height;
    this.subLabel = width + "x" + height;
  }

  public BufferedImage createImage() {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = (Graphics2D) image.getGraphics();

    applyRenderingHints(g2d);

    drawBackground(g2d);
    drawCheckerBoard(g2d);
    drawCenterGrid(g2d);
    drawLabels(g2d);
    drawBorder(g2d);

    return image;
  }

  private void applyRenderingHints(Graphics2D g2d) {
    if (!antialias) return;

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
  }

  private void drawBackground(Graphics2D g2d) {
    g2d.setColor(backgroundColor);
    g2d.fillRect(0, 0, width, height);
  }

  private void drawCheckerBoard(Graphics2D g2d) {
    // Major checker board
    painter.setCellSize(majorCellSize);
    painter.setDarkColor(new Color(0f, 0f, 0f, majorCheckerAlpha));
    painter.setLightColor(new Color(0f, 0f, 0f, 0f));
    painter.paint(g2d, 0, 0, width, height);

    // Minor checker board
    painter.setCellSize(gridCellSize);
    painter.setDarkColor(new Color(0f, 0f, 0f, minorCheckerAlpha));
    painter.paint(g2d, 0, 0, width, height);
  }

  private void drawCenterGrid(Graphics2D g2d) {
    g2d.setColor(borderColor);

    // Horizontal
    g2d.drawLine(0, height / 2, width, height / 2);

    // Vertical
    g2d.drawLine(width / 2, 0, width / 2, height);
  }

  private void drawBorder(Graphics2D g2d) {
    g2d.setColor(borderColor);
    g2d.drawRect(0, 0, width - 1, height - 1);
    g2d.drawRect(1, 1, width - 3, height - 3);
  }

  private void drawLabels(Graphics2D g2d) {
    g2d.setColor(Color.WHITE);

    // Label
    g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, labelFontSize));
    g2d.drawString(label, gridCellSize, gridCellSize + labelFontSize);

    // Sublabel
    g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, subLabelFontSize));
    g2d.drawString(subLabel, gridCellSize, gridCellSize + labelFontSize + subLabelFontSize);
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getSubLabel() {
    return subLabel;
  }

  public void setSubLabel(String subLabel) {
    this.subLabel = subLabel;
  }

  public boolean isAntialias() {
    return antialias;
  }

  public void setAntialias(boolean antialias) {
    this.antialias = antialias;
  }
}
