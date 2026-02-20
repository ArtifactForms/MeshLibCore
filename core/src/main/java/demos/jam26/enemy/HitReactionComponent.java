package demos.jam26.enemy;

import demos.jam26.assets.AssetRefs;
import demos.ray.RaycastHit;
import engine.components.AbstractComponent;
import engine.scene.audio.SoundManager;
import math.Mathf;
import math.Vector3f;

public class HitReactionComponent extends AbstractComponent {

  // Hit scale reaction
  private float timer = 0f;
  private final float duration = 0.1f;

  private Vector3f baseScale;
  private boolean wasHit;

  @Override
  public void onAttach() {
    baseScale = getOwner().getTransform().getScale();
  }

  public void hit(RaycastHit hit) {
    // restart scale punch
    timer = duration;

    if (!wasHit) {
      SoundManager.playEffect(AssetRefs.SOUND_ENEMY_HIT_SHRIEK_KEY);
      wasHit = true;
    }

    DeathAnimationComponent animation = getOwner().getComponent(DeathAnimationComponent.class);
    if (animation != null) {
      animation.die();
    }
  }

  @Override
  public void onUpdate(float tpf) {
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
