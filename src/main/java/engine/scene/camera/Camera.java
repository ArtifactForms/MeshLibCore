package engine.scene.camera;

import engine.components.Transform;
import math.Matrix4f;
import math.Vector3f;

/**
 * Represents a generic camera within a 3D scene. A camera defines the view and projection settings
 * necessary for rendering a 3D scene from a specific perspective. This interface abstracts common
 * properties and functionalities that all camera types share, including transformation, view
 * matrices, projection matrices, and interaction with screen-space coordinates.
 *
 * <p>Cameras are responsible for defining how 3D scenes are projected onto a 2D screen and can be
 * configured for various projection modes, such as perspective and orthographic projections.
 * Implementations should define their specific logic for projection and view matrix handling, field
 * of view adjustments, clipping plane configuration, and conversion between screen and world
 * coordinates.
 */
public interface Camera {

  /**
   * Sets the camera's target position in the world.
   *
   * <p>The target defines the point in the 3D world that the camera is focused on. This is
   * typically used for creating behaviors such as following an object or maintaining a fixed view
   * of a specific scene element. The target can be dynamically updated during runtime to adjust the
   * camera's view.
   *
   * @param target The new target for the camera to focus on, represented as a {@link Vector3f}.
   */
  void setTarget(Vector3f target);

  /**
   * Retrieves the current target position of the camera.
   *
   * <p>The target defines the point in the 3D world that the camera is currently focused on. This
   * is useful for determining the camera's orientation or calculating the view matrix. The target
   * remains constant unless explicitly changed using {@link #setTarget(Vector3f)}.
   *
   * @return The current target of the camera as a {@link Vector3f}.
   */
  Vector3f getTarget();

  /**
   * Retrieves the camera's transformation, including its position, rotation, and scale in the 3D
   * world.
   *
   * <p>The transformation is a {@link Transform} object that encapsulates the camera's position,
   * rotation, and scaling within the scene. It is used to compute the camera's world space
   * orientation and can be updated to change the camera's location or rotation in the scene.
   *
   * @return The camera's {@link Transform} representing its position, rotation, and scaling in the
   *     world.
   */
  Transform getTransform();

  /**
   * Retrieves the field of view (FOV) of the camera, applicable for perspective projections.
   *
   * <p>The field of view determines how wide or narrow the camera's perspective is, affecting how
   * much of the 3D scene is visible at any given time. This property is only relevant for
   * perspective cameras.
   *
   * @return The current field of view in degrees.
   */
  float getFieldOfView();

  /**
   * Sets the field of view (FOV) for perspective cameras.
   *
   * <p>The field of view defines how wide or narrow the camera's perspective is, which influences
   * the perception of depth in the scene. This property only applies to perspective cameras and has
   * no effect on orthographic cameras.
   *
   * @param fov The desired field of view in degrees.
   */
  void setFieldOfView(float fov);

  /**
   * Retrieves the near clipping plane distance for the camera.
   *
   * <p>The near clipping plane defines the closest distance at which objects will be rendered by
   * the camera. Any objects closer than this distance will not be visible. This value is crucial
   * for depth calculations and scene rendering.
   *
   * @return The near clipping plane distance.
   */
  float getNearPlane();

  /**
   * Sets the near clipping plane distance for the camera.
   *
   * <p>The near clipping plane defines the closest visible distance in the 3D world. Objects closer
   * than this plane will not be rendered. Adjusting this value helps prevent issues like z-fighting
   * and ensures proper visibility of objects.
   *
   * @param nearPlane The desired near clipping plane distance.
   */
  void setNearPlane(float nearPlane);

  /**
   * Retrieves the far clipping plane distance for the camera.
   *
   * <p>The far clipping plane defines the furthest distance at which objects will be rendered.
   * Objects farther than this distance will not be visible. This value is critical for controlling
   * the rendering of distant objects and depth precision.
   *
   * @return The far clipping plane distance.
   */
  float getFarPlane();

  /**
   * Sets the far clipping plane distance for the camera.
   *
   * <p>The far clipping plane defines the maximum visible distance for the camera. Objects beyond
   * this distance will not be rendered. Modifying this value affects the range of visible objects
   * in the scene and can help optimize performance by excluding distant geometry.
   *
   * @param farPlane The desired far clipping plane distance.
   */
  void setFarPlane(float farPlane);

  /**
   * Retrieves the current aspect ratio of the camera's viewport.
   *
   * <p>The aspect ratio is the ratio of the width to the height of the camera's viewport and
   * determines how the scene is projected onto the screen. The aspect ratio is used to adjust the
   * projection matrix for accurate rendering and proper scene alignment, especially in 3D.
   *
   * @return The current aspect ratio of the camera's viewport (width divided by height).
   */
  float getAspectRatio();

  /**
   * Sets the aspect ratio for the camera's viewport.
   *
   * <p>Changing the aspect ratio of the camera will affect how the scene is projected, ensuring
   * that the rendered image fits the viewport properly. This adjustment is typically necessary when
   * resizing the window or changing the display resolution.
   *
   * @param aspectRatio The desired aspect ratio for the camera's viewport (width divided by
   *     height).
   */
  void setAspectRatio(float aspectRatio);

  /**
   * Computes the view matrix for this camera.
   *
   * <p>The view matrix represents the transformation that converts world-space coordinates into
   * camera-space coordinates, based on the camera's position and orientation.
   *
   * @return The view matrix as a {@link Matrix4f}.
   */
  Matrix4f getViewMatrix();

  /**
   * Computes the projection matrix for this camera.
   *
   * <p>The projection matrix defines how the 3D scene is projected onto a 2D screen, taking into
   * account the field of view, aspect ratio, and near/far clipping planes. The specific
   * implementation depends on whether the camera uses perspective or orthographic projection.
   *
   * @return The projection matrix as a {@link Matrix4f}.
   */
  Matrix4f getProjectionMatrix();

  /**
   * Computes the combined view-projection matrix for this camera.
   *
   * <p>The view-projection matrix combines the view and projection transformations into a single
   * matrix, allowing objects to be transformed directly from world space to clip space.
   *
   * @return The combined view-projection matrix as a {@link Matrix4f}.
   */
  Matrix4f getViewProjectionMatrix();
}
