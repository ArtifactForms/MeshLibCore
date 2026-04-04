package client.ui;

import client.app.GameClient;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.resources.Font;
import math.Color;

public class PingComponent extends AbstractComponent implements RenderableComponent {

  private GameClient client;

  private Font font;

  public PingComponent(GameClient client) {
    this.client = client;
    this.font = new Font("monogram-extended", 32, Font.PLAIN);
  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.WHITE);
    g.setFont(font);

    String text = "" + client.getPingTracker().getPing() + " ms";
    float x = g.getWidth() - g.textWidth(text) - 20;
    float y = g.getHeight() - 20;
    g.text(text, x, y);
  }
}
