package client.ui.core;

import engine.rendering.Graphics;
import engine.resources.Font;
import engine.runtime.input.KeyEvent;
import math.Color;

public class Label extends AbstractUiElement {

  private static final Font FONT = new Font("monogram-extended", 40, Font.PLAIN);

  private int x;

  private int y;

  private String text;

  public Label(String text, int x, int y) {
    this.text = text;
    this.x = x;
    this.y = y;
  }

  @Override
  public void onMouse(float x, float y) {
    // Not used by labels
  }

  @Override
  public void onMouseClicked(float x, float y) {
    // Not used by labels
  }

  @Override
  public void onMousePressed(float mouseX, float mouseY) {
    // Not used by labels
  }

  @Override
  public void onKeyPressed(KeyEvent e) {
    // Not used by labels
  }

  @Override
  public void onKeyTyped(KeyEvent e) {
    // Not used by labels
  }

  @Override
  public void render(Graphics g) {

    g.setFont(FONT);
    g.setColor(Color.WHITE);
    g.text(text, this.x, this.y);
  }
}
