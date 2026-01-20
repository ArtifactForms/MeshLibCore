package math;

/**
 * The {@code Bounds} class represents a 3D axis-aligned bounding box (AABB), defined by two {@link
 * Vector3f} points: the minimum and maximum corners. This class provides various utility methods
 * for manipulating and querying the bounding box, such as checking if a point is contained within
 * the bounds, expanding the bounds, and testing for intersections with rays or other bounds.
 *
 * <p>A bounding box is often used in 3D graphics for collision detection, frustum culling, and
 * other spatial queries.
 */
public class Bounds {

  /** The minimum corner of the bounding box. */
  private Vector3f min;

  /** The maximum corner of the bounding box. */
  private Vector3f max;

  /**
   * Constructs a new {@code Bounds} object with the specified minimum and maximum corners.
   *
   * @param min the minimum corner of the bounding box
   * @param max the maximum corner of the bounding box
   * @throws IllegalArgumentException if either {@code min} or {@code max} is {@code null}
   */
  public Bounds(Vector3f min, Vector3f max) {
    if (min == null) {
      throw new IllegalArgumentException("Min cannot be null.");
    }
    if (max == null) {
      throw new IllegalArgumentException("Max cannot be null.");
    }
    this.min = new Vector3f(min);
    this.max = new Vector3f(max);
  }

  /**
   * Returns the closest point on the bounding box to the given {@code point}. The closest point is
   * determined by clamping each coordinate of the point between the minimum and maximum bounds of
   * the box.
   *
   * @param point the point to clamp to the bounding box
   * @return a new {@code Vector3f} representing the closest point on the bounding box
   */
  public Vector3f closestPoint(Vector3f point) {
    float x = Math.max(min.x, Math.min(max.x, point.x));
    float y = Math.max(min.y, Math.min(max.y, point.y));
    float z = Math.max(min.z, Math.min(max.z, point.z));
    return new Vector3f(x, y, z);
  }

  /**
   * Checks if the given {@code point} is inside the bounding box. The point is considered inside if
   * all of its coordinates are between the minimum and maximum coordinates of the box.
   *
   * @param p the point to check
   * @return {@code true} if the point is inside the bounding box, {@code false} otherwise
   */
  public boolean contains(Vector3f p) {
    return p.x >= min.x
        && p.x <= max.x
        && p.y >= min.y
        && p.y <= max.y
        && p.z >= min.z
        && p.z <= max.z;
  }

  /**
   * Expands the bounding box to encompass the given {@code point}. If the point is outside the
   * current bounds, the box will be enlarged to include it.
   *
   * @param p the point to include in the bounding box
   */
  public void encapsulate(Vector3f p) {
    min.set(Math.min(min.x, p.x), Math.min(min.y, p.y), Math.min(min.z, p.z));
    max.set(Math.max(max.x, p.x), Math.max(max.y, p.y), Math.max(max.z, p.z));
  }

  /**
   * Expands the bounding box by the given {@code amount}. The expansion is done uniformly along all
   * axes, increasing the size of the bounding box by the specified amount.
   *
   * @param amount the amount to expand the bounding box by
   */
  public void expand(float amount) {
    float halfAmount = amount / 2;
    min.subtractLocal(halfAmount, halfAmount, halfAmount);
    max.addLocal(halfAmount, halfAmount, halfAmount);
  }

  /**
   * Tests whether the given ray intersects this axis-aligned bounding box (AABB).
   *
   * <p>This method performs a standard slab-based ray–AABB intersection test. The ray is tested
   * against the three axis-aligned slabs (x, y, z), and an intersection interval {@code [tmin,
   * tmax]} is computed incrementally.
   *
   * <p>The method is designed for real-time use cases such as object picking and visibility tests.
   * It correctly handles:
   *
   * <ul>
   *   <li>Rays starting inside the box
   *   <li>Rays starting outside and intersecting the box
   *   <li>Rays pointing away from the box
   *   <li>Negative ray directions
   *   <li>Rays parallel to one or more box faces
   * </ul>
   *
   * <p>Limitations and design decisions:
   *
   * <ul>
   *   <li>Rays with a zero-length direction vector are considered invalid and will never intersect.
   *   <li>Degenerate boxes (where {@code min == max}) are treated as a single point and are only
   *       intersected if the ray origin lies exactly on that point.
   *   <li>Grazing intersections (exact edge or corner contact) are not guaranteed to be detected in
   *       all floating-point configurations.
   * </ul>
   *
   * <p>This behavior is intentional and suitable for visual picking and gameplay logic, where
   * robustness and performance are preferred over exhaustive geometric edge-case handling.
   *
   * @param ray the {@link Ray3f} to test for intersection; must have a non-zero direction vector
   * @return {@code true} if the ray intersects this bounding box, {@code false} otherwise
   */
  public boolean intersectsRay(Ray3f ray) {
    return intersectRayDistance(ray) != null;
  }

