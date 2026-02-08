package mesh.modifier.deform;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * A modifier that bends a 3D mesh along the X-axis.
 *
 * <p>This modifier applies a simple bending deformation to each vertex of the given 3D mesh. The
 * degree of bending is controlled by the {@code factor} parameter. A higher factor results in a
 * more pronounced bend. Extreme bending may distort or cause self-intersection depending on the
 * value of the factor.
 */
public class BendModifier implements IMeshModifier {

  /** A very small value used to determine if the bending factor is effectively zero. */
  private static final float EPSILON = 1e-7f;

  /**
   * The bending factor determining the degree of bending along the X-axis. Default value is 0.5.
   */
  private float factor;

  /**
   * A modifier that bends a mesh along the X-axis.
   *
   * <p>The bending is controlled by the `factor` parameter. Default is 0.5f, introducing a subtle
   * bend.
   */
  public BendModifier() {
    this(0.5f);
  }

  /**
   * Constructor to specify a custom bending factor.
   *
   * @param factor the bending factor controlling the degree of bending. Higher values cause more
   *     bending, but extreme values can distort the mesh.
   */
  public BendModifier(float factor) {
    this.factor = factor;
  }

  /**
   * Modifies the provided mesh by applying bending to its vertices along the X-axis. If the
   * provided mesh contains no vertices, the method safely returns the mesh without changes.
   *
   * <p>The bending is only applied if the {@link #factor} value is valid (greater than a small
   * threshold, defined by {@link #EPSILON}). This prevents the mesh from being unnecessarily
   * modified when the bending factor is negligible and would result in division by zero issues.
   *
   * @param mesh the 3D mesh to bend. Cannot be {@code null}.
   * @return the modified mesh after applying bending, or the original mesh if no changes are
   *     applied.
   * @throws IllegalArgumentException if {@code mesh} is null.
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
    if (mesh.vertices.isEmpty()) {
      return mesh;
    }
    if (isFactorValid()) bend(mesh);
    return mesh;
  }

  /**
   * Performs the bending operation on all vertices of the provided mesh using parallel processing.
   *
   * @param mesh the 3D mesh whose vertices are to be deformed.
   */
  private void bend(Mesh3D mesh) {
    mesh.vertices.parallelStream().forEach(this::simpleDeformBend);
  }

  /**
   * Applies the bending transformation to a single vertex based on the bending equation.
   *
   * @param v the vertex to deform.
   */
  private void simpleDeformBend(Vector3f v) {
    float theta = v.x * factor;
    float sinTheta = Mathf.sin(theta);
    float cosTheta = Mathf.cos(theta);

    float bx = -(v.y - 1.0f / factor) * sinTheta;
    float by = (v.y - 1.0f / factor) * cosTheta + 1.0f / factor;
    float bz = v.z;

    v.set(bx, by, bz);
  }

  /**
   * Checks if the bending factor is valid (i.e., not effectively zero).
   *
   * @return {@code true} if the factor is a valid number for bending, {@code false} otherwise.
   */
  private boolean isFactorValid() {
    return Mathf.abs(factor) > EPSILON;
  }

  /**
   * Gets the current bending factor value.
   *
   * @return the bending factor value.
   */
  public float getFactor() {
    return factor;
  }

  /**
   * Sets the bending factor to a new value.
   *
   * @param factor the new bending factor value. Higher values result in more bending.
   */
  public void setFactor(float factor) {
    this.factor = factor;
  }
}
