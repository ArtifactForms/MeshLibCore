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
	 * @param point the point to check
	 * @return {@code true} if the point is inside the bounding box, {@code false}
	 *         otherwise
	 */
	public boolean contains(Vector3f point) {
		return point.x >= min.x && point.x <= max.x && point.y >= min.y
		    && point.y <= max.y && point.z >= min.z && point.z <= max.z;
	}

	/**
	 * Expands the bounding box to encompass the given {@code point}. If the point
	 * is outside the current bounds, the box will be enlarged to include it.
	 *
	 * @param point the point to include in the bounding box
	 */
	public void encapsulate(Vector3f point) {
		min = new Vector3f(Math.min(min.x, point.x), Math.min(min.y, point.y),
		    Math.min(min.z, point.z));
		max = new Vector3f(Math.max(max.x, point.x), Math.max(max.y, point.y),
		    Math.max(max.z, point.z));
	}

	/**
	 * Expands the bounding box by the given {@code amount}. The expansion is done
	 * uniformly along all axes, increasing the size of the bounding box by the
	 * specified amount.
	 *
	 * @param amount the amount to expand the bounding box by
	 */
	public void expand(float amount) {
		Vector3f expansion = new Vector3f(amount / 2, amount / 2, amount / 2);
		min = min.subtract(expansion);
		max = max.add(expansion);
	}

	/**
	 * Tests if the given {@code ray} intersects the bounding box. The
	 * intersection is checked using the slab method, which determines if the ray
	 * intersects the box along each axis.
	 *
	 * @param ray the ray to test for intersection
	 * @return {@code true} if the ray intersects the bounding box, {@code false}
	 *         otherwise
	 */
	public boolean intersectRay(Ray3f ray) {
		Vector3f invDir = ray.getDirection().reciprocal();
		Vector3f tMin = min.subtract(ray.getOrigin()).mult(invDir);
		Vector3f tMax = max.subtract(ray.getOrigin()).mult(invDir);

		float t1 = Math.min(tMin.x, tMax.x);
		float t2 = Math.max(tMin.x, tMax.x);

		for (int i = 1; i < 3; i++) { // For y and z axes
			t1 = Math.max(t1, Math.min(tMin.get(i), tMax.get(i)));
			t2 = Math.min(t2, Math.max(tMin.get(i), tMax.get(i)));
		}

		return t1 <= t2 && t2 >= 0;
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
	 */
	public void setMinMax(Vector3f min, Vector3f max) {
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