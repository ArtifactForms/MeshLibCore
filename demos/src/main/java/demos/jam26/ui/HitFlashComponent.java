package demos.jam26.ui;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.render.Graphics;
import math.Color;
import math.Mathf;

/** Fullscreen red flash effect used as player hit feedback. */
public class HitFlashComponent extends AbstractComponent implements RenderableComponent {

  /** Intensity units per second. */
  private static final float DECAY_SPEED = 2.5f;

  /** [0..1] */
  private float intensity = 0f;

  /**
   * Triggers a hit flash.
   *
   * @param strength value in range [0..1]
   */
  public void trigger(float strength) {
    intensity = Mathf.clamp01(intensity + strength);
  }

  @Override
  public void update(float deltaTime) {
    if (intensity <= 0f) return;

    intensity -= DECAY_SPEED * deltaTime;
    intensity = Mathf.clamp01(intensity);
  }

  @Override
  public void render(Graphics g) {
    if (intensity <= 0f) return;

    g.setColor(new Color(1f, 0f, 0f, intensity));
    g.fillRect(0, 0, g.getWidth(), g.getHeight());
  }
}
