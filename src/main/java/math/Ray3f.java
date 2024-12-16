package math;

/**
 * Represents a ray in 3D space, defined by an origin point and a direction
 * vector.
 * <p>
 * A ray is a mathematical abstraction used in 3D graphics, physics simulations,
 * and computational geometry. It extends infinitely from its origin in the
 * specified direction. The direction vector is automatically normalized during
 * initialization to ensure consistent calculations.
 * </p>
 */
public class Ray3f {

	/**
	 * The starting point of the ray.
	 */
	private final Vector3f origin;

	/**
	 * The normalized direction vector of the ray.
	 */
	private final Vector3f direction;

	/**
	 * Constructs a new {@code Ray3f} with the given origin and direction.
	 * <p>
	 * The direction vector will be normalized internally to ensure correctness in
	 * calculations. Both {@code origin} and {@code direction} must be non-null.
	 * </p>
	 * 
	 * @param origin    The starting point of the ray (non-null)
	 * @param direction The direction vector of the ray (non-null, normalized
	 *                  internally)
	 * @throws IllegalArgumentException if either {@code origin} or
	 *                                  {@code direction} is null
	 */
	public Ray3f(Vector3f origin, Vector3f direction) {
		if (origin == null) {
			throw new IllegalArgumentException("Origin cannot be null.");
		}
		if (direction == null) {
			throw new IllegalArgumentException("Direction cannot be null.");
		}
		this.origin = origin;
		this.direction = direction;
		this.direction.normalizeLocal();
	}

	/**
	 * Returns the origin of the ray.
	 * 
	 * @return The origin of the ray.
	 */
	public Vector3f getOrigin() {
		return origin;
	}

	/**
	 * Returns the normalized direction vector of the ray.
	 * 
	 * @return The direction vector of the ray.
	 */
	public Vector3f getDirection() {
		return direction;
	}

	/**
	 * Computes the point along the ray at a given parameter {@code t}.
	 * <p>
	 * The formula for the point is:
	 * 
	 * <pre>
	 * {@code
	 * point = origin + t * direction
	 * }
	 * </pre>
	 * 
	 * where {@code t} is a scalar representing the distance along the ray from
	 * the origin.
	 * </p>
	 * 
	 * @param t The parameter along the ray (can be negative, zero, or positive)-
	 * @return The point at parameter {@code t}.
	 */
	public Vector3f getPointAt(float t) {
		return origin.add(direction.mult(t));
	}

}