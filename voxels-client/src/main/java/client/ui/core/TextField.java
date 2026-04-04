package client.ui.core;

import engine.rendering.Graphics;
import engine.resources.Font;
import engine.runtime.input.KeyEvent;
import math.Color;

public class TextField extends AbstractUiElement {

  private static final Font FONT = new Font("monogram-extended", 40, Font.PLAIN);

  private int offsetX;
  
  private int offsetY;

  private int x;
  
  private int y;

  private StringBuilder text = new StringBuilder();
  
  private boolean focused = false;

  private int cursorPosition = 0;

  private Color background = Color.getColorFromInt(40, 40, 40);
  
  private Color border = Color.getColorFromInt(80, 80, 80);
  
  private Color foreground = Color.getColorFromInt(228, 228, 228);

  public TextField() {
	  
  }
  
  public TextField(int offsetX, int offsetY, int width, int height) {
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.width = width;
    this.height = height;
  }

  // --- Rendering ---

  @Override
  public void render(Graphics g) {
    // Centered positioning (wie SimpleLabel)
    x = (int) ((g.getWidth() - width) * 0.5f) + offsetX;
    y = (int) ((g.getHeight() - height) * 0.5f) + offsetY;

    renderBackground(g);
    renderText(g);

    if (focused) {
      renderCursor(g);
    }
  }

  private void renderBackground(Graphics g) {
    g.setColor(background);
    g.fillRect(x, y, width, height);

    g.setColor(border);
    g.drawRect(x, y, width, height);
  }

  private void renderText(Graphics g) {
    g.setFont(FONT);
    g.setColor(foreground);

    float textX = x + 5;
    float textY = y + height * 0.7f;

    g.text(text.toString(), textX, textY);
  }

  private void renderCursor(Graphics g) {
    g.setFont(FONT);

    String beforeCursor = text.substring(0, cursorPosition);
    float cursorX = x + 5 + g.textWidth(beforeCursor);

    float top = y + 5;
    float bottom = y + height - 5;

    g.drawLine(cursorX, top, cursorX, bottom);
  }

  // --- Mouse ---

  @Override
  public void onMouseClicked(float mouseX, float mouseY) {
    focused = isInside(mouseX, mouseY);
  }

  private boolean isInside(float mx, float my) {
    return mx >= x && mx <= x + width &&
           my >= y && my <= y + height;
  }

  // --- Keyboard ---

  @Override
  public void onKeyTyped(KeyEvent e) {
    if (!focused) return;

    char c = e.getChar();

    if (c >= 32 && c != 127) {
      text.insert(cursorPosition, c);
      cursorPosition++;
    }
  }

  @Override
  public void onKeyPressed(KeyEvent e) {
    if (!focused) return;

    switch (e.getKey()) {

      case BACKSPACE -> {
        if (cursorPosition > 0) {
          text.deleteCharAt(cursorPosition - 1);
          cursorPosition--;
        }
      }

      case ARROW_LEFT -> {
        if (cursorPosition > 0) cursorPosition--;
      }

      case ARROW_RIGHT -> {
        if (cursorPosition < text.length()) cursorPosition++;
      }
    }
  }

  // --- API ---

  public String getText() {
    return text.toString();
  }

  public void setText(String value) {
    text.setLength(0);
    text.append(value);
    cursorPosition = text.length();
  }

  public boolean isFocused() {
    return focused;
  }
}