package demos.jam26.ui;

import demos.jam26.player.PlayerHealthComponent;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import math.Color;
import math.Mathf;
import workspace.ui.Graphics;

/** Simple HUD health bar bound to a PlayerHealthComponent. Renders background, fill, and border. */
public class HealthBarUIComponent extends AbstractComponent implements RenderableComponent {

  private static final float BORDER_PADDING = 2f;
  private static final float BG_ALPHA = 0.5f;

  private final PlayerHealthComponent health;

  private float x;
  private float y;
  private float width;
  private float height;

  public HealthBarUIComponent(PlayerHealthComponent health) {
    this.health = health;
    this.x = 20;
    this.y = 20;
    this.width = 200;
    this.height = 16;
  }

  @Override
  public void render(Graphics g) {
    drawBackground(g);
    drawHealthFill(g);
    drawBorder(g);
  }

  private void drawBackground(Graphics g) {
    g.setColor(new Color(0, 0, 0, BG_ALPHA));
    g.fillRect(
        x - BORDER_PADDING,
        y - BORDER_PADDING,
        width + BORDER_PADDING * 2,
        height + BORDER_PADDING * 2);
  }

  private void drawHealthFill(Graphics g) {
    float t = Mathf.clamp(health.getHealth01(), 0f, 1f);
    Color c = Color.lerp(Color.RED, Color.GREEN, t);
    g.setColor(c);
    g.fillRect(x, y, width * t, height);
  }

  private void drawBorder(Graphics g) {
    g.setColor(Color.WHITE);
    g.drawRect(x, y, width, height);
  }
}
