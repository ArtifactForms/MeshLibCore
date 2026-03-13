package client.ui;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import math.Color;

public class InventoryComponent extends AbstractComponent
    implements RenderableComponent, InventoryView {

  private int scale = 4;
  private int slotSize = 16 * scale;
  private int rows = 4;
  private int cols = 9;
  private int padding = 5;
  private Color background = new Color(0, 0, 0, 0.6f);
  private Color border = new Color(1, 1, 1, 1);
  private boolean visible;

  public InventoryComponent() {}

  @Override
  public void render(Graphics g) {
    if (!visible) return;

    int offsetX = (g.getWidth() - getWidth()) / 2;
    int offsetY = (g.getHeight() - getHeight()) / 2;

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        int x = col * (slotSize + padding);
        int y = row * (slotSize + padding);
        drawBackground(g, offsetX + x, offsetY + y);
        drawBorder(g, offsetX + x, offsetY + y);
      }
    }
  }

  private void drawBackground(Graphics g, int x, int y) {
    g.setColor(background);
    g.fillRect(x, y, slotSize, slotSize);
  }

  private void drawBorder(Graphics g, int x, int y) {
    g.setColor(border);
    g.drawRect(x, y, slotSize, slotSize);
  }

  private int getWidth() {
    return (cols * (slotSize + padding)) - padding;
  }

  private int getHeight() {
    return (rows * (slotSize + padding)) - padding;
  }

  @Override
  public void display() {
    this.visible = true;
  }

  @Override
  public void hide() {
    this.visible = false;
  }
}
