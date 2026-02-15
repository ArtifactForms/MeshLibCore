package mesh.modifier.deform;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * A modifier that twists a 3D mesh around the Y-axis.
 *
 * <p>This modifier applies a twisting deformation to each vertex of the given 3D mesh.
 * The rotation angle for each vertex is determined by its Y-coordinate multiplied
 * by the {@code factor} parameter. A higher factor results in a stronger twist.
 * Extreme twist values may significantly distort the mesh geometry.
 */
public class TwistModifier implements IMeshModifier {

  /** A very small value used to determine if the twist factor is effectively zero. */
  private static final float EPSILON = 1e-7f;

  /**
   * The twist factor determining the amount of rotation around the Y-axis.
   * Default value is 0.5.
   */
  private float factor;

  /**
   * Creates a modifier that twists a mesh around the Y-axis.
   *
   * <p>The twisting is controlled by the {@code factor} parameter.
   * Default is 0.5f, introducing a moderate twist effect.
   */
  public TwistModifier() {
    this(0.5f);
  }

  /**
   * Constructor to specify a custom twist factor.
   *
   * @param factor the twist factor controlling the amount of rotation
   *     applied per unit of Y-coordinate. Higher values cause stronger
   *     twisting, but extreme values may heavily distort the mesh.
   */
  public TwistModifier(float factor) {
    this.factor = factor;
  }

  /**
   * Modifies the provided mesh by applying a twist deformation around the Y-axis.
   * If the provided mesh contains no vertices, the method safely returns
   * the mesh without changes.
   *
   * <p>The twisting is only applied if the {@link #factor} value is valid
   * (greater than a small threshold, defined by {@link #EPSILON}). This prevents
   * unnecessary computation and avoids numerical instability when the twist
   * factor is effectively zero.
   *
   * @param mesh the 3D mesh to twist. Cannot be {@code null}.
   * @return the modified mesh after applying the twist, or the original mesh
   *     if no changes are applied.
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
    if (isFactorValid()) {
      twist(mesh);
    }
    return mesh;
  }

  /**
   * Checks if the twist factor is valid (i.e., not effectively zero).
   *
   * @return {@code true} if the factor is a valid number for twisting,
   *     {@code false} otherwise.
   */
  private boolean isFactorValid() {
    return Mathf.abs(factor) > EPSILON;
  }

  /**
   * Performs the twisting operation on all vertices of the provided mesh.
   *
   * @param mesh the 3D mesh whose vertices are to be deformed.
   */
  private void twist(Mesh3D mesh) {
    for (int i = 0; i < mesh.getVertexCount(); i++) {
      Vector3f v = mesh.getVertexAt(i);
      twist(v);
    }
  }

  /**
   * Applies the twist transformation to a single vertex.
   *
   * <p>The vertex is rotated around the Y-axis. The rotation angle is
   * calculated as {@code angle = v.y * factor}. This results in vertices
   * higher along the Y-axis being rotated more strongly than lower ones.
   *
   * @param v the vertex to deform.
   */
  private void twist(Vector3f v) {
    float angle = v.getY() * factor;
    float cos = (float) Math.cos(angle);
    float sin = (float) Math.sin(angle);

    float x = v.getX();
    float z = v.getZ();

    float newX = cos * x - sin * z;
    float newZ = sin * x + cos * z;

    v.set(newX, v.getY(), newZ);
  }

  /**
   * Gets the current twist factor value.
   *
   * @return the twist factor value.
   */
  public float getFactor() {
    return factor;
  }

  /**
   * Sets the twist factor to a new value.
   *
   * @param factor the new twist factor value. Higher values result
   *     in stronger twisting.
   */
  public void setFactor(float factor) {
    this.factor = factor;
  }
}