package demos.jam26port.game.ui.health;

import math.Color;
import math.Mathf;
import workspace.ui.Graphics;

public class HealthBarViewImpl implements HealthBarView {

  private static final float BORDER_PADDING = 2f;
  private static final float BG_ALPHA = 0.5f;

  // shake tuning
  private static final float SHAKE_FREQUENCY = 20f; // oscillations speed (rad/sec)
  private static final float SHAKE_AMPLITUDE = 30f; // max pixels at full punch
  private static final float PUNCH_DECAY_SPEED = 2f; // energy per second

  // layout
  private float baseX = 20;
  private float baseY = 20;
  private float width = 200;
  private float height = 16;

  // animation state
  private float targetHealth = 1f;
  private float displayedHealth = 1f;

  // tuning
  private float smoothSpeed = 5f;

  // juice
  private float punch = 0f;
  private float shakeTime = 0f;
  private float shakeOffsetX = 0f;

  @Override
  public void setHealth01(float value) {
    value = Mathf.clamp(value, 0f, 1f);

    // only punch on damage
    if (value < targetHealth) {
      punch = Mathf.clamp01(punch + (targetHealth - value));
    }

    targetHealth = value;
  }

  @Override
  public void update(float tpf) {
    // smooth health bar
    displayedHealth += (targetHealth - displayedHealth) * smoothSpeed * tpf;

    // keep punch alive while smoothing is still catching up
    float smoothingError = Mathf.abs(targetHealth - displayedHealth);
    punch = Mathf.max(punch, smoothingError);

    // decay punch over time
    punch = Mathf.max(0f, punch - PUNCH_DECAY_SPEED * tpf);

    // shake motion
    shakeTime += tpf;
    shakeOffsetX = Mathf.sin(shakeTime * SHAKE_FREQUENCY) * punch * SHAKE_AMPLITUDE;
  }

  @Override
  public void render(Graphics g) {
    float x = baseX + shakeOffsetX;

    drawBackground(g, x);
    drawHealthFill(g, x);
    drawBorder(g, x);
  }

  private void drawBackground(Graphics g, float x) {
    g.setColor(new Color(0, 0, 0, BG_ALPHA));
    g.fillRect(
        x - BORDER_PADDING,
        baseY - BORDER_PADDING,
        width + BORDER_PADDING * 2,
        height + BORDER_PADDING * 2);
  }

  private void drawHealthFill(Graphics g, float x) {
    float t = displayedHealth;
    Color c = Color.lerp(Color.RED, Color.GREEN, t);
    g.setColor(c);
    g.fillRect(x, baseY, width * t, height);
  }

  private void drawBorder(Graphics g, float x) {
    g.setColor(Color.WHITE);
    g.drawRect(x, baseY, width, height);
  }
}
