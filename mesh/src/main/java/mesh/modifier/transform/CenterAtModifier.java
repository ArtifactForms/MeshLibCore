package mesh.modifier.transform;

import math.Bounds;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.util.MeshBoundsCalculator;

/**
 * A modifier that centers a 3D mesh at a specified point in space.
 *
 * <p>This class translates the mesh so that its center aligns with the given target center. If the
 * mesh is already centered within a small threshold defined by {@code EPSILON}, no changes are
 * made.
 */
public class CenterAtModifier implements IMeshModifier {

  /** A small threshold used to determine if the mesh is already centered. */
  public static final float EPSILON = 1e-6f;

  /** The target center point to which the mesh should be aligned. */
  private Vector3f center;

  /** The mesh currently being modified. */
  private Mesh3D mesh;

  /** The calculated bounding box of the mesh. */
  private Bounds bounds;

  /** Constructs a new {@code CenterAtModifier} with the default center at the origin (0, 0, 0). */
  public CenterAtModifier() {
    center = new Vector3f();
  }

  /**
   * Constructs a new {@code CenterAtModifier} with the specified target center.
   *
   * @param center the target center point. Cannot be {@code null}. throws IllegalArgumentException
   *     if {@code center} is null.
   */
  public CenterAtModifier(Vector3f center) {
    setCenter(center);
  }

  /**
   * Modifies the provided mesh by translating it to align its center with the target center. If the
   * mesh is already centered (within {@code EPSILON}), no changes are made.
   *
   * @param mesh the 3D mesh to center. Cannot be {@code null}.
   * @return the modified mesh after centering, or the original mesh if no changes were applied.
   * @throws IllegalArgumentException if {@code mesh} is null.
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
    if (mesh.getVertexCount() == 0) {
      return mesh;
    }
    setMesh(mesh);
    calculateBounds();
    if (meshIsAlreadyCentered()) {
      return mesh;
    }
    centerMesh();
    return mesh;
  }

  /** Translates the mesh to align its center with the target center. */
  private void centerMesh() {
    Vector3f distance = center.subtract(bounds.getCenter());
    new TranslateModifier(distance).modify(mesh);
  }

  /**
   * Sets the mesh to be modified.
   *
   * @param mesh the mesh to modify
   */
  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }

  /** Calculates the bounds of the current mesh. */
  private void calculateBounds() {
    this.bounds = MeshBoundsCalculator.calculateBounds(mesh);
  }

  /**
   * Checks whether the mesh is already centered at the target center within the threshold {@code
   * EPSILON}.
   *
   * @return {@code true} if the mesh is already centered; {@code false} otherwise.
   */
  private boolean meshIsAlreadyCentered() {
    return bounds.getCenter().distance(center) < EPSILON;
  }

  /**
   * Returns the target center point.
   *
   * @return the target center point.
   */
  public Vector3f getCenter() {
    return center;
  }

  /**
   * Sets the target center point to which the mesh should be aligned.
   *
   * @param center the new target center point. Cannot be {@code null}.
   * @throws IllegalArgumentException if {@code center} is null.
   */
  public void setCenter(Vector3f center) {
    if (center == null) {
      throw new IllegalArgumentException("Center cannot be null");
    }
    this.center = center;
  }
}
