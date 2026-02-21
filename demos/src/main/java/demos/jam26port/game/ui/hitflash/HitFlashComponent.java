package demos.jam26port.game.ui.hitflash;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;

public class HitFlashComponent extends AbstractComponent implements RenderableComponent {

  private HitFlashView view;

  public HitFlashComponent(HitFlashView view) {
    this.view = view;
  }

  public void displayHitFlash(float strength) {
    view.trigger(strength);
  }

  @Override
  public void onUpdate(float tpf) {
    view.update(tpf);
  }

  @Override
  public void render(Graphics g) {
    view.render(g);
  }
}
