package client.ui;

import client.ui.core.AbstractUiElement;
import engine.rendering.Graphics;
import engine.resources.Font;
import math.Color;

public class SimpleLabel extends AbstractUiElement {

  private static final Font FONT = new Font("monogram-extended", 40, Font.PLAIN);

  private int offsetX;

  private int offsetY;

  private int x;

  private int y;

  private int width;

  private int height;

  private String text;

  private Color foreground = Color.getColorFromInt(228, 228, 228);

  private TextAlignment alignment = TextAlignment.CENTER;

  public SimpleLabel(String text, int x, int y, int width, int height) {
    this.text = text;
    this.offsetX = x;
    this.offsetY = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public void render(Graphics g) {
    x = (int) ((g.getWidth() - width) * 0.5f) + offsetX;
    y = (int) ((g.getHeight() - height) * 0.5f) + offsetY;

    renderText(g);
  }

  private void renderText(Graphics g) {
    g.setColor(foreground);
    g.setFont(FONT);

    float textWidth = g.textWidth(text);
    float textX;

    switch (alignment) {
      case LEFT:
        textX = x;
        break;
      case RIGHT:
        textX = x + (width - textWidth);
        break;
      case CENTER:
      default:
        textX = x + (width - textWidth) * 0.5f;
        break;
    }

    float textY = y + height * 0.7f;

    g.text(text, textX, textY);
  }

  public void setText(String text) {
    this.text = text;
  }

  public TextAlignment getAlignment() {
    return alignment;
  }

  public void setAlignment(TextAlignment alignment) {
    this.alignment = alignment;
  }

  @Override
  public void onMouseClicked(float mouseX, float mouseY) {}

  @Override
  public void onMousePressed(float mouseX, float mouseY) {}

  @Override
  public void onMouse(float x, float y) {}
}
