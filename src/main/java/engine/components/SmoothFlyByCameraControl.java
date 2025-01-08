package engine.components;

import engine.input.Input;
import engine.input.Key;
import engine.scene.camera.Camera;
import engine.scene.camera.PerspectiveCamera;
import math.Mathf;
import math.Vector3f;

/**
 * The {@code SmoothFlyByCameraControl} class provides first-person camera control functionality,
 * allowing the user to move the camera smoothly in all directions and adjust its orientation using
 * mouse and keyboard input. This control simulates a "fly-by" movement with acceleration and
 * deceleration, while smoothing rapid mouse movements for a better experience.
 *
 * <p>The movement is controlled by the following keys:
 *
 * <ul>
 *   <li>W: Move forward
 *   <li>S: Move backward
 *   <li>A: Move left (strafe)
 *   <li>D: Move right (strafe)
 *   <li>SPACE: Move up
 *   <li>SHIFT: Move down
 * </ul>
 *
 * <p>The mouse controls the camera's yaw (left-right) and pitch (up-down) based on the mouse
 * movement, with smoothing applied to reduce sudden, erratic movements.
 */
public class SmoothFlyByCameraControl extends AbstractComponent {

  private static final float DEFAULT_MOUSE_SENSITIVITY = 10f;
  private static final float DEFAULT_MOVE_SPEED = 30f;
  private static final float MAX_VERTICAL_ANGLE = 80f;
  private static final float MIN_VERTICAL_ANGLE = -80f;

  private final Vector3f forward = new Vector3f();
  private final Vector3f target = new Vector3f();

  private float mouseSensitivity = DEFAULT_MOUSE_SENSITIVITY;
  private float smoothedMouseX = 0f;
  private float smoothedMouseY = 0f;
  private float mouseSmoothingFactor = 0.7f;
  private float moveSpeed = DEFAULT_MOVE_SPEED;
  private float acceleration = 10f;
  private float deceleration = 5f;

  private Input input;
  private Camera camera;

  private Vector3f currentVelocity = new Vector3f();
  private Vector3f targetVelocity = new Vector3f();

  /**
   * Constructs a new {@code SmoothFlyByCameraControl} with the specified input and camera.
   *
   * @param input The {@link Input} instance to capture user input.
   * @param camera The {@link PerspectiveCamera} to control.
   * @throws IllegalArgumentException if either input or camera is null.
   */
  public SmoothFlyByCameraControl(Input input, PerspectiveCamera camera) {
    if (input == null || camera == null) {
      throw new IllegalArgumentException("Input and Camera cannot be null.");
    }
    this.input = input;
    this.camera = camera;
  }

  /**
   * Updates the camera's position and orientation based on user input.
   *
   * @param tpf Time per frame, used to adjust movement and smoothing.
   */
  @Override
  public void update(float tpf) {
    float rawMouseX = input.getMouseDeltaX() * mouseSensitivity * tpf;
    float rawMouseY = input.getMouseDeltaY() * mouseSensitivity * tpf;

    // Smooth mouse input using linear interpolation (lerp)
    smoothedMouseX = Mathf.lerp(smoothedMouseX, rawMouseX, mouseSmoothingFactor);
    smoothedMouseY = Mathf.lerp(smoothedMouseY, rawMouseY, mouseSmoothingFactor);

    handleRotation(smoothedMouseX, smoothedMouseY);
    updateTargetVelocity();

    if (targetVelocity.isZero()) {
      if (currentVelocity.length() > 0.01f) {
        currentVelocity.lerpLocal(Vector3f.ZERO, deceleration * tpf);
      } else {
        currentVelocity.set(0, 0, 0);
      }
    }

    currentVelocity.lerpLocal(targetVelocity, acceleration * tpf);

    if (currentVelocity.length() > 0) {
      applyMovement(currentVelocity, tpf);
    }

    updateTarget();
    input.center();
  }

  /**
   * Handles the camera's yaw and pitch rotation based on mouse movement.
   *
   * @param mouseX The smoothed mouse movement in the X direction.
   * @param mouseY The smoothed mouse movement in the Y direction.
   */
  private void handleRotation(float mouseX, float mouseY) {
    float yaw = mouseX;
    camera.getTransform().rotate(0, Mathf.toRadians(yaw), 0);

    Vector3f rotation = camera.getTransform().getRotation();
    float currentPitch = rotation.x + Mathf.toRadians(mouseY);
    currentPitch =
        Mathf.clamp(
            currentPitch, Mathf.toRadians(MIN_VERTICAL_ANGLE), Mathf.toRadians(MAX_VERTICAL_ANGLE));
    rotation.x = currentPitch;
    camera.getTransform().setRotation(rotation);
  }

