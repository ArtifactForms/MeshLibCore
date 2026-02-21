package demos.jam26port.pickup;

import demos.jam26port.player.PlayerContext;
import engine.components.AbstractComponent;
import math.Mathf;
import math.Vector3f;

public abstract class PickupComponent extends AbstractComponent {

  protected float pickupRadius = 32f;
  protected boolean collected = false;

  protected final PlayerContext player;

  // juice
  private float popTime = 0.15f;
  private float timer = 0f;

  protected PickupComponent(PlayerContext player) {
    this.player = player;
  }

  @Override
  public void onUpdate(float tpf) {
    if (collected) {
      updateCollected(tpf);
      return;
    }

    Vector3f playerPos = player.getPosition();
    Vector3f pos = getOwner().getTransform().getPosition();

    if (pos.distanceSquared(playerPos) <= pickupRadius * pickupRadius) {
      if (canPickUp()) {
        onPickup();
      }
    }
  }

  protected void onPickup() {
    collected = true;
    timer = popTime;
    applyEffect();
  }

  protected void updateCollected(float tpf) {
    timer -= tpf;

    float t = 1f - (timer / popTime);
    float s = 1f + Mathf.sin(t * Mathf.PI) * 0.5f;

    getOwner().getTransform().setScale(s, s, s);
    getOwner().getTransform().rotate(0, tpf * 6f, 0);

    if (timer <= 0f) {
      getOwner().destroy();
    }
  }

  /** Game logic (health, ammo, key, etc.) */
  protected abstract void applyEffect();

  protected abstract boolean canPickUp();
}
