package demos.jam26port.game.ui.health;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.render.Graphics;

public class HealthBarComponent extends AbstractComponent implements RenderableComponent {

  private HealthBarView view;

  public HealthBarComponent(HealthBarView view) {
    this.view = view;
  }

  @Override
  public void onUpdate(float tpf) {
    view.update(tpf);
  }

  @Override
  public void render(Graphics g) {
    view.render(g);
  }

  public void displayNormalizedHealth(float normalizedHealth) {
    view.setHealth01(normalizedHealth);
  }
}
