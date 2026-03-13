package client.entity;

import client.app.GameClient;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;

public class EntitiesComponent extends AbstractComponent implements RenderableComponent {

  private GameClient client;	
	
  public EntitiesComponent(GameClient client) {
	  this.client = client;
  }
	
  @Override
  public void render(Graphics g) {
	  client.getEntityManager().renderAll(g);
  }
}
