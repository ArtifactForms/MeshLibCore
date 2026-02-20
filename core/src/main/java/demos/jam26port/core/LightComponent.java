package demos.jam26port.core;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.scene.light.PointLight;
import workspace.ui.Graphics;

public class LightComponent extends AbstractComponent implements RenderableComponent {

  private PointLight light;

  public LightComponent(PointLight light) {
    this.light = light;
  }

  @Override
  public void render(Graphics g) {
    g.render(light);
  }
}
