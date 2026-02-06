package engine.scene.camera;

import math.Vector3f;

/**
 * Represents the world-space geometry of a camera view frustum.
 *
 * <p>The frustum is described by its eight corner points: four on the near plane and four on the
 * far plane.
 *
 * <p>This class is purely geometric and contains no rendering logic. It is intended to be reused
 * for:
 *
 * <ul>
 *   <li>Debug visualization
 *   <li>Frustum culling
 *   <li>Picking and intersection tests
 *   <li>Shadow cascade calculations
 * </ul>
 *
 * <h3>Mutability</h3>
 *
 * All corner vectors are mutable and updated in-place. The vector references remain stable after
 * construction.
 *
 * <h3>Performance Notes</h3>
 *
 * This class avoids per-frame allocation of its output vectors. Temporary vectors are used
 * internally during {@link #update(Camera)}.
 */
public class FrustumGeometry {

  /** Near-top-left corner of the frustum (world space). */
  public final Vector3f ntl;

  /** Near-top-right corner of the frustum (world space). */
  public final Vector3f ntr;

  /** Near-bottom-left corner of the frustum (world space). */
  public final Vector3f nbl;

  /** Near-bottom-right corner of the frustum (world space). */
  public final Vector3f nbr;

  /** Far-top-left corner of the frustum (world space). */
  public final Vector3f ftl;

  /** Far-top-right corner of the frustum (world space). */
  public final Vector3f ftr;

  /** Far-bottom-left corner of the frustum (world space). */
  public final Vector3f fbl;

  /** Far-bottom-right corner of the frustum (world space). */
  public final Vector3f fbr;

  /**
   * Creates an empty frustum geometry instance.
   *
   * <p>All corner vectors are initialized and can be safely reused across multiple {@link
   * #update(Camera)} calls.
   */
  public FrustumGeometry() {
    ntl = new Vector3f();
    ntr = new Vector3f();
    nbl = new Vector3f();
    nbr = new Vector3f();
    ftl = new Vector3f();
    ftr = new Vector3f();
    fbl = new Vector3f();
    fbr = new Vector3f();
  }

  /**
   * Updates this frustum geometry from the given camera.
   *
   * <p>All corner points are recalculated in world space based on:
   *
   * <ul>
   *   <li>Camera position
   *   <li>Camera orientation (forward, up, right)
   *   <li>Vertical field of view
   *   <li>Aspect ratio
   *   <li>Near and far plane distances
   * </ul>
   *
   * <p>This method does not modify any state inside the camera. All internal vectors are updated
   * in-place.
   *
   * @param camera the camera whose frustum should be computed
   */
  public void update(Camera camera) {
    // Camera basis
    Vector3f pos = camera.getTransform().getPosition();
    Vector3f forward = camera.getTransform().getForward().normalize();
    Vector3f up = camera.getTransform().getUp().normalize();
    Vector3f right = forward.cross(up).normalize();

    // Projection parameters
    float near = camera.getNearPlane();
    float far = camera.getFarPlane();
    float fovY = camera.getVerticalFOV(); // radians
    float aspect = camera.getAspectRatio();

    // Plane dimensions
    float nearHeight = (float) (2f * Math.tan(fovY / 2f) * near);
    float nearWidth = nearHeight * aspect;
    float farHeight = (float) (2f * Math.tan(fovY / 2f) * far);
    float farWidth = farHeight * aspect;

    // Plane centers
    Vector3f nc = new Vector3f(pos).addLocal(new Vector3f(forward).multLocal(near));
    Vector3f fc = new Vector3f(pos).addLocal(new Vector3f(forward).multLocal(far));

    // Near plane corners
    ntl.set(nc.add(up.mult(nearHeight / 2)).subtract(right.mult(nearWidth / 2)));
    ntr.set(nc.add(up.mult(nearHeight / 2)).add(right.mult(nearWidth / 2)));
    nbl.set(nc.subtract(up.mult(nearHeight / 2)).subtract(right.mult(nearWidth / 2)));
    nbr.set(nc.subtract(up.mult(nearHeight / 2)).add(right.mult(nearWidth / 2)));

    // Far plane corners
    ftl.set(fc.add(up.mult(farHeight / 2)).subtract(right.mult(farWidth / 2)));
    ftr.set(fc.add(up.mult(farHeight / 2)).add(right.mult(farWidth / 2)));
    fbl.set(fc.subtract(up.mult(farHeight / 2)).subtract(right.mult(farWidth / 2)));
    fbr.set(fc.subtract(up.mult(farHeight / 2)).add(right.mult(farWidth / 2)));
  }

  /**
   * Creates a new frustum geometry instance from the given camera.
   *
   * <p>This is a convenience factory for one-off computations. For per-frame usage, prefer reusing
   * a single instance and calling {@link #update(Camera)} instead.
   *
   * @param camera the camera to compute the frustum from
   * @return a new, initialized {@code FrustumGeometry}
   */
  public static FrustumGeometry fromCamera(Camera camera) {
    FrustumGeometry geometry = new FrustumGeometry();
    geometry.update(camera);
    return geometry;
  }
}