  /**
   * Computes the distance along the ray at which it first intersects this axis-aligned bounding box
   * (AABB).
   *
   * <p>The returned value {@code t} represents the parameter in the ray equation {@code P(t) =
   * origin + direction * t}. A positive value indicates an intersection in front of the ray origin.
   *
   * <p>If the ray starts inside the box, the distance to the exit point ({@code tmax}) is returned.
   *
   * <p>If the ray does not intersect the box, or if the entire box lies behind the ray origin, this
   * method returns {@code null}.
   *
   * <p>Special cases:
   *
   * <ul>
   *   <li>Rays with a zero-length direction return {@code null}.
   *   <li>Parallel rays outside the corresponding slab return {@code null}.
   *   <li>Degenerate boxes are only intersected if the ray origin lies exactly on the box point.
   * </ul>
   *
   * @param ray the {@link Ray3f} to test; must have a non-zero direction
   * @return the distance to the first intersection point, or {@code null} if no intersection occurs
   */
  public Float intersectRayDistance(Ray3f ray) {
    Vector3f o = ray.getOrigin();
    Vector3f d = ray.getDirection();

    if (d.isZero()) return null;

    float tmin = Float.NEGATIVE_INFINITY;
    float tmax = Float.POSITIVE_INFINITY;

    for (int i = 0; i < 3; i++) {
      float origin = o.get(i);
      float dir = d.get(i);
      float minB = min.get(i);
      float maxB = max.get(i);

      if (Math.abs(dir) < 1e-6f) {
        // Ray parallel to slab
        if (origin < minB || origin > maxB) {
          return null;
        }
      } else {
        float invD = 1.0f / dir;
        float t1 = (minB - origin) * invD;
        float t2 = (maxB - origin) * invD;

        if (t1 > t2) {
          float tmp = t1;
          t1 = t2;
          t2 = tmp;
        }

        tmin = Math.max(tmin, t1);
        tmax = Math.min(tmax, t2);

        if (tmin > tmax) return null;
      }
    }

    // If box is behind ray
    if (tmax < 0) return null;

    // If inside box → tmin < 0 → use tmax
    return tmin >= 0 ? tmin : tmax;
  }

  /**
   * Tests if this bounding box intersects another {@code Bounds}. The intersection is checked by
   * comparing the min and max coordinates of both boxes.
   *
   * @param other the other bounding box to check for intersection
   * @return {@code true} if the bounding boxes intersect, {@code false} otherwise
   */
  public boolean intersects(Bounds other) {
    return min.x <= other.max.x
        && max.x >= other.min.x
        && min.y <= other.max.y
        && max.y >= other.min.y
        && min.z <= other.max.z
        && max.z >= other.min.z;
  }

  /**
   * Calculates the squared distance from the given {@code point} to the closest point on the
   * bounding box. This method avoids calculating the square root for performance reasons, returning
   * the squared distance instead.
   *
   * @param point the point to calculate the squared distance from
   * @return the squared distance from the point to the closest point on the bounding box
   */
  public float sqrDistance(Vector3f point) {
    float dx = Math.max(0, Math.max(min.x - point.x, point.x - max.x));
    float dy = Math.max(0, Math.max(min.y - point.y, point.y - max.y));
    float dz = Math.max(0, Math.max(min.z - point.z, point.z - max.z));
    return dx * dx + dy * dy + dz * dz;
  }

  /**
   * Sets the minimum and maximum corners of the bounding box to the specified values.
   *
   * @param min the new minimum corner
   * @param max the new maximum corner
   * @throws IllegalArgumentException if min or max is null.
   */
  public void setMinMax(Vector3f min, Vector3f max) {
    if (min == null || max == null) {
      throw new IllegalArgumentException("Min and Max cannot be null.");
    }
    this.min = new Vector3f(min);
    this.max = new Vector3f(max);
  }

  /**
   * Returns the minimum corner of the bounding box.
   *
   * @return the minimum corner of the bounding box
   */
  public Vector3f getMin() {
    return min;
  }

  /**
   * Returns the maximum corner of the bounding box.
   *
   * @return the maximum corner of the bounding box
   */
  public Vector3f getMax() {
    return max;
  }

  /** @return the size of the bounds along the x-axis */
  public float getWidth() {
    return max.x - min.x;
  }

  /** @return the size of the bounds along the y-axis */
  public float getHeight() {
    return max.y - min.y;
  }

  /** @return the size of the bounds along the z-axis */
  public float getDepth() {
    return max.z - min.z;
  }

  /** @return the size of the bounding box on all axes */
  public Vector3f getSize() {
    return new Vector3f(max.x - min.x, max.y - min.y, max.z - min.z);
  }

  /**
   * Returns a string representation of the Bounds object. The string includes the minimum and
   * maximum points of the bounds.
   *
   * @return a string representation of this Bounds object in the format: "Bounds [min=<min>,
   *     max=<max>]".
   */
  @Override
  public String toString() {
    return "Bounds [min=" + min + ", max=" + max + "]";
  }
}
