package engine.collision.collider;

import math.Vector3f;

public class AABBCollider extends AbstractCollider {

  private final Vector3f halfExtents;

  public AABBCollider(Vector3f halfExtents) {
    this.halfExtents = halfExtents;
  }

  public Vector3f getHalfExtents() {
    return halfExtents;
  }

  @Override
  public ColliderType getType() {
    return ColliderType.AABB;
  }
}
