package engine.demos.jam26.pickup;

import engine.components.AbstractComponent;
import engine.scene.camera.Camera;
import math.Mathf;
import math.Vector3f;

public abstract class PickupComponent extends AbstractComponent {

  protected float pickupRadius = 32f;
  protected boolean collected = false;

  // juice
  private float popTime = 0.15f;
  private float timer = 0f;

  @Override
  public void onUpdate(float tpf) {
    if (collected) {
      updateCollected(tpf);
      return;
    }

    Camera cam = getOwner().getScene().getActiveCamera();
    if (cam == null) return;

    Vector3f p = cam.getTransform().getPosition();
    Vector3f pos = getOwner().getTransform().getPosition();

    if (pos.distanceSquared(p) <= pickupRadius * pickupRadius) {
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
