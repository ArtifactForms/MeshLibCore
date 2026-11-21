package engine.physics.collision.collider;

import math.Vector3f;

/**
 * Represents an axis-aligned bounding box collider.
 *
 * <p>The collider is defined by its half extents relative to its center.
 */
public class AABBCollider extends AbstractCollider {

  private final Vector3f halfExtents;

  /**
   * Creates an axis-aligned bounding box collider.
   *
   * @param halfExtents the half size of the box along each axis
   * @throws IllegalArgumentException if the half extents are invalid
   *                                  or any component is less than or equal to zero
   */
  public AABBCollider(Vector3f halfExtents) {
      if (!Vector3f.isValid(halfExtents)) {
          throw new IllegalArgumentException("Half extents must be a valid finite vector.");
      }

      if (halfExtents.x <= 0 || halfExtents.y <= 0 || halfExtents.z <= 0) {
          throw new IllegalArgumentException("All half extents must be greater than zero.");
      }

      this.halfExtents = new Vector3f(halfExtents);
  }

  /**
   * Returns a copy of the half extents.
   *
   * @return a new vector containing the half extents
   */
  public Vector3f getHalfExtents() {
    return new Vector3f(halfExtents);
  }

  /**
   * Copies the half extents into the provided vector.
   *
   * @param store the vector that receives the half extents
   * @return the provided vector
   * @throws IllegalArgumentException if store is null
   */
  public Vector3f getHalfExtents(Vector3f store) {
    if (store == null) {
      throw new IllegalArgumentException("Store cannot be null.");
    }
    store.set(halfExtents);
    return store;
  }

  @Override
  public ColliderType getType() {
    return ColliderType.AABB;
  }
}
