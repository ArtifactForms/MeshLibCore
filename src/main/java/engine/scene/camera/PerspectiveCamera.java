package engine.scene.camera;

import engine.components.Transform;
import math.Mathf;
import math.Matrix4;
import math.Matrix4f;
import math.Vector3f;

/**
 * A camera that simulates perspective projection, commonly used in 3D graphics. It uses a field of
 * view (FOV), near and far clipping planes, and an aspect ratio to create a 3D projection of the
 * world onto a 2D screen. The camera has a transform (position, rotation, scale) and a target that
 * it looks at. This class provides methods for manipulating and querying these properties.
 */
public class PerspectiveCamera implements Camera {

  /** The camera's transform (position, rotation, scale) */
  private final Transform transform = new Transform();

  /** The field of view (in radians), default is 60 degrees (PI/3) */
  private float fov = Mathf.PI / 3f;

  /** The near clipping plane distance */
  private float nearPlane = 0.1f;

  /** The far clipping plane distance */
  private float farPlane = 5000;

  /** The aspect ratio of the camera (width/height) */
  private float aspectRatio = 16f / 9f;

  /** The target point the camera is looking at */
  private Vector3f target;

  /**
   * Creates a new PerspectiveCamera with default values. The default field of view is 60 degrees,
   * near plane is 0.1, far plane is 5000, and the aspect ratio is 16:9.
   */
  public PerspectiveCamera() {
    target = new Vector3f();
  }

  /**
   * Calculates and returns the horizontal field of view (FOV) of the camera in radians.
   *
   * <p>The horizontal FOV is derived from the vertical FOV and the aspect ratio of the camera. It
   * is calculated using the following formula:
   *
   * <pre>
   * horizontalFOV = 2 * atan(tan(verticalFOV / 2) * aspectRatio)
   * </pre>
   *
   * This ensures that the horizontal FOV is consistent with the perspective projection defined by
   * the vertical FOV and the screen's aspect ratio.
   *
   * @return The horizontal field of view in radians.
   */
  public float getHorizontalFOV() {
    float verticalFOV = getVerticalFOV();
    return 2.0f * (float) Math.atan(Math.tan(verticalFOV / 2.0f) * aspectRatio);
  }

  /**
   * Gets the vertical field of view (FOV) of the camera in radians.
   *
   * @return The vertical FOV.
   */
  public float getVerticalFOV() {
    return fov;
  }

  /**
   * Gets the target point the camera is currently looking at.
   *
   * @return The target point.
   */
  @Override
  public Vector3f getTarget() {
    return target;
  }

  /**
   * Sets the target point for the camera to look at.
   *
   * @param target The target point in world space.
   */
  @Override
  public void setTarget(Vector3f target) {
    this.target = target;
  }

  /**
   * Gets the transform (position, rotation, and scale) of the camera.
   *
   * @return The camera's transform.
   */
  @Override
  public Transform getTransform() {
    return transform;
  }

  /**
   * Gets the field of view of the camera in radians.
   *
   * @return The field of view (in radians).
   */
  @Override
  public float getFieldOfView() {
    return fov;
  }

  /**
   * Sets the field of view of the camera in radians.
   *
   * @param fov The desired field of view (in radians).
   */
  @Override
  public void setFieldOfView(float fov) {
    this.fov = fov;
  }

  /**
   * Gets the near clipping plane distance of the camera.
   *
   * @return The near clipping plane distance.
   */
  @Override
  public float getNearPlane() {
    return nearPlane;
  }

  /**
   * Sets the near clipping plane distance of the camera.
   *
   * @param nearPlane The near clipping plane distance.
   */
  @Override
  public void setNearPlane(float nearPlane) {
    this.nearPlane = nearPlane;
  }

  /**
   * Gets the far clipping plane distance of the camera.
   *
   * @return The far clipping plane distance.
   */
  @Override
  public float getFarPlane() {
    return farPlane;
  }

  /**
   * Sets the far clipping plane distance of the camera.
   *
   * @param farPlane The far clipping plane distance.
   */
  @Override
  public void setFarPlane(float farPlane) {
    this.farPlane = farPlane;
  }

  /**
   * Gets the aspect ratio of the camera (width/height).
   *
   * @return The aspect ratio.
   */
  @Override
  public float getAspectRatio() {
    return aspectRatio;
  }

  /**
   * Sets the aspect ratio of the camera (width/height).
   *
   * @param aspectRatio The desired aspect ratio.
   */
  @Override
  public void setAspectRatio(float aspectRatio) {
    this.aspectRatio = aspectRatio;
  }

  @Override
  public Matrix4f getProjectionMatrix() {
    Matrix4f projection = new Matrix4f();
    Matrix4 m = Matrix4.perspective(fov, aspectRatio, nearPlane, farPlane);
    projection = new Matrix4f(m.getValues());
    return projection;
  }

  @Override
  public Matrix4f getViewProjectionMatrix() {
    Matrix4f view = getViewMatrix();
    Matrix4f projection = getProjectionMatrix();
    return projection.multiply(view); // Assuming Matrix4f.multiply() is column-major
  }

  @Override
  public Matrix4f getViewMatrix() {
    Transform t = getTransform();
    Vector3f pos = t.getPosition();
    Vector3f fwd = t.getForward();

    Vector3f right = fwd.cross(new Vector3f(0, -1, 0)).normalizeLocal();
    Vector3f up = right.cross(fwd).normalizeLocal();

    return Matrix4f.lookAt(pos, pos.add(fwd), up);
  }
}
