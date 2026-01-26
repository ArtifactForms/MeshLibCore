package engine.demos.jam26.enemy;

import engine.Timer;
import engine.components.AbstractComponent;
import engine.demos.jam26.assets.AssetRefs;
import engine.demos.ray.RaycastHit;
import engine.scene.audio.SoundManager;
import math.Mathf;
import math.Vector3f;

public class HitReactionComponent extends AbstractComponent {

  // Hit reaction
  private float timer = 0f;
  private final float duration = 0.1f;

  // Hit stop
  private float hitStopTimer = 0f;
  private final float hitStopDuration = 0.04f;

  private final Timer time;
  private Vector3f baseScale;

  private boolean wasHit;

  public HitReactionComponent(Timer time) {
    this.time = time;
  }

  @Override
  public void onAttach() {
    baseScale = getOwner().getTransform().getScale();
  }

  public void hit(RaycastHit hit) {
    // Reset reaction
    timer = duration;
    hitStopTimer = hitStopDuration;

    if (!wasHit) {
      SoundManager.playEffect(AssetRefs.SOUND_ENEMY_HIT_SHRIEK_KEY);
      wasHit = true;
    }

    // Hit stop only if not already stopped
    if (time.getTimeScale() > 0f) {
      time.setTimeScale(0f);
    }

    DeathAnimationComponent animation = getOwner().getComponent(DeathAnimationComponent.class);
    if (animation != null) {
      animation.die();
    }
  }

  @Override
  public void onUpdate(float tpf) {

    // HIT STOP
    if (hitStopTimer > 0f) {
      hitStopTimer -= time.getUnscaledTimePerFrame();
      if (hitStopTimer <= 0f) {
        time.setTimeScale(1f);
      }
    }

    // HIT SCALE POP
    if (timer <= 0f) return;

    timer -= tpf;

    float t = 1f - (timer / duration);

    // Fast punch → smooth return
    float punch = Mathf.exp(-8f * t) * 0.5f;

    float s = 1f + punch;

    getOwner().getTransform().setScale(baseScale.x * s, baseScale.y * s, baseScale.z * s);

    if (timer <= 0f) {
      getOwner().getTransform().setScale(baseScale);
    }
  }
}
