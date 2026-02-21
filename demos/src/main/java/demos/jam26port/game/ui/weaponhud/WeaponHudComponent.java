package demos.jam26port.game.ui.weaponhud;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.render.Graphics;

public class WeaponHudComponent extends AbstractComponent implements RenderableComponent {

  private WeaponView view;

  public WeaponHudComponent(WeaponView view) {
    this.view = view;
  }

  @Override
  public void render(Graphics g) {
    view.render(g);
  }
}
