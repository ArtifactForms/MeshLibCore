package client.ui.core;

import engine.rendering.Graphics;
import engine.resources.Font;
import engine.runtime.input.Key;
import engine.runtime.input.KeyEvent;
import math.Color;

public class TextField extends AbstractUiElement {

  private int x;
  private int y;
  private int width;
  private int height;

  private boolean focused;
  private String text = "";

  private static final Font FONT = new Font("monogram-extended", 40, Font.PLAIN);

  public TextField(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public void render(Graphics g) {

    renderBackground(g);
    renderBorder(g);
    renderText(g);
  }

  private void renderBackground(Graphics g) {

    if (focused) {
      g.setColor(Color.getColorFromInt(60, 60, 60));
    } else {
      g.setColor(Color.getColorFromInt(40, 40, 40));
    }

    g.fillRect(x, y, width, height);
  }

  private void renderBorder(Graphics g) {

    g.setColor(Color.WHITE);
    g.drawRect(x, y, width, height);
  }

  private void renderText(Graphics g) {

    g.setFont(FONT);
    g.setColor(Color.WHITE);

    float textX = x + 8;
    float textY = y + height * 0.7f;

    g.text(text, textX, textY);

    if (focused) {
      renderCursor(g, textX, textY);
    }
  }

  private void renderCursor(Graphics g, float textX, float textY) {

    float textWidth = g.textWidth(text);

    float cursorX = textX + textWidth + 2;

    g.drawLine(cursorX, y + 6, cursorX, y + height - 6);
  }

  @Override
  public void onMouseClicked(float mouseX, float mouseY) {}

  @Override
  public void onMouse(float x, float y) {}

  private boolean contains(float x, float y) {

    return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
  }

  @Override
  public void onMousePressed(float mouseX, float mouseY) {
    focused = contains(mouseX, mouseY);
  }

  @Override
  public void onKeyPressed(KeyEvent e) {
    if (!isFocused()) {
      return;
    }

    if (e.getKey() != Key.BACKSPACE) {
      return;
    }

    backspace();
  }

  @Override
  public void onKeyTyped(KeyEvent e) {
    if (!isFocused()) return;
    char c = e.getChar();

    if (c != 0) {
      addCharacter(c);
    }
  }

  public void addCharacter(char c) {
    text += c;
  }

  public void backspace() {

    if (!text.isEmpty()) {
      text = text.substring(0, text.length() - 1);
    }
  }

  public String getText() {
    return text;
  }

  public boolean isFocused() {
    return focused;
  }
}
