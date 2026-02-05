package engine.collision.collider;

import math.Vector3f;

public final class CapsuleCollider extends AbstractCollider {

  private final float radius;
  private final float halfHeight;

  public CapsuleCollider(float radius, float halfHeight) {
    this.radius = radius;
    this.halfHeight = halfHeight;
  }

  public float getRadius() {
    return radius;
  }

  public float getHalfHeight() {
    return halfHeight;
  }

  @Override
  public ColliderType getType() {
    return ColliderType.CAPSULE;
  }

  /** World-space centers of the two sphere caps. */
  public void getSegment(Vector3f center, Vector3f outA, Vector3f outB) {
    outA.set(center.x, center.y - halfHeight, center.z);
    outB.set(center.x, center.y + halfHeight, center.z);
  }
}
