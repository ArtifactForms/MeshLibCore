package client.ui.button;

import client.ui.core.AbstractUiElement;
import common.world.SoundEffect;
import engine.rendering.Graphics;
import engine.resources.Font;
import engine.scene.audio.SoundManager;
import math.Color;

public class SimpleButton extends AbstractUiElement {

  private int offsetX;

  private int offsetY;

  private int x;

  private int y;

  private int width;

  private int height;

  private boolean hover;

  private String text;

  private Color background = Color.getColorFromInt(120, 120, 120);

  private Color foreground = Color.getColorFromInt(228, 228, 228);

  private Color hoverBackground = Color.getColorFromInt(100, 100, 100);

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
    if (hover) {
      g.setColor(hoverBackground);
    } else {
      g.setColor(background);
    }
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
      SoundManager.playEffect(SoundEffect.BUTTON_CLICK);
    }
  }

  @Override
  public void onMousePressed(float mouseX, float mouseY) {}

  @Override
  public void onMouse(float x, float y) {
    hover = contains(x, y);
  }

  private boolean contains(float x, float y) {
    return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
  }

  public void setCallback(ButtonClickCallback callback) {
    this.callback = callback;
  }
}
