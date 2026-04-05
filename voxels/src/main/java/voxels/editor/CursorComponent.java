package voxels.editor;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import math.Color;

public class CursorComponent extends AbstractComponent implements RenderableComponent {

  private int halfSize = 10;

  private Input input;

  public CursorComponent(Input input) {
    this.input = input;
  }

  @Override
  public void render(Graphics g) {
    float x = input.getMouseX();
    float y = input.getMouseY();

    g.setColor(Color.BLACK);
    g.drawLine(x - halfSize + 1, y + 1, x + halfSize + 1, y + 1);
    g.drawLine(x + 1, y - halfSize + 1, x + 1, y + halfSize + 1);

    g.setColor(Color.WHITE);
    g.drawLine(x - halfSize, y, x + halfSize, y);
    g.drawLine(x, y - halfSize, x, y + halfSize);
  }
}
