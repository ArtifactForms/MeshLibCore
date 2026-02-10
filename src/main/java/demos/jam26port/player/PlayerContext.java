package demos.jam26port.player;

import engine.scene.camera.PerspectiveCamera;
import math.Vector3f;

/**
 * PlayerContext is a small, explicit bundle of references that define "the player" from a gameplay
 * perspective.
 *
 * <p>It is NOT a system, NOT global state, and NOT an engine concept. It exists purely to wire
 * gameplay components together without hidden lookups or tight coupling.
 */
public final class PlayerContext {

  private final HealthComponent health;
  private final PerspectiveCamera camera;

  public PlayerContext(HealthComponent health, PerspectiveCamera camera) {
    this.health = health;
    this.camera = camera;
  }

  public HealthComponent getHealth() {
    return health;
  }

  public Vector3f getPosition() {
    return camera.getTransform().getPosition();
  }

  public void setPosition(Vector3f position) {
    camera.getTransform().setPosition(position);
  }

  public boolean canReceiveHealth() {
    return health.getCurrentHealth() < health.getMaxHealth();
  }
}
