package engine.scene.camera;

import engine.components.Transform;
import math.Matrix4f;
import math.Ray3f;
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

  Vector3f getTarget();

  /**
   * Retrieves the camera's transformation.
   *
   * <p>The transformation defines the position, rotation, and scaling of the camera in the 3D
   * world. This transformation is used to compute the camera's position and orientation in world
   * space.
   *
   * @return The {@link Transform} representing the camera's position, rotation, and scaling.
   */
  Transform getTransform();

  /**
   * Retrieves the current view matrix of the camera.
   *
   * <p>The view matrix is used to transform world-space coordinates into camera (view) space. It is
   * typically derived from the camera's position and orientation in the 3D world.
   *
   * @return The view matrix as a {@link Matrix4f}.
   */
  Matrix4f getViewMatrix();

  /**
   * Retrieves the current projection matrix of the camera.
   *
   * <p>The projection matrix defines how a 3D scene is projected onto a 2D viewport, depending on
   * the camera's projection settings (perspective or orthographic).
   *
   * @return The projection matrix as a {@link Matrix4f}.
   */
  Matrix4f getProjectionMatrix();

  /**
   * Updates the view matrix based on the current transformation.
   *
   * <p>This method should recalculate the view matrix whenever the camera's position or orientation
   * has changed.
   */
  void updateViewMatrix();

  /**
   * Updates the projection matrix based on camera-specific settings.
   *
   * <p>This method should be called whenever changes are made to parameters like the field of view,
   * near or far clipping planes, or aspect ratio.
   */
  void updateProjectionMatrix();

  /**
   * Retrieves the field of view (FOV) for perspective cameras.
   *
   * <p>The field of view determines how wide or narrow the camera's view is and only applies to
   * perspective projections.
   *
   * @return The current field of view in degrees.
   */
  float getFieldOfView();

  /**
   * Sets the field of view (FOV) for perspective cameras.
   *
   * <p>This only has an effect on cameras configured for perspective projection.
   *
   * @param fov The desired field of view in degrees.
   */
  void setFieldOfView(float fov);

  /**
   * Retrieves the near clipping plane distance.
   *
   * <p>The near clipping plane defines the closest distance from the camera at which objects are
   * rendered. Objects closer than this distance will not be visible.
   *
   * @return The near clipping plane distance.
   */
  float getNearPlane();

  /**
   * Sets the near clipping plane distance.
   *
   * <p>This modifies how close an object must be to the camera to be visible.
   *
   * @param nearPlane The desired near clipping plane distance.
   */
  void setNearPlane(float nearPlane);

  /**
   * Retrieves the far clipping plane distance.
   *
   * <p>The far clipping plane defines the furthest distance from the camera at which objects are
   * rendered. Objects farther away than this distance will not be visible.
   *
   * @return The far clipping plane distance.
   */
  float getFarPlane();

  /**
   * Sets the far clipping plane distance.
   *
   * <p>This modifies how far objects can be from the camera to remain visible.
   *
   * @param farPlane The desired far clipping plane distance.
   */
  void setFarPlane(float farPlane);

  /**
   * Retrieves the current aspect ratio of the camera's viewport.
   *
   * <p>The aspect ratio is defined as the ratio of the viewport's width to its height and is used
   * to adjust the projection matrix accordingly.
   *
   * @return The aspect ratio of the viewport.
   */
  float getAspectRatio();

  /**
   * Sets the aspect ratio for the camera's viewport.
   *
   * <p>Changing the aspect ratio should trigger an update to the projection matrix.
   *
   * @param aspectRatio The desired aspect ratio.
   */
  void setAspectRatio(float aspectRatio);

  /**
   * Converts 2D screen coordinates to a 3D ray in world space.
   *
   * <p>This method is essential for raycasting operations, such as determining which objects in the
   * scene correspond to a given 2D screen-space click.
   *
   * @param screenX The x-coordinate on the screen.
   * @param screenY The y-coordinate on the screen.
   * @param viewportWidth The width of the viewport in pixels.
   * @param viewportHeight The height of the viewport in pixels.
   * @return A {@link Ray3f} representing the computed ray in 3D world space.
   */
  Ray3f createRay(float screenX, float screenY, int viewportWidth, int viewportHeight);

  /**
   * Converts 2D screen-space coordinates to their corresponding world-space coordinates.
   *
   * <p>This is the inverse of projection and can be used for operations like object picking or
   * determining intersections between screen-space inputs and 3D objects in the world.
   *
   * @param screenCoords The 2D screen-space coordinates to unproject.
   * @param viewportWidth The width of the viewport in pixels.
   * @param viewportHeight The height of the viewport in pixels.
   * @return The corresponding 3D world-space coordinates as a {@link Vector3f}.
   */
  Vector3f unproject(Vector3f screenCoords, int viewportWidth, int viewportHeight);
}
