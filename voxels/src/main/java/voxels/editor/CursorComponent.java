package voxels.editor;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import math.Color;

public class CursorComponent extends AbstractComponent implements RenderableComponent {

  private int halfSize = 5;
  private Input input;

  public CursorComponent(Input input) {
    this.input = input;
  }

  @Override
  public void render(Graphics g) {
    float x = input.getMouseX();
    float y = input.getMouseY();
    g.setColor(Color.WHITE);
    g.drawLine(x - halfSize, y, x + halfSize, y);
    g.drawLine(x, y - halfSize, x, y + halfSize);
  }
}