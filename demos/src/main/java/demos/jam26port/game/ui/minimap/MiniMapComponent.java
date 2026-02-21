package demos.jam26port.game.ui.minimap;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.render.Graphics;
import math.Vector3f;

public class MiniMapComponent extends AbstractComponent implements RenderableComponent {

  private MinimapView view;

  public MiniMapComponent(MinimapView view) {
    this.view = view;
  }

  @Override
  public void render(Graphics g) {
    view.render(g);
  }

  public void setPlayerWorldPosition(Vector3f position) {
    view.setPlayerWorldPosition(position);
  }
}
