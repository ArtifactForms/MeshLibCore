package math;

public class GeometryUtil {

  /**
   * Calculates the centroid (center of mass) of a triangle defined by three points.
   *
   * <p>The centroid is computed as the average of the x, y, and z coordinates of the three
   * vertices.
   *
   * @param a The first vertex of the triangle.
   * @param b The second vertex of the triangle.
   * @param c The third vertex of the triangle.
   * @return The centroid of the triangle as a new Vector3f.
   */
  public static Vector3f calculateCentroid(Vector3f a, Vector3f b, Vector3f c) {
    float x = (a.x + b.x + c.x) / 3f;
    float y = (a.y + b.y + c.y) / 3f;
    float z = (a.z + b.z + c.z) / 3f;
    return new Vector3f(x, y, z);
  }

  /**
   * Calculates the centroid (or center of mass) of a triangle defined by three 2D points.
   *
   * @param a The first point of the triangle.
   * @param b The second point of the triangle.
   * @param c The third point of the triangle.
   * @return The centroid of the triangle.
   */
  public static Vector2f getMainEmphasis(Vector2f a, Vector2f b, Vector2f c) {
    Vector2f m = a.add(b).add(c).mult(Mathf.ONE_THIRD);
    return m;
  }

  /**
   * Calculates the center point of a quadrilateral defined by four 2D points.
   *
   * @param a The first point of the quadrilateral.
   * @param b The second point of the quadrilateral.
   * @param c The third point of the quadrilateral.
   * @param d The fourth point of the quadrilateral.
   * @return The center point of the quadrilateral.
   */
  public static Vector2f calculateCenter(Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
    float x = (a.x + b.x + c.x + d.x) * 0.25f;
    float y = (a.y + b.y + c.y + d.y) * 0.25f;
    return new Vector2f(x, y);
  }

  /**
   * Calculates the center point of a quadrilateral defined by four 3D points.
   *
   * @param a The first point of the quadrilateral.
   * @param b The second point of the quadrilateral.
   * @param c The third point of the quadrilateral.
   * @param d The fourth point of the quadrilateral.
   * @return The center point of the quadrilateral.
   */
  public static Vector3f calculateCenter(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
    float x = (a.x + b.x + c.x + d.x) * 0.25f;
    float y = (a.y + b.y + c.y + d.y) * 0.25f;
    float z = (a.z + b.z + c.z + d.z) * 0.25f;
    return new Vector3f(x, y, z);
  }

  /**
   * Calculates the midpoint between two 3D points.
   *
   * @param start The starting point.
   * @param end The ending point.
   * @return The midpoint between the two points.
   */
  public static Vector3f getMidpoint(Vector3f start, Vector3f end) {
    return start.add(end).mult(0.5f);
  }

  /**
   * Calculates the midpoint between two 2D points.
   *
   * @param start The starting point.
   * @param end The ending point.
   * @return The midpoint between the two points.
   */
  public static Vector2f getMidpoint(Vector2f start, Vector2f end) {
    return start.add(end).mult(0.5f);
  }

  /**
   * Calculates the angle in radians between two 3D vectors.
   *
   * @param v1 The first vector.
   * @param v2 The second vector.
   * @return The angle between the two vectors in radians.
   * @throws IllegalArgumentException if either vector has zero length.
   */
  public static double angleBetweenVectors(Vector3f v1, Vector3f v2) {
    double dotProduct = v1.dot(v2);
    double magnitude1 = v1.length();
    double magnitude2 = v2.length();

    if (magnitude1 == 0 || magnitude2 == 0) {
      throw new IllegalArgumentException("Vectors cannot have zero length");
    }

    // Handle floating-point precision issues
    double cosTheta = Math.max(-1.0, Math.min(1.0, dotProduct / (magnitude1 * magnitude2)));

    double angle = Math.acos(cosTheta);

    return angle;
  }

  /**
   * Calculates a point along the line segment between two 2D points.
   *
   * @param start The starting point of the line segment.
   * @param end The ending point of the line segment.
   * @param lambda A value between 0 and 1 that determines the position of the point along the line
   *     segment. A value of 0 will return the start point, a value of 1 will return the end point,
   *     and values between 0 and 1 will return points in between.
   * @return The point along the line segment.
   */
  public static Vector2f getDistributionPoint(Vector2f start, Vector2f end, float lambda) {
    float scalar = 1f / (1f + lambda);
    return start.add(end.mult(lambda)).mult(scalar);
  }

  /**
   * Calculates a point on a circle given a center point, radius, angle, and direction.
   *
   * @param center The center point of the circle.
   * @param radius The radius of the circle.
   * @param angle The angle in radians.
   * @param cw If true, the angle is measured clockwise from the positive x-axis. If false, the
   *     angle is measured counterclockwise.
   * @return A point on the circle.
   */
  public static Vector2f pointOnCircle(Vector2f center, float radius, float angle, boolean cw) {
    angle = cw ? angle : -angle;
    float x = (float) (center.x + radius * Math.cos(angle));
    float y = (float) (center.y + radius * Math.sin(angle));
    return new Vector2f(x, y);
  }

  /**
   * Computes an arbitrary vector orthogonal to the given vector.
   *
   * @param vector The input vector to find an orthogonal vector to.
   * @return A vector orthogonal to the provided vector.
   */
  public static Vector3f getOrthogonalVector(Vector3f vector) {
    // Choose an arbitrary vector that isn't parallel to the input vector
    Vector3f arbitrary = Math.abs(vector.x) < 0.9 ? new Vector3f(1, 0, 0) : new Vector3f(0, 1, 0);

    // Compute a cross product to ensure orthogonality
    return vector.cross(arbitrary).normalize();
  }
}
