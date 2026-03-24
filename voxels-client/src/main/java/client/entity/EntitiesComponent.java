package client.entity;

import client.app.GameClient;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import math.Vector3f;

public class EntitiesComponent extends AbstractComponent implements RenderableComponent {

  private Vector3f camPos = new Vector3f();
  private GameClient client;

  public EntitiesComponent(GameClient client) {
    this.client = client;
  }

  @Override
  public void update(float tpf) {
    client.getEntityManager().update(tpf);
  }

  @Override
  public void render(Graphics g) {
    getOwner().getScene().getActiveCamera().getTransform().getPosition(camPos);
    client.getEntityManager().renderAll(g, camPos);
  }
}
