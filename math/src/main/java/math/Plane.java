package math;

/**
 * Represents a geometric plane in 3D space using the equation:
 *
 * <pre>
 *   dot(normal, point) + distance = 0
 * </pre>
 *
 * The normal vector is always normalized. This class is designed for high-performance use cases
 * such as frustum culling and collision tests.
 */
public final class Plane {

  /** Normalized plane normal */
  private final Vector3f normal = new Vector3f();

  /** Plane distance (D) in the plane equation */
  private float distance;

  /** Creates an uninitialized plane (normal = 0, distance = 0). */
  public Plane() {
    // intentionally empty
  }

  /** Creates a plane from a normal and distance. The normal is automatically normalized. */
  public Plane(Vector3f normal, float distance) {
    set(normal, distance);
  }

  /**
   * Sets the plane using raw plane coefficients.
   *
   * <p>The plane equation is:
   *
   * <pre>
   *   Ax + By + Cz + D = 0
   * </pre>
   *
   * The normal (A,B,C) is normalized internally.
   */
  public void set(float a, float b, float c, float d) {
    float length = (float) Math.sqrt(a * a + b * b + c * c);
    if (length == 0f) {
      throw new IllegalArgumentException("Plane normal cannot be zero.");
    }

    float invLen = 1.0f / length;
    normal.set(a * invLen, b * invLen, c * invLen);
    distance = d * invLen;
  }

  /** Sets the plane from a normal vector and distance. The normal is normalized internally. */
  public void set(Vector3f n, float d) {
    float len = n.length();
    if (len == 0f) {
      throw new IllegalArgumentException("Plane normal cannot be zero.");
    }

    float invLen = 1.0f / len;
    normal.set(n).multLocal(invLen);
    distance = d * invLen;
  }
  
  /**
   * Sets the plane from three points in counter-clockwise order.
   *
   * @param a first point on the plane
   * @param b second point on the plane
   * @param c third point on the plane
   */
  public void setFromPoints(Vector3f a, Vector3f b, Vector3f c) {
      // Vectors lying on the plane
      Vector3f ab = b.subtract(a);
      Vector3f ac = c.subtract(a);

      // Normal is cross product of two edges
      Vector3f n = ab.cross(ac).normalizeLocal();

      // Set plane normal
      normal.set(n);

      // Plane equation: dot(normal, point) + distance = 0
      distance = -normal.dot(a);
  }

  /**
   * Flips the plane orientation. After calling this method, the plane represents the same geometric
   * plane but with inverted facing.
   */
  public void flip() {
    normal.negateLocal();
    distance = -distance;
  }

  /**
   * Computes the signed distance from a point to the plane.
   *
   * <p>> 0 : point is in front of the plane = 0 : point lies on the plane < 0 : point is behind the
   * plane
   */
  public float distanceToPoint(Vector3f point) {
    return normal.dot(point) + distance;
  }

  /**
   * Copies the plane normal into the provided vector. This method avoids allocations and is safe
   * for hot paths.
   */
  public Vector3f getNormal(Vector3f out) {
    return out.set(normal);
  }

  public Vector3f getNormal() {
    return normal;
  }

  /** @return the plane distance (D) */
  public float getDistance() {
    return distance;
  }

  @Override
  public String toString() {
    return "Plane [normal=" + normal + ", distance=" + distance + "]";
  }
}
