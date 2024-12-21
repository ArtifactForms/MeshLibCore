package math;

/**
 * The {@code Bounds} class represents a 3D axis-aligned bounding box (AABB),
 * defined by two {@link Vector3f} points: the minimum and maximum corners. This
 * class provides various utility methods for manipulating and querying the
 * bounding box, such as checking if a point is contained within the bounds,
 * expanding the bounds, and testing for intersections with rays or other
 * bounds.
 * 
 * <p>
 * A bounding box is often used in 3D graphics for collision detection, frustum
 * culling, and other spatial queries.
 * </p>
 */
public class Bounds {

	/**
	 * The minimum corner of the bounding box.
	 */
	private Vector3f min;

	/**
	 * The maximum corner of the bounding box.
	 */
	private Vector3f max;

	/**
	 * Constructs a new {@code Bounds} object with the specified minimum and
	 * maximum corners.
	 *
	 * @param min the minimum corner of the bounding box
	 * @param max the maximum corner of the bounding box
	 * @throws IllegalArgumentException if either {@code min} or {@code max} is
	 *                                  {@code null}
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
	 * Returns the closest point on the bounding box to the given {@code point}.
	 * The closest point is determined by clamping each coordinate of the point
	 * between the minimum and maximum bounds of the box.
	 *
	 * @param point the point to clamp to the bounding box
	 * @return a new {@code Vector3f} representing the closest point on the
	 *         bounding box
	 */
	public Vector3f closestPoint(Vector3f point) {
		float x = Math.max(min.x, Math.min(max.x, point.x));
		float y = Math.max(min.y, Math.min(max.y, point.y));
		float z = Math.max(min.z, Math.min(max.z, point.z));
		return new Vector3f(x, y, z);
	}

	/**
	 * Checks if the given {@code point} is inside the bounding box. The point is
	 * considered inside if all of its coordinates are between the minimum and
	 * maximum coordinates of the box.
	 *
	 * @param p the point to check
	 * @return {@code true} if the point is inside the bounding box, {@code false}
	 *         otherwise
	 */
	public boolean contains(Vector3f p) {
		return p.x >= min.x && p.x <= max.x && p.y >= min.y
		    && p.y <= max.y && p.z >= min.z && p.z <= max.z;
	}

	/**
	 * Expands the bounding box to encompass the given {@code point}. If the point
	 * is outside the current bounds, the box will be enlarged to include it.
	 *
	 * @param p the point to include in the bounding box
	 */
	public void encapsulate(Vector3f p) {
		min.set(Math.min(min.x, p.x), Math.min(min.y, p.y), Math.min(min.z, p.z));
		max.set(Math.max(max.x, p.x), Math.max(max.y, p.y), Math.max(max.z, p.z));
	}

	/**
	 * Expands the bounding box by the given {@code amount}. The expansion is done
	 * uniformly along all axes, increasing the size of the bounding box by the
	 * specified amount.
	 *
	 * @param amount the amount to expand the bounding box by
	 */
	public void expand(float amount) {
		float halfAmount = amount / 2;
		min.subtractLocal(halfAmount, halfAmount, halfAmount);
		max.addLocal(halfAmount, halfAmount, halfAmount);
	}

	/**
	 * Tests whether the given ray intersects this axis-aligned bounding box
	 * (AABB).
	 * <p>
	 * The method uses the slab method to compute the intersection by checking the
	 * ray's position and direction relative to the box's bounds in each axis (x,
	 * y, z). It accounts for parallel rays and updates intersection intervals to
	 * determine if there is an overlap.
	 * </p>
	 *
	 * @param ray the {@link Ray3f} to test for intersection with this AABB. The
	 *            ray must have its inverse direction precomputed and accessible
	 *            via {@code ray.getDirectionInv()} for optimal performance.
	 * @return {@code true} if the ray intersects the AABB, {@code false}
	 *         otherwise.
	 */
	public boolean intersectRay(Ray3f ray) {
		if (ray.getDirection().isZero()) {
			return false; // A ray with zero direction cannot intersect anything
		}

		if (min.equals(max)) {
			return ray.getOrigin().equals(min);
		}

		float tmin = 0.0f;
		float tmax = Float.POSITIVE_INFINITY;

		for (int d = 0; d < 3; ++d) {
			float invDir = ray.getDirectionInv().get(d);

			// Handle zero direction component
			if (invDir == 0.0f) {
				if (ray.getOrigin().get(d) < min.get(d)
				    || ray.getOrigin().get(d) > max.get(d)) {
					return false;
				}
				continue;
			}

			float bmin, bmax;
			if (invDir < 0.0f) {
				bmin = max.get(d);
				bmax = min.get(d);
			} else {
				bmin = min.get(d);
				bmax = max.get(d);
			}

			float dmin = (bmin - ray.getOrigin().get(d)) * invDir;
			float dmax = (bmax - ray.getOrigin().get(d)) * invDir;

			tmin = Math.max(tmin, dmin);
			tmax = Math.min(tmax, dmax);

			if (tmin > tmax) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Tests if this bounding box intersects another {@code Bounds}. The
	 * intersection is checked by comparing the min and max coordinates of both
	 * boxes.
	 *
	 * @param other the other bounding box to check for intersection
	 * @return {@code true} if the bounding boxes intersect, {@code false}
	 *         otherwise
	 */
	public boolean intersects(Bounds other) {
		return min.x <= other.max.x && max.x >= other.min.x && min.y <= other.max.y
		    && max.y >= other.min.y && min.z <= other.max.z && max.z >= other.min.z;
	}

	/**
	 * Calculates the squared distance from the given {@code point} to the closest
	 * point on the bounding box. This method avoids calculating the square root
	 * for performance reasons, returning the squared distance instead.
	 *
	 * @param point the point to calculate the squared distance from
	 * @return the squared distance from the point to the closest point on the
	 *         bounding box
	 */
	public float sqrDistance(Vector3f point) {
		float dx = Math.max(0, Math.max(min.x - point.x, point.x - max.x));
		float dy = Math.max(0, Math.max(min.y - point.y, point.y - max.y));
		float dz = Math.max(0, Math.max(min.z - point.z, point.z - max.z));
		return dx * dx + dy * dy + dz * dz;
	}

	/**
	 * Sets the minimum and maximum corners of the bounding box to the specified
	 * values.
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

}