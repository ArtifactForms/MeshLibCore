package engine.physics.collision.contact;

import math.Vector3f;

/**
 * Represents a collision contact between two colliders.
 *
 * <p>A {@code Contact} contains the minimal information required to resolve an intersection between
 * two objects in the collision system.
 *
 * <ul>
 *   <li>The {@code normal} defines the direction in which object A must be moved to separate it
 *       from object B.
 *   <li>The {@code penetrationDepth} specifies how deeply the two objects overlap along the contact
 *       normal.
 * </ul>
 *
 * <p>The contact normal is defined to point <b>from collider A to collider B</b>. This convention
 * must be used consistently throughout the collision and resolution system.
 *
 * <p>The penetration depth is guaranteed to be greater than or equal to zero and represents the
 * minimal translation distance required to resolve the collision along the contact normal.
 */
public final class Contact {

  /**
   * The collision normal pointing from collider A to collider B. Used for collision response and
   * positional correction.
   */
  private final Vector3f normal;

  /** The depth of penetration along the contact normal. Always {@code >= 0}. */
  private final float penetrationDepth;

  /**
   * Creates a new collision contact.
   *
   * @param normal the contact normal pointing from collider A to collider B
   * @param penetrationDepth the penetration depth along the normal (must be {@code >= 0})
   */
  public Contact(Vector3f normal, float penetrationDepth) {
    this.normal = normal;
    this.penetrationDepth = penetrationDepth;
  }

  public Contact inverted() {
    return new Contact(normal.negate(), penetrationDepth);
  }

  /**
   * Returns the contact normal.
   *
   * @return the collision normal pointing from collider A to collider B
   */
  public Vector3f getNormal() {
    return normal;
  }

  /**
   * Returns the penetration depth of the collision.
   *
   * @return the penetration depth (always {@code >= 0})
   */
  public float getPenetrationDepth() {
    return penetrationDepth;
  }
}
