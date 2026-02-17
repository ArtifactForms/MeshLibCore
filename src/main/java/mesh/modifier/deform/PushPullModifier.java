package mesh.modifier.deform;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * A modifier that adjusts the vertices of a 3D mesh by displacing them radially relative to a
 * specified center point. The displacement is determined by the difference between the target
 * radius and the current distance of each vertex from the center.
 *
 * <p>This modifier can be used to create effects like expanding or contracting a mesh radially,
 * forming shapes such as spheres, domes, or pits.
 */
public class PushPullModifier implements IMeshModifier {

  private static final float EPSILON = 1e-6f;

  /** Target radius for radial displacement. */
  private float targetRadius;

  /** Center point for radial displacement. */
  private Vector3f center;

  /** Default constructor. Initializes with zero displacement and origin (0, 0, 0) as center. */
  public PushPullModifier() {
    this(0, new Vector3f());
  }

  /**
   * Constructs a PushPullModifier with the specified radius and center point. The center cannot be
   * null.
   *
   * @param targetRadius the target radius for vertex displacement
   * @param center the center point for radial displacement
   * @throws IllegalArgumentException if center is null
   */
  public PushPullModifier(float targetRadius, Vector3f center) {
    setTargetRadius(targetRadius);
    setCenter(center);
  }

  /**
   * Modifies the mesh by displacing its vertices radially relative to the center point.
   *
   * @param mesh the mesh to modify
   * @return the modified mesh
   * @throws IllegalArgumentException if the provided mesh is null
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
    if (mesh.getVertexCount() == 0) {
      return mesh;
    }
    pushPullVertices(mesh);
    return mesh;
  }

  /** Displaces each vertex radially relative to the center point. */
  private void pushPullVertices(Mesh3D mesh) {
    for (int i = 0; i < mesh.getVertexCount(); i++) {
      Vector3f v = mesh.getVertexAt(i);
      pushPullVertex(v);
    }
  }

  /**
   * Displaces a single vertex radially based on its distance to the center.
   *
   * @param vertex the vertex to modify
   */
  private void pushPullVertex(Vector3f vertex) {
    float distanceToCenter = vertex.distance(center);
    if (Math.abs(distanceToCenter) < EPSILON) {
      // Vertices exactly at the center will result in a NaN value during
      // normalization in directionToCenter. This check skips such vertices.
      return;
    }
    float displacement = targetRadius - distanceToCenter;
    Vector3f directionToCenter = vertex.subtract(center).normalize();
    vertex.set(directionToCenter.mult(displacement).add(center));
  }

  /**
   * Gets the target radius for radial displacement.
   *
   * @return the target radius
   */
  public float getTargetRadius() {
    return targetRadius;
  }

  /**
   * Sets the target radius for radial displacement.
   *
   * @param targetRadius the target radius to set
   */
  public void setTargetRadius(float targetRadius) {
    this.targetRadius = targetRadius;
  }

  /**
   * Gets the center point for radial displacement.
   *
   * @return the center point
   */
  public Vector3f getCenter() {
    return center;
  }

  /**
   * Sets the center point for radial displacement. The center cannot be null.
   *
   * @param center the center point to set
   * @throws IllegalArgumentException if the center is null
   */
  public void setCenter(Vector3f center) {
    if (center == null) {
      throw new IllegalArgumentException("Center cannot be null.");
    }
    this.center = center;
  }
}
