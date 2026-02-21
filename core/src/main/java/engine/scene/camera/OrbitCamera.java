package engine.scene.camera;

import engine.components.Transform;
import math.Mathf;
import math.Vector3f;

/**
 * An orbit-style camera implementation typically used in 3D editors.
 *
 * <p>This camera rotates around a central target point using spherical coordinates:
 *
 * <ul>
 *   <li><b>Yaw:</b> Horizontal rotation around the Y-axis.
 *   <li><b>Pitch:</b> Vertical rotation (clamped to prevent gimbal lock and flipping).
 *   <li><b>Distance:</b> The radius of the orbit from the target.
 * </ul>
 *
 * * It acts as a wrapper/decorator around a {@link PerspectiveCamera}, delegating projection
 * properties while managing the view transformation logic.
 */
public class OrbitCamera implements Camera {

  private final PerspectiveCamera camera;

  /** Horizontal rotation angle in radians. */
  private float yaw;

  /** Vertical rotation angle in radians. */
  private float pitch;

  /** Radius of the orbit from the target point. */
  private float distance = 10f;

  /** Minimum pitch limit to prevent camera flipping at the poles. */
  private final float minPitch = -Mathf.PI / 2f + 0.01f;

  /** Maximum pitch limit to prevent camera flipping at the poles. */
  private final float maxPitch = Mathf.PI / 2f - 0.01f;

  /** Initializes a new OrbitCamera with default orientation and distance. */
  public OrbitCamera() {
    camera = new PerspectiveCamera();
    updateTransform();
  }

  /**
   * Updates the camera's world position and orientation.
   *
   * <p>Converts the spherical coordinates (yaw, pitch, distance) into a Cartesian position relative
   * to the target and directs the camera to look at the target.
   */
  private void updateTransform() {
    pitch = Mathf.clamp(pitch, minPitch, maxPitch);

    // Calculate position on the sphere around the target using spherical-to-cartesian conversion
    float cosPitch = (float) Math.cos(pitch);
    float x = distance * cosPitch * (float) Math.sin(yaw);
    float y = distance * (float) Math.sin(pitch);
    float z = distance * cosPitch * (float) Math.cos(yaw);

    Vector3f targetPoint = camera.getTarget();
    Transform t = camera.getTransform();

    // Offset the calculated sphere position by the target's world position
    t.setPosition(targetPoint.x + x, targetPoint.y + y, targetPoint.z + z);

    // Ensure the camera is always oriented towards the target
    t.lookAt(targetPoint);
  }

  /**
   * Rotates the camera by adding deltas to yaw and pitch. * @param deltaYaw Angle to add to
   * horizontal rotation (in radians).
   *
   * @param deltaPitch Angle to add to vertical rotation (in radians).
   */
  public void rotate(float deltaYaw, float deltaPitch) {
    yaw += deltaYaw;
    pitch += deltaPitch;
    updateTransform();
  }

  /**
   * Adjusts the orbit radius using a multiplier. * @param delta The zoom amount. Usually a small
   * value where positive zooms in and negative zooms out.
   */
  public void zoom(float delta) {
    distance *= (1.0f - delta);
    distance = Math.max(distance, 0.1f);
    updateTransform();
  }

  /**
   * Moves the target point in world space. * @param offset The world-space vector to add to the
   * current target position.
   */
  public void pan(Vector3f offset) {
    Vector3f target = camera.getTarget();
    camera.setTarget(target.add(offset));
    updateTransform();
  }

  /** @return The current yaw (horizontal) rotation in radians. */
  public float getYaw() {
    return yaw;
  }

  /** @return The current pitch (vertical) rotation in radians. */
  public float getPitch() {
    return pitch;
  }

  /** @return The current orbit radius from the target. */
  public float getDistance() {
    return distance;
  }

  /**
   * Sets the orbit radius directly.
   *
   * @param distance The new radius (clamped to a minimum of 0.1).
   */
  public void setDistance(float distance) {
    this.distance = Math.max(distance, 0.1f);
    updateTransform();
  }

  /** Resets the target point to the world origin (0, 0, 0). */
  public void resetTarget() {
    setTarget(new Vector3f(0, 0, 0));
  }

  /**
   * Focuses the camera on a specific world position. * @param point The world position to orbit
   * around.
   */
  public void focusOn(Vector3f point) {
    setTarget(point);
  }

  @Override
  public Transform getTransform() {
    return camera.getTransform();
  }

  @Override
  public Vector3f getTarget() {
    return camera.getTarget();
  }

  @Override
  public void setTarget(Vector3f target) {
    camera.setTarget(target);
    updateTransform();
  }

  @Override
  public float getFieldOfView() {
    return camera.getFieldOfView();
  }

  @Override
  public void setFieldOfView(float fov) {
    camera.setFieldOfView(fov);
  }

  @Override
  public float getNearPlane() {
    return camera.getNearPlane();
  }

  @Override
  public void setNearPlane(float nearPlane) {
    camera.setNearPlane(nearPlane);
  }

  @Override
  public float getFarPlane() {
    return camera.getFarPlane();
  }

  @Override
  public void setFarPlane(float farPlane) {
    camera.setFarPlane(farPlane);
  }

  @Override
  public float getAspectRatio() {
    return camera.getAspectRatio();
  }

  @Override
  public void setAspectRatio(float aspectRatio) {
    camera.setAspectRatio(aspectRatio);
  }

  @Override
  public math.Matrix4f getViewMatrix() {
    return camera.getViewMatrix();
  }

  @Override
  public math.Matrix4f getProjectionMatrix() {
    return camera.getProjectionMatrix();
  }

  @Override
  public math.Matrix4f getViewProjectionMatrix() {
    return camera.getViewProjectionMatrix();
  }

  @Override
  public float getHorizontalFOV() {
    return camera.getHorizontalFOV();
  }

  @Override
  public float getVerticalFOV() {
    return camera.getVerticalFOV();
  }
}
