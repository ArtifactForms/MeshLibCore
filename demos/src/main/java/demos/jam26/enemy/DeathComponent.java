package demos.jam26.enemy;

import engine.Timer;
import engine.components.AbstractComponent;
import math.Mathf;
import math.Vector3f;

public class DeathComponent extends AbstractComponent {

  private enum State {
    NONE,
    HIT_STOP,
    COLLAPSE
  }

  private State state = State.NONE;

  private float timer = 0f;

  private final Timer time;
  private Vector3f baseScale;
  private Vector3f fallDir;

  public DeathComponent(Timer time) {
    this.time = time;
  }

  @Override
  public void onAttach() {
    baseScale = getOwner().getTransform().getScale();
  }

  public void die(Vector3f hitDir) {
    if (state != State.NONE) return;

    // small freeze
    time.setTimeScale(0f);
    timer = 0.05f;

    fallDir = hitDir.normalize().mult(20f);

    state = State.HIT_STOP;
  }

  @Override
  public void onUpdate(float tpf) {

    switch (state) {

      case HIT_STOP -> {
        timer -= time.getUnscaledTimePerFrame();

        // squash
        float s = 1.2f;
        getOwner().getTransform().setScale(
            baseScale.x * s,
            baseScale.y * 0.8f,
            baseScale.z * s
        );

        if (timer <= 0f) {
          time.setTimeScale(1f);
          timer = 0.25f;
          state = State.COLLAPSE;
        }
      }

      case COLLAPSE -> {
        timer -= tpf;

        float t = 1f - (timer / 0.25f);
        t = Mathf.clamp01(t);

        // sink + flatten
        float yScale = Mathf.lerp(1f, 0.1f, t);
        getOwner().getTransform().setScale(
            baseScale.x,
            baseScale.y * yScale,
            baseScale.z
        );

        // knockback
        getOwner().getTransform().translate(
            fallDir.x * tpf,
            -30f * tpf,
            fallDir.z * tpf
        );

        if (timer <= 0f) {
            getOwner().destroy();
          state = State.NONE;
        }
      }
    }
  }
}
