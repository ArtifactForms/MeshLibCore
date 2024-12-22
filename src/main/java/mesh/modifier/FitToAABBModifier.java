package mesh.modifier;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.util.Bounds3;

/**
 * A modifier that scales a 3D mesh uniformly to fit within a specified axis-aligned bounding box
 * (AABB). The scaling ensures that the largest dimension of the mesh is scaled to match the
 * smallest target dimension without altering the aspect ratio of the mesh.
 */
public class FitToAABBModifier implements IMeshModifier {

  /**
   * The target dimensions of the AABB to fit the mesh into. The x, y, and z values must be greater
   * than zero.
   */
  private Vector3f targetDimension;

  /** The mesh to be modified. */
  private Mesh3D mesh;

  /**
   * Creates a new modifier with the specified target AABB dimensions.
   *
   * @param width the target width of the AABB; must be greater than zero.
   * @param height the target height of the AABB; must be greater than zero.
   * @param depth the target depth of the AABB; must be greater than zero.
   * @throws IllegalArgumentException if any dimension is less than or equal to zero.
   */
  public FitToAABBModifier(float width, float height, float depth) {
    targetDimension = new Vector3f(width, height, depth);
    validateTargetDimension();
  }

  /**
   * Modifies the mesh by scaling it uniformly to fit within the target AABB. If the provided mesh
   * contains no vertices, the method safely returns the mesh without changes.
   *
   * @param mesh the mesh to be scaled; cannot be null.
   * @return the scaled mesh.
   * @throws IllegalArgumentException if the provided mesh is null.
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
    if (mesh.vertices.isEmpty()) {
      return mesh;
    }
    setMesh(mesh);
    applyScale(calculateScale());
    return mesh;
  }

  /**
   * Validates that the target dimensions are greater than zero.
   *
   * @throws IllegalArgumentException if any target dimension is less than or equal to zero.
   */
  private void validateTargetDimension() {
    if (targetDimension.x <= 0 || targetDimension.y <= 0 || targetDimension.z <= 0) {
      throw new IllegalArgumentException("Target dimensions must be greater than zero.");
    }
  }

  /**
   * Applies a uniform scale to the mesh.
   *
   * @param scale the scale factor to apply.
   */
  private void applyScale(float scale) {
    mesh.apply(new ScaleModifier(scale));
  }

  /**
   * Calculates the uniform scaling factor based on the target dimensions and the current bounding
   * box dimensions of the mesh.
   *
   * @return the calculated scale factor.
   */
  private float calculateScale() {
    float minDimension = getMinimumTargetDimension();
    float maxDimension = getMaximumSourceDimension();
    return minDimension / (maxDimension == 0 ? 1 : maxDimension);
  }

  /**
   * Determines the smallest dimension from the target AABB.
   *
   * @return the smallest target dimension.
   */
  private float getMinimumTargetDimension() {
    return Mathf.min(targetDimension.x, targetDimension.y, targetDimension.z);
  }

  /**
   * Determines the largest dimension of the current mesh bounding box.
   *
   * @return the largest source dimension.
   */
  private float getMaximumSourceDimension() {
    Bounds3 bounds = mesh.calculateBounds();
    return Mathf.max(bounds.getWidth(), bounds.getHeight(), bounds.getDepth());
  }

  /**
   * Sets the current mesh being modified.
   *
   * @param mesh the mesh to modify.
   */
  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }
}
