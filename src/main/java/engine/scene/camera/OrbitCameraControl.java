package engine.scene.camera;

import engine.components.AbstractComponent;
import engine.input.Input;
import engine.input.Key;
import engine.input.MouseInput;
import math.Mathf;
import math.Vector3f;

/**
 * Editor-style orbit camera control component.
 *
 * <p>This component handles user input to manipulate an {@link OrbitCamera}. It supports three main
 * interaction modes:
 *
 * <ul>
 *   <li><b>Rotation:</b> Middle Mouse Button (MMB) to rotate around the target.
 *   <li><b>Panning:</b> Shift + MMB for 1:1 pixel-perfect translation in the view plane.
 *   <li><b>Zooming:</b> Mouse wheel for exponential distance scaling.
 * </ul>
 */
public class OrbitCameraControl extends AbstractComponent {

  /** Default multiplier for camera rotation speed. */
  private static final float DEFAULT_ROTATE_SENSITIVITY = 15.0f;

  /** Default multiplier for camera zoom speed. */
  private static final float DEFAULT_ZOOM_SENSITIVITY = 10.0f;

  private final Input input;
  private final OrbitCamera camera;

  private float rotateSensitivity = DEFAULT_ROTATE_SENSITIVITY;
  private float zoomSensitivity = DEFAULT_ZOOM_SENSITIVITY;

  /**
   * Constructs a new OrbitCameraControl.
   *
   * @param input The input system to poll for mouse and keyboard states.
   * @param camera The orbit camera to be controlled.
   * @throws IllegalArgumentException if input or camera is null.
   */
  public OrbitCameraControl(Input input, OrbitCamera camera) {
    if (input == null || camera == null) {
      throw new IllegalArgumentException("Input and OrbitCamera cannot be null.");
    }
    this.input = input;
    this.camera = camera;
  }

  /**
   * Updates the camera state based on current user input.
   *
   * @param tpf Time per frame (delta time) in seconds.
   */
  @Override
  public void onUpdate(float tpf) {
    if (input.isMousePressed(MouseInput.CENTER)) {

      // ------------------------------------------------------------
      // Panning (SHIFT + MMB)
      // Uses trigonometry to map mouse pixels to world units,
      // ensuring the point under the cursor stays under the cursor.
      // ------------------------------------------------------------
      if (input.isKeyPressed(Key.SHIFT)) {
        float dx = input.getMouseDeltaX();
        float dy = input.getMouseDeltaY();

        float distance = camera.getDistance();
        float fovY = camera.getVerticalFOV();
        float aspectRatio = camera.getAspectRatio();

        // Calculate visible viewport height at the target's distance
        float worldHeightAtDistance = 2.0f * distance * (float) Math.tan(fovY / 2.0f);
        float worldWidthAtDistance = worldHeightAtDistance * aspectRatio;

        // Map pixel deltas to world unit deltas
        float worldUnitsPerPixelX = worldWidthAtDistance / input.getScreenWidth();
        float worldUnitsPerPixelY = worldHeightAtDistance / input.getScreenHeight();

        Vector3f right = camera.getTransform().getRight();
        Vector3f up = camera.getTransform().getUp();

        // Build the movement vector relative to camera orientation
        Vector3f panOffset =
            new Vector3f()
                .addLocal(right.mult(-dx * worldUnitsPerPixelX))
                .addLocal(up.mult(dy * worldUnitsPerPixelY));

        camera.pan(panOffset);
      }

      // ------------------------------------------------------------
      // Rotation (MMB only)
      // Updates the yaw and pitch of the orbit camera.
      // ------------------------------------------------------------
      else {
        float dx = input.getMouseDeltaX() * rotateSensitivity * tpf;
        float dy = input.getMouseDeltaY() * rotateSensitivity * tpf;

        camera.rotate(Mathf.toRadians(-dx), Mathf.toRadians(-dy));
      }
    }

    // ------------------------------------------------------------
    // Zoom (Scroll Wheel)
    // Adjusts the distance between the camera and the target point.
    // ------------------------------------------------------------
    float scroll = input.getMouseWheelDelta();
    if (scroll != 0f) {
      camera.zoom(-scroll * zoomSensitivity * tpf);
    }
  }

  /** @return The current rotation sensitivity multiplier. */
  public float getRotateSensitivity() {
    return rotateSensitivity;
  }

  /** @param rotateSensitivity The new rotation sensitivity multiplier. */
  public void setRotateSensitivity(float rotateSensitivity) {
    this.rotateSensitivity = rotateSensitivity;
  }

  /** @return The current zoom sensitivity multiplier. */
  public float getZoomSensitivity() {
    return zoomSensitivity;
  }

  /** @param zoomSensitivity The new zoom sensitivity multiplier. */
  public void setZoomSensitivity(float zoomSensitivity) {
    this.zoomSensitivity = zoomSensitivity;
  }
}
