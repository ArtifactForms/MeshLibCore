package client.app;

import client.entity.ClientEntityManager;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;

public class EntitiesComponent extends AbstractComponent implements RenderableComponent {

  @Override
  public void render(Graphics g) {
	  ClientEntityManager.renderAll(g);
  }
}
