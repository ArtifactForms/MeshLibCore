package engine.demos.jam26.enemy;

import engine.components.AbstractComponent;
import engine.demos.jam26.assets.AssetRefs;
import engine.scene.nodes.Billboard;
import math.Mathf;

public class DeathAnimationComponent extends AbstractComponent {

  private boolean dying = false;
  private float timer = 0f;

  private static final float DURATION = 0.5f;
  private static final int FRAME_COUNT = 7;

  public void die() {
    if (dying) return;
    dying = true;
    timer = DURATION;
  }

  @Override
  public void onUpdate(float tpf) {
    if (!dying) return;

    timer -= tpf;

    float t = 1f - (timer / DURATION);
    t = Mathf.clamp01(t);

    // FRAME CALC
    int frame = (int) (t * FRAME_COUNT);
    if (frame >= FRAME_COUNT) frame = FRAME_COUNT - 1;

    Billboard bb = (Billboard) getOwner();
    bb.setUvRect(AssetRefs.EYE_DEATH_FRAMES[frame]);

    // EXTRA JUICE
    // squash & stretch
    float s = 1f + Mathf.sin(t * Mathf.PI) * 0.3f;
    getOwner().getTransform().setScale(s, s, s);

    // spin-out
    getOwner().getTransform().rotate(0, 0, tpf * 8f);

    if (timer <= 0f) {
      // optional:
      // getOwner().destroy(); TODO Engine is missing this
      getOwner().getScene().removeNode(getOwner());
    }
  }
}
