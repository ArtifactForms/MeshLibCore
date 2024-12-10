package mesh.modifier;

import math.Vector3f;
import mesh.Mesh3D;

/**
 * The {@code SpherifyModifier} class modifies a 3D mesh by transforming its
 * vertices to approximate the shape of a sphere. The degree of spherification
 * is controlled by a factor between 0 and 1, with additional parameters for the
 * sphere's radius and center.
 */
public class SpherifyModifier implements IMeshModifier {

	/**
	 * The interpolation factor for spherification (0 = no effect, 1 = full
	 * sphere).
	 */
	private float factor;

	/**
	 * The radius of the sphere used in the spherification process.
	 */
	private float radius;

	/**
	 * The center of the sphere.
	 */
	private Vector3f center;

	/**
	 * The mesh being modified.
	 */
	private Mesh3D mesh;

	/**
	 * Default constructor. Creates a spherify modifier with a default radius of
	 * 1.0 and a factor of 1.0.
	 */
	public SpherifyModifier() {
		this(1.0f);
	}

	/**
	 * Constructor with a specified radius.
	 *
	 * @param radius the radius of the sphere. Must be greater than zero.
	 * @throws IllegalArgumentException if the radius is less than or equal to
	 *                                  zero.
	 */
	public SpherifyModifier(float radius) {
		this.center = new Vector3f();
		setRadius(radius);
		setFactor(1.0f);
	}

	/**
	 * Modifies the given mesh by spherifying its vertices. If the provided mesh
	 * contains no vertices, the method safely returns the mesh without changes.
	 *
	 * @param mesh the mesh to modify. Cannot be {@code null}.
	 * @return the modified mesh.
	 * @throws IllegalArgumentException if the provided mesh is {@code null}.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		if (factor == 0) {
			return mesh;
		}
		if (mesh.vertices.isEmpty()) {
			return mesh;
		}
		setMesh(mesh);
		spherify();
		return mesh;
	}

	/**
	 * Performs the spherification on the mesh vertices.
	 */
	private void spherify() {
		mesh.vertices.parallelStream().forEach(this::spherifyVertex);
	}

	/**
	 * Spherifies a single vertex by interpolating its position toward the
	 * corresponding point on the sphere surface.
	 *
	 * @param vertex the vertex to modify.
	 */
	private void spherifyVertex(Vector3f vertex) {
		Vector3f direction = vertex.subtract(center).normalize();
		Vector3f newPosition = direction.mult(radius).add(center);
		vertex.lerpLocal(newPosition, factor);
	}

	/**
	 * Sets the mesh to be modified.
	 *
	 * @param mesh the mesh to modify.
	 */
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	/**
	 * Returns the interpolation factor for spherification.
	 *
	 * @return the factor.
	 */
	public float getFactor() {
		return factor;
	}

	/**
	 * Sets the interpolation factor for spherification.
	 *
	 * @param factor the factor, a value between 0 and 1.
	 * @throws IllegalArgumentException if the factor is not between 0 and 1.
	 */
	public void setFactor(float factor) {
		if (factor < 0 || factor > 1)
			throw new IllegalArgumentException("Factor must be between 0 and 1.");
		this.factor = factor;
	}

	/**
	 * Returns the radius of the sphere used in the spherification process.
	 *
	 * @return the radius.
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * Sets the radius of the sphere used in the spherification process.
	 *
	 * @param radius the radius. Must be greater than zero.
	 * @throws IllegalArgumentException if the radius is less than or equal to
	 *                                  zero.
	 */
	public void setRadius(float radius) {
		if (radius <= 0)
			throw new IllegalArgumentException("Radius must be greater than zero.");
		this.radius = radius;
	}

	/**
	 * Returns the center of the sphere.
	 *
	 * @return the center of the sphere as a {@link Vector3f}.
	 */
	public Vector3f getCenter() {
		return new Vector3f(center);
	}

	/**
	 * Sets the center of the sphere using individual coordinates.
	 *
	 * @param x the x-coordinate of the center.
	 * @param y the y-coordinate of the center.
	 * @param z the z-coordinate of the center.
	 */
	public void setCenter(float x, float y, float z) {
		center.set(x, y, z);
	}

	/**
	 * Sets the center of the sphere using a {@link Vector3f}.
	 *
	 * @param center the center of the sphere. Cannot be {@code null}.
	 * @throws IllegalArgumentException if the center is {@code null}.
	 */
	public void setCenter(Vector3f center) {
		if (center == null)
			throw new IllegalArgumentException("Center cannot be null.");
		this.center.set(center);
	}

}
