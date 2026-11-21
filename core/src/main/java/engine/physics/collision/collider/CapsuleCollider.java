package engine.physics.collision.collider;

import math.Vector3f;

/**
 * Represents a vertically aligned capsule collider.
 *
 * <p>The capsule consists of a cylindrical segment and two spherical caps. The half height defines
 * half the distance between the centers of the spherical caps, not half of the total capsule
 * height.
 */
public final class CapsuleCollider extends AbstractCollider {

  private final float radius;

  private final float halfHeight;

  /**
   * Creates a capsule collider.
   *
   * @param radius the radius of the spherical caps
   * @param halfHeight half the distance between the centers of the spherical caps
   * @throws IllegalArgumentException if either value is negative
   */
  public CapsuleCollider(float radius, float halfHeight) {
    if (radius < 0) {
      throw new IllegalArgumentException("Radius cannot be negative.");
    }
    if (halfHeight < 0) {
      throw new IllegalArgumentException("Half height cannot be negative.");
    }
    this.radius = radius;
    this.halfHeight = halfHeight;
  }

  /**
   * Calculates the world-space centers of the two spherical caps.
   *
   * @param center the center of the capsule
   * @param outA output vector for the upper sphere center
   * @param outB output vector for the lower sphere center
   */
  public void getSegment(Vector3f center, Vector3f outA, Vector3f outB) {
    outA.set(center.x, center.y - halfHeight, center.z);
    outB.set(center.x, center.y + halfHeight, center.z);
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
}
