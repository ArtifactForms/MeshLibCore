package client.ui;

import engine.rendering.Graphics;
import engine.resources.Font;
import math.Color;

public class SimpleButton implements UiElement {

  private int offsetX;
  private int offsetY;
  private int x;
  private int y;
  private int width;
  private int height;
  private String text;
  private Color background = Color.getColorFromInt(76, 134, 158);
  private Color foreground = Color.getColorFromInt(228, 228, 228);
  private ButtonClickCallback callback;

  private static final Font FONT = new Font("monogram-extended", 40, Font.PLAIN);

  public SimpleButton(String text, int x, int y, int width, int height) {
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
	
    renderBackground(g);
    renderText(g);
    renderBorder(g);
  }

  private void renderBackground(Graphics g) {
    g.setColor(background);
    g.fillRect(x, y, width, height);
  }

  private void renderBorder(Graphics g) {
    g.setColor(Color.BLACK);
    g.drawRect(x, y, width, height);
    
    g.setColor(Color.WHITE);
    g.drawLine(x + 1, y + 1, x + width - 1, y + 1);
    g.drawLine(x + 1, y + 1, x + 1, y + height - 1);
  }

  private void renderText(Graphics g) {
    g.setColor(foreground);
    g.setFont(FONT);

    float textWidth = g.textWidth(text);

    // center horizontally
    float textX = x + (width - textWidth) * 0.5f;

    // simple vertical placement (baseline inside button)
    float textY = y + height * 0.7f;

    g.text(text, textX, textY);
  }

  @Override
  public void onMouseClicked(float mouseX, float mouseY) {
    if (contains(mouseX, mouseY) && callback != null) {
      callback.onButtonClicked();
    }
  }

  private boolean contains(float x, float y) {
    return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
  }

  public void setCallback(ButtonClickCallback callback) {
    this.callback = callback;
  }
}
