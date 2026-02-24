package demos.collision;

import engine.components.AbstractComponent;
import engine.components.MovementInputConsumer;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.runtime.input.MouseMode;
import engine.scene.camera.Camera;
import math.Mathf;
import math.Vector3f;

/**
 * First-person controller component.
 *
 * <p>Responsibilities:
 *
 * <ul>
 *   <li>Handles mouse look (yaw + clamped pitch)
 *   <li>Generates normalized movement direction vectors
 *   <li>Forwards movement intent to a {@link MovementInputConsumer}
 *   <li>Synchronizes the active camera transform
 * </ul>
 *
 * <p>This component does NOT perform physics or apply movement directly. It only produces movement
 * intent and view rotation.
 */
public class FPSControl extends AbstractComponent {

  private static final float DEFAULT_MOUSE_SENSITIVITY = 10f;
  private static final float MAX_PITCH = 80f;
  private static final float MIN_PITCH = -80f;

  private float mouseSensitivity = DEFAULT_MOUSE_SENSITIVITY;
  private float mouseSmoothingFactor = 0.25f;

  private float smoothedMouseX = 0f;
  private float smoothedMouseY = 0f;
  private float currentPitch = 0f;

  /** Vertical camera offset relative to the player position. */
  private float eyeHeight = 0.8f;

  private final Input input;
  private final MovementInputConsumer consumer;

  public FPSControl(Input input, MovementInputConsumer consumer) {
    this.input = input;
    this.consumer = consumer;
  }

  @Override
  public void onAttach() {
    input.setMouseMode(MouseMode.LOCKED);
  }

  @Override
  public void onDetach() {
    input.setMouseMode(MouseMode.ABSOLUTE);
  }

  @Override
  public void onUpdate(float tpf) {
    handleMouseLook(tpf);

    Vector3f moveDirection = calculateMoveDirection();
    if (moveDirection.lengthSquared() > 0) {
      consumer.addMovementInput(moveDirection);
    }

    if (input.isKeyPressed(Key.SPACE)) {
      consumer.jump();
    }

    syncCamera();
  }

  /**
   * Synchronizes the active camera with the player's transform.
   *
   * <p>The player's yaw is applied from the owner's transform. The pitch is maintained locally and
   * clamped. The camera is positioned at eye height and its target is updated.
   */
  private void syncCamera() {
    Camera activeCamera = getOwner().getScene().getActiveCamera();
    if (activeCamera == null) return;

    // Combine yaw (from player) and pitch (local)
    float playerYaw = getOwner().getTransform().getRotation().y;
    activeCamera.getTransform().setRotation(currentPitch, playerYaw, 0);

    // Position camera at eye height
    activeCamera
        .getTransform()
        .setPosition(getOwner().getTransform().getPosition().add(0, -eyeHeight, 0));

    // Update camera target
    Vector3f forward = activeCamera.getTransform().getForward();
    activeCamera.setTarget(activeCamera.getTransform().getPosition().add(forward));
  }

  /**
   * Calculates a normalized movement direction based on keyboard input.
   *
   * <p>The direction is derived from the camera's forward and right vectors, flattened onto the XZ
   * plane to prevent vertical movement.
   */
  private Vector3f calculateMoveDirection() {
    Vector3f dir = new Vector3f();
    Camera activeCamera = getOwner().getScene().getActiveCamera();
    if (activeCamera == null) return dir;

    Vector3f forward = activeCamera.getTransform().getForward();
    Vector3f right = activeCamera.getTransform().getRight();

    // Flatten to XZ plane
    forward.y = 0;
    right.y = 0;
    forward.normalizeLocal();
    right.normalizeLocal();

    if (input.isKeyPressed(Key.W)) dir.addLocal(forward);
    if (input.isKeyPressed(Key.S)) dir.subtractLocal(forward);
    if (input.isKeyPressed(Key.A)) dir.subtractLocal(right);
    if (input.isKeyPressed(Key.D)) dir.addLocal(right);

    if (dir.lengthSquared() > 0) {
      dir.normalizeLocal();
    }

    return dir;
  }

  /**
   * Handles mouse input for first-person view rotation.
   *
   * <ul>
   *   <li>Yaw is applied directly to the player transform (unlimited rotation).
   *   <li>Pitch is stored locally and clamped to prevent over-rotation.
   * </ul>
   */
  private void handleMouseLook(float tpf) {
    float rawX = input.getMouseDeltaX() * mouseSensitivity * tpf;
    float rawY = input.getMouseDeltaY() * mouseSensitivity * tpf;

    smoothedMouseX = Mathf.lerp(smoothedMouseX, rawX, mouseSmoothingFactor);
    smoothedMouseY = Mathf.lerp(smoothedMouseY, rawY, mouseSmoothingFactor);

    // Apply yaw directly to the player (unrestricted rotation)
    getOwner().getTransform().rotate(0, Mathf.toRadians(smoothedMouseX), 0);

    // Compute and clamp pitch
    currentPitch -= Mathf.toRadians(smoothedMouseY);
    currentPitch =
        Mathf.clamp(currentPitch, Mathf.toRadians(MIN_PITCH), Mathf.toRadians(MAX_PITCH));
  }

  public void setMouseSensitivity(float mouseSensitivity) {
    this.mouseSensitivity = mouseSensitivity;
  }

  public void setMouseSmoothingFactor(float mouseSmoothingFactor) {
    this.mouseSmoothingFactor = mouseSmoothingFactor;
  }

  public void setEyeHeight(float eyeHeight) {
    this.eyeHeight = eyeHeight;
  }
}
