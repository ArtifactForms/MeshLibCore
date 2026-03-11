package engine.scene.camera;

import math.Bounds;
import math.Matrix4f;
import math.Plane;
import math.Vector3f;

/**
 * Represents a view frustum used for visibility culling in a 3D scene.
 *
 * <p>The frustum is defined by six clipping planes:
 *
 * <ul>
 *   <li>Left
 *   <li>Right
 *   <li>Bottom
 *   <li>Top
 *   <li>Near
 *   <li>Far
 * </ul>
 *
 * <p>The planes are extracted from the camera's view-projection matrix and can be used to test
 * whether objects lie inside the camera's visible region. This is commonly used for <b>frustum
 * culling</b>, which avoids rendering objects that are outside the camera's view volume.
 *
 * <p>This implementation supports intersection tests for:
 *
 * <ul>
 *   <li>Bounding spheres
 *   <li>Axis-aligned bounding boxes (AABB)
 * </ul>
 *
 * <p>The frustum planes are normalized after extraction to ensure correct distance calculations
 * during intersection tests.
 */
public class Frustum {

  /** The left clipping plane of the frustum. */
  public final Plane left = new Plane();

  /** The right clipping plane of the frustum. */
  public final Plane right = new Plane();

  /** The bottom clipping plane of the frustum. */
  public final Plane bottom = new Plane();

  /** The top clipping plane of the frustum. */
  public final Plane top = new Plane();

  /** The near clipping plane of the frustum. */
  public final Plane near = new Plane();

  /** The far clipping plane of the frustum. */
  public final Plane far = new Plane();

  /** Internal array containing all frustum planes for iteration. */
  private final Plane[] planes = new Plane[6];

  /** Creates a new frustum instance with initialized clipping planes. */
  public Frustum() {
    planes[0] = left;
    planes[1] = right;
    planes[2] = bottom;
    planes[3] = top;
    planes[4] = near;
    planes[5] = far;
  }

  /**
   * Updates the frustum planes using a combined view-projection matrix.
   *
   * <p>The planes are extracted directly from the matrix using the standard frustum extraction
   * method:
   *
   * <pre>
   * Left   = m3 + m0
   * Right  = m3 - m0
   * Bottom = m3 + m1
   * Top    = m3 - m1
   * Near   = m3 - m2
   * Far    = m3 + m2
   * </pre>
   *
   * <p>After extraction, all planes are normalized to ensure correct distance calculations.
   *
   * @param vp the combined view-projection matrix of the camera
   */
  public void update(Matrix4f vp) {

    float[] m = vp.getValues();

    // LEFT
    left.set(m[12] + m[0], m[13] + m[1], m[14] + m[2], m[15] + m[3]);

    // RIGHT
    right.set(m[12] - m[0], m[13] - m[1], m[14] - m[2], m[15] - m[3]);

    // BOTTOM
    bottom.set(m[12] + m[4], m[13] + m[5], m[14] + m[6], m[15] + m[7]);

    // TOP
    top.set(m[12] - m[4], m[13] - m[5], m[14] - m[6], m[15] - m[7]);

    // NEAR
    near.set(m[12] - m[8], m[13] - m[9], m[14] - m[10], m[15] - m[11]);

    // FAR
    far.set(m[12] + m[8], m[13] + m[9], m[14] + m[10], m[15] + m[11]);

    for (Plane p : planes) {
      normalizePlane(p);
    }
  }

  /**
   * Normalizes a plane so that the normal vector has unit length.
   *
   * <p>This is required to ensure correct signed distance calculations when performing intersection
   * tests.
   *
   * @param p the plane to normalize
   */
  private void normalizePlane(Plane p) {

    Vector3f n = p.getNormal();

    float length = (float) Math.sqrt(n.x * n.x + n.y * n.y + n.z * n.z);

    if (length == 0f) return;

    float inv = 1.0f / length;

    p.set(n.x * inv, n.y * inv, n.z * inv, p.getDistance() * inv);
  }

  /**
   * Tests whether a bounding sphere intersects or lies inside the frustum.
   *
   * <p>If the sphere lies completely outside any frustum plane, it is considered not visible.
   *
   * @param center the center of the sphere
   * @param radius the sphere radius
   * @return {@code true} if the sphere intersects or lies inside the frustum, {@code false} if it
   *     lies completely outside
   */
  public boolean intersectsSphere(Vector3f center, float radius) {
    if (near.distanceToPoint(center) < -radius) {
      return false;
    }

    if (left.distanceToPoint(center) < -radius) return false;
    if (right.distanceToPoint(center) < -radius) return false;
    if (bottom.distanceToPoint(center) < -radius) return false;
    if (top.distanceToPoint(center) < -radius) return false;
    if (far.distanceToPoint(center) < -radius) return false;

    return true;
  }

  /**
   * Tests whether an axis-aligned bounding box (AABB) intersects the frustum.
   *
   * <p>This method uses the <b>positive vertex test</b>, which selects the corner of the AABB that
   * lies farthest in the direction of the plane normal. If that vertex is outside the plane, the
   * entire bounding box is outside the frustum.
   *
   * @param bounds the axis-aligned bounding box
   * @return {@code true} if the bounding box intersects or lies inside the frustum, {@code false}
   *     if it lies completely outside
   */
  public boolean intersectsAABB(Bounds bounds) {
    Vector3f min = bounds.getMin();
    Vector3f max = bounds.getMax();

    for (Plane plane : planes) {
      Vector3f n = plane.getNormal();

      float px = (n.x >= 0) ? max.x : min.x;
      float py = (n.y >= 0) ? max.y : min.y;
      float pz = (n.z >= 0) ? max.z : min.z;

      if (n.x * px + n.y * py + n.z * pz + plane.getDistance() < 0) {
        return false;
      }
    }
    return true;
  }
}
