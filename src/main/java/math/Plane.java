package math;

/**
 * Represents a geometric plane in 3D space defined by a normal vector and a distance from the
 * origin.
 *
 * <p>The equation of the plane is represented as: <br>
 * <code>Ax + By + Cz + D = 0</code>, where:
 *
 * <ul>
 *   <li><code>(A, B, C)</code> is the normalized normal vector of the plane.
 *   <li><code>D</code> is the distance of the plane from the origin, along the normal direction.
 * </ul>
 */
public class Plane {

  /** The normal vector of the plane, representing its orientation. */
  private Vector3f normal;

  /** The distance of the plane from the origin along the normal vector. */
  private float distance;

  /**
   * Constructs a plane with a default normal vector (0, 0, 0) and a distance of 0.
   *
   * <p>The resulting plane is uninitialized and must be configured using the {@link #set(float,
   * float, float, float)} method.
   */
  public Plane() {
    this.normal = new Vector3f();
    this.distance = 0;
  }

  /**
   * Sets the plane parameters using its coefficients.
   *
   * <p>The coefficients (A, B, C, D) define the plane equation <code>Ax + By + Cz + D = 0</code>.
   * The normal vector is automatically normalized during this operation.
   *
   * @param a the x-component of the normal vector.
   * @param b the y-component of the normal vector.
   * @param c the z-component of the normal vector.
   * @param d the distance from the origin along the plane's normal vector.
   */
  public void set(float a, float b, float c, float d) {
    this.normal.set(a, b, c);
    normal.normalizeLocal();
    this.distance = d;
  }

  /**
   * Calculates the signed distance from a given point to the plane.
   *
   * <p>The signed distance is computed as: <br>
   * <code>distance = dot(normal, point) + D</code>, where:
   *
   * <ul>
   *   <li><code>normal</code> is the plane's normal vector.
   *   <li><code>point</code> is the 3D point to test.
   *   <li><code>D</code> is the distance parameter of the plane.
   * </ul>
   *
   * @param point the point to calculate the distance from.
   * @return the signed distance from the point to the plane. A positive value indicates the point
   *     is in the direction of the normal vector, and a negative value indicates it is on the
   *     opposite side.
   */
  public float distanceToPoint(Vector3f point) {
    return normal.dot(point) + distance;
  }

  /**
   * Gets the normal vector of the plane.
   *
   * @return the normal vector of the plane.
   */
  public Vector3f getNormal() {
    return normal;
  }

  /**
   * Gets the distance of the plane from the origin along its normal vector.
   *
   * @return the distance of the plane from the origin.
   */
  public float getDistance() {
    return distance;
  }
}
