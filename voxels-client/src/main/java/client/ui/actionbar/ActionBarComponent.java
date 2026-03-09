package client.ui.actionbar;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.resources.Font;
import math.Color;

public class ActionBarComponent extends AbstractComponent
    implements RenderableComponent, ActionBarView {

  private String text = "";
  private float displayTimer = 0;
  private Font font;

  public ActionBarComponent() {
    // Initialize font once to save CPU cycles
    this.font = new Font("monogram-extended", 32, Font.PLAIN);
  }

  @Override
  public void render(Graphics g) {
    if (displayTimer <= 0 || text.isEmpty()) return;

    // Important: set font before x calculation
    g.setFont(font);

    float alpha = getAlpha();
    float x = (g.getWidth() - g.textWidth(text)) * 0.5f;
    float y = g.getHeight() - 110;

    // Render Shadow (Black with current alpha)
    g.setColor(new Color(0, 0, 0, alpha * 0.7f));
    g.text(text, x + 2, y + 2);

    // Render Main Text
    g.setColor(new Color(1, 1, 1, alpha));
    g.text(text, x, y);
  }

  private float getAlpha() {
    // Fades out linearly during the last second
    return Math.min(1.0f, displayTimer);
  }

  @Override
  public void onUpdate(float tpf) {
    if (displayTimer > 0) {
      displayTimer -= tpf;
    }
  }

  @Override
  public void display(String message, float duration) {
    this.text = message;
    this.displayTimer = duration;
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
