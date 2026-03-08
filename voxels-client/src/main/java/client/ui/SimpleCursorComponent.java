package client.ui;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import math.Color;

public class SimpleCursorComponent extends AbstractComponent implements RenderableComponent {

  private float x;
  private float y;
  private float halfSize = 10;
  private Input input;

  public SimpleCursorComponent(Input input) {
    this.input = input;
  }

  @Override
  public void onUpdate(float tpf) {
    x = input.getMouseX();
    y = input.getMouseY();
  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.WHITE);
    g.drawLine(x - halfSize, y, x + halfSize, y);
    g.drawLine(x, y - halfSize, x, y + halfSize);
  }
}
