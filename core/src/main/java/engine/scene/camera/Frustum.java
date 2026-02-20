package engine.scene.camera;

import math.Matrix4f;
import math.Plane;
import math.Vector3f;

/**
 * Represents a camera frustum defined by six clipping planes: left, right, bottom, top, near, and
 * far. This class provides methods to extract frustum planes from a projection matrix and perform
 * intersection tests for visibility culling.
 */
public class Frustum {

  /** The left clipping plane. */
  public final Plane left = new Plane();

  /** The right clipping plane. */
  public final Plane right = new Plane();

  /** The bottom clipping plane. */
  public final Plane bottom = new Plane();

  /** The top clipping plane. */
  public final Plane top = new Plane();

  /** The near clipping plane. */
  public final Plane near = new Plane();

  /** The far clipping plane. */
  public final Plane far = new Plane();

  /**
   * Updates the frustum planes based on the provided View-Projection matrix. This implementation
   * uses the Gribb-Hartmann method adapted for Row-Major matrices.
   *
   * @param vp The combined View-Projection matrix to extract planes from.
   */
  public void update(Matrix4f vp) {
    float[] m = vp.getValues();

    // In a Row-Major matrix, the 4th row (W-components) contains the values
    // at indices 12, 13, 14, and 15. We combine this row with the basis
    // rows (0=X, 1=Y, 2=Z) to extract the clipping planes.

    // Left Plane: Row 3 + Row 0
    left.set(m[12] + m[0], m[13] + m[1], m[14] + m[2], m[15] + m[3]);

    // Right Plane: Row 3 - Row 0
    right.set(m[12] - m[0], m[13] - m[1], m[14] - m[2], m[15] - m[3]);

    // Bottom Plane: Row 3 + Row 1
    bottom.set(m[12] + m[4], m[13] + m[5], m[14] + m[6], m[15] + m[7]);

    // Top Plane: Row 3 - Row 1
    top.set(m[12] - m[4], m[13] - m[5], m[14] - m[6], m[15] - m[7]);

    // Near Plane: Row 3 + Row 2
    near.set(m[12] + m[8], m[13] + m[9], m[14] + m[10], m[15] + m[11]);

    // Far Plane: Row 3 - Row 2
    far.set(m[12] - m[8], m[13] - m[9], m[14] - m[10], m[15] - m[11]);
  }

  /**
   * Performs a sphere-frustum intersection test.
   *
   * @param center The center point of the sphere in world space.
   * @param radius The radius of the sphere.
   * @return {@code true} if the sphere is partially or fully inside the frustum; {@code false} if
   *     it is completely outside.
   */
  public boolean containsSphere(Vector3f center, float radius) {
    if (left.distanceToPoint(center) < -radius) return false;
    if (right.distanceToPoint(center) < -radius) return false;
    if (bottom.distanceToPoint(center) < -radius) return false;
    if (top.distanceToPoint(center) < -radius) return false;
    if (near.distanceToPoint(center) < -radius) return false;
    if (far.distanceToPoint(center) < -radius) return false;
    return true;
  }

  /**
   * Returns an array containing all six frustum planes. The order is: Left, Right, Bottom, Top,
   * Near, Far.
   *
   * @return An array of the six {@link Plane} objects defining this frustum.
   */
  public Plane[] getPlanes() {
    return new Plane[] {left, right, bottom, top, near, far};
  }
}
