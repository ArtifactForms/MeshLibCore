package math;

/**
 * Represents a ray in 3D space, defined by an origin point and a direction vector.
 *
 * <p>A ray is a mathematical abstraction used in 3D graphics, physics simulations, and
 * computational geometry. It extends infinitely from its origin in the specified direction. The
 * direction vector is automatically normalized during initialization to ensure consistent
 * calculations.
 */
public class Ray3f {

  /** The starting point of the ray. */
  private final Vector3f origin;

  /** The normalized direction vector of the ray. */
  private final Vector3f direction;

  /** The reciprocal of the direction vector, used for optimized ray-box intersection. */
  private final Vector3f directionInv;

  /**
   * Constructs a new {@code Ray3f} with the given origin and direction.
   *
   * <p>The direction vector will be normalized internally to ensure correctness in calculations.
   * Both {@code origin} and {@code direction} must be non-null.
   *
   * @param origin The starting point of the ray (non-null)
   * @param direction The direction vector of the ray (non-null, normalized internally)
   * @throws IllegalArgumentException if either {@code origin} or {@code direction} is null
   */
  public Ray3f(Vector3f origin, Vector3f direction) {
    if (origin == null) {
      throw new IllegalArgumentException("Origin cannot be null.");
    }
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null.");
    }
    this.origin = new Vector3f(origin);
    this.direction = new Vector3f(direction);
    this.direction.normalizeLocal();
    this.directionInv = direction.reciprocal();
  }

  /**
   * Computes the shortest distance from the ray to a point in 3D space.
   *
   * <p>This method calculates the perpendicular distance from the given point to the ray. If the
   * point lies behind the origin of the ray, the distance from the point to the ray's origin is
   * returned. If the point lies along the ray's path (in the direction of the ray), the
   * perpendicular distance is calculated. The ray extends infinitely in the positive direction from
   * its origin.
   *
   * @param point The point in 3D space to compute the distance to (non-null).
   * @return The shortest distance from the ray to the point. If the point is behind the ray's
   *     origin, the distance from the point to the origin is returned.
   * @throws IllegalArgumentException if the point is null.
   */
  public float distanceToPoint(Vector3f point) {
    if (point == null) {
      throw new IllegalArgumentException("Point cannot be null.");
    }

    // Calculate vector from ray origin to the point
    Vector3f toPoint = point.subtract(origin);

    // Project the vector to the ray's direction (dot product normalized)
    float projection = toPoint.dot(direction);

    // If the projection is negative, the point is behind the ray's origin
    if (projection < 0) {
      // Return the distance from the origin to the point
      return toPoint.length();
    }

    // Return the perpendicular distance (calculate as the distance from the point to the closest
    // point on the ray)
    Vector3f closestPoint = getPointAt(projection);
    return closestPoint.subtract(point).length();
  }

  /**
   * Computes the point along the ray at a given parameter {@code t}.
   *
   * <p>The formula for the point is:
   *
   * <pre>{@code
   * point = origin + t * direction
   * }</pre>
   *
   * where {@code t} is a scalar representing the distance along the ray from the origin. Positive
   * values of {@code t} will give points in the direction the ray is pointing, while negative
   * values will give points in the opposite direction.
   *
   * @param t The parameter along the ray (can be negative, zero, or positive).
   * @return The point at parameter {@code t}.
   */
  public Vector3f getPointAt(float t) {
    return origin.add(direction.mult(t));
  }

  /**
   * Returns the origin of the ray.
   *
   * <p>The origin is the starting point from which the ray emanates.
   *
   * @return The origin of the ray.
   */
  public Vector3f getOrigin() {
    return new Vector3f(origin);
  }

  /**
   * Returns the normalized direction vector of the ray.
   *
   * <p>The direction vector defines the direction in which the ray travels. The vector is
   * normalized, ensuring consistent calculations for operations like intersections.
   *
   * @return The direction vector of the ray.
   */
  public Vector3f getDirection() {
    return new Vector3f(direction);
  }

  /**
   * Returns the reciprocal of the direction vector of the ray.
   *
   * <p>The reciprocal of the direction vector is precomputed to optimize ray-box intersection
   * tests, where division by components of the direction vector is required.
   *
   * @return The reciprocal of the direction vector of the ray.
   */
  public Vector3f getDirectionInv() {
    return new Vector3f(directionInv);
  }
}
