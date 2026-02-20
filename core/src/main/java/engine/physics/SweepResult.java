package engine.physics;

import engine.physics.collision.component.ColliderComponent;
import math.Vector3f;

public final class SweepResult {

  private final boolean hit;
  private final float toi; // Time of impact
  private final Vector3f point;
  private final Vector3f normal;
  private final ColliderComponent collider;

  public SweepResult(
      boolean hit, float toi, Vector3f point, Vector3f normal, ColliderComponent collider) {

    this.hit = hit;
    this.toi = toi;
    this.point = point;
    this.normal = normal;
    this.collider = collider;
  }

  public static SweepResult noHit() {
    return new SweepResult(false, 1f, null, null, null);
  }

  public boolean hasHit() {
    return hit;
  }

  public float getTOI() {
    return toi;
  }

  public Vector3f getPoint() {
    return point;
  }

  public Vector3f getNormal() {
    return normal;
  }

  public ColliderComponent getCollider() {
    return collider;
  }
}
