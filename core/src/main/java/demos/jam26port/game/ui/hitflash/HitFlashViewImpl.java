package demos.jam26port.game.ui.hitflash;

import math.Color;
import math.Mathf;
import workspace.ui.Graphics;

/** Fullscreen red flash effect used as player hit feedback. */
public class HitFlashViewImpl implements HitFlashView {

  /** Intensity units per second. */
  private static final float DECAY_SPEED = 2.5f;

  /** [0..1] */
  private float intensity = 0f;

  /**
   * Triggers a hit flash.
   *
   * @param strength value in range [0..1]
   */
  @Override
  public void trigger(float strength) {
    intensity = Mathf.clamp01(intensity + strength);
  }

  @Override
  public void update(float tpf) {
    if (intensity <= 0f) return;

    intensity -= DECAY_SPEED * tpf;
    intensity = Mathf.clamp01(intensity);
  }

  @Override
  public void render(Graphics g) {
    if (intensity <= 0f) return;

    g.setColor(new Color(1f, 0f, 0f, intensity));
    g.fillRect(0, 0, g.getWidth(), g.getHeight());
  }
}
