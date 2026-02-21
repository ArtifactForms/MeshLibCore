package demos.cube;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.runtime.input.Input;
import engine.runtime.input.MouseMode;
import math.Color;
import workspace.ui.Graphics;

/**
 * Simple UI component that visualizes the current cursor position.
 *
 * <p>In LOCKED mode the cursor remains centered. In ABSOLUTE mode it follows the actual mouse
 * position.
 */
public class Cursor extends AbstractComponent implements RenderableComponent {

  private Input input;

  public Cursor(Input input) {
    this.input = input;
  }

  private float x;
  private float y;
  private float radius = 5;

  @Override
  public void onUpdate(float tpf) {

    if (input.getMouseMode() == MouseMode.LOCKED) {
      x = input.getScreenWidth() * 0.5f;
      y = input.getScreenHeight() * 0.5f;
      return;
    }

    x = input.getMouseX();
    y = input.getMouseY();
  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.WHITE);
    g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
  }
}