  /** Updates the target velocity based on the current keyboard input. */
  private void updateTargetVelocity() {
    targetVelocity.set(0, 0, 0);

    Vector3f forward = camera.getTransform().getForward();
    Vector3f right = camera.getTransform().getRight();

    if (input.isKeyPressed(Key.W)) targetVelocity.addLocal(forward);
    if (input.isKeyPressed(Key.S)) targetVelocity.addLocal(forward.negate());
    if (input.isKeyPressed(Key.A)) targetVelocity.addLocal(right.negate());
    if (input.isKeyPressed(Key.D)) targetVelocity.addLocal(right);
    if (input.isKeyPressed(Key.SPACE)) targetVelocity.addLocal(0, -1, 0);
    if (input.isKeyPressed(Key.SHIFT)) targetVelocity.addLocal(0, 1, 0);

    targetVelocity.normalizeLocal();
  }

  /**
   * Applies the calculated movement to the camera's position based on the current velocity.
   *
   * @param velocity The current velocity vector.
   * @param tpf Time per frame, used to adjust movement speed.
   */
  private void applyMovement(Vector3f velocity, float tpf) {
    Vector3f position = camera.getTransform().getPosition();
    position.addLocal(velocity.mult(moveSpeed * tpf));
    camera.getTransform().setPosition(position);
  }

  /** Updates the camera's target position based on its forward direction. */
  private void updateTarget() {
    Vector3f position = camera.getTransform().getPosition();
    Vector3f rotation = camera.getTransform().getRotation();

    forward
        .set(
            Mathf.cos(rotation.y) * Mathf.cos(rotation.x),
            Mathf.sin(rotation.x),
            Mathf.sin(rotation.y) * Mathf.cos(rotation.x))
        .normalizeLocal();

    target.set(position).addLocal(forward);
    camera.setTarget(target);
  }

  /** Called when the component is attached to a scene node. */
  @Override
  public void onAttach() {
    // TODO Auto-generated method stub
  }

  /** Called when the component is detached from a scene node. */
  @Override
  public void onDetach() {
    // TODO Auto-generated method stub
  }

  /**
   * Returns the current movement speed of the camera.
   *
   * @return The movement speed.
   */
  public float getMoveSpeed() {
    return moveSpeed;
  }

  /**
   * Sets the movement speed of the camera.
   *
   * @param moveSpeed The new movement speed to set.
   */
  public void setMoveSpeed(float moveSpeed) {
    this.moveSpeed = moveSpeed;
  }

  /**
   * Returns the current mouse sensitivity.
   *
   * @return The mouse sensitivity.
   */
  public float getMouseSensitivity() {
    return mouseSensitivity;
  }

  /**
   * Sets the mouse sensitivity.
   *
   * @param mouseSensitivity The new mouse sensitivity to set.
   */
  public void setMouseSensitivity(float mouseSensitivity) {
    this.mouseSensitivity = mouseSensitivity;
  }

  /**
   * Returns the current acceleration factor.
   *
   * @return The acceleration factor.
   */
  public float getAcceleration() {
    return acceleration;
  }

  /**
   * Sets the acceleration factor for the camera's movement.
   *
   * @param acceleration The new acceleration factor to set.
   */
  public void setAcceleration(float acceleration) {
    this.acceleration = acceleration;
  }

  /**
   * Returns the current deceleration factor.
   *
   * @return The deceleration factor.
   */
  public float getDeceleration() {
    return deceleration;
  }

  /**
   * Sets the deceleration factor for the camera's movement.
   *
   * @param deceleration The new deceleration factor to set.
   */
  public void setDeceleration(float deceleration) {
    this.deceleration = deceleration;
  }

  /**
   * Returns the current mouse smoothing factor.
   *
   * @return The mouse smoothing factor.
   */
  public float getMouseSmoothingFactor() {
    return mouseSmoothingFactor;
  }

  /**
   * Sets the mouse smoothing factor, which controls how much the mouse input is smoothed.
   *
   * @param mouseSmoothingFactor The new smoothing factor to set.
   */
  public void setMouseSmoothingFactor(float mouseSmoothingFactor) {
    this.mouseSmoothingFactor = mouseSmoothingFactor;
  }
}
