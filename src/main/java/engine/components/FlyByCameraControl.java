package engine.components;

import engine.input.Input;
import engine.input.Key;
import engine.input.MouseMode;
import engine.scene.camera.Camera;
import engine.scene.camera.PerspectiveCamera;
import math.Mathf;
import math.Vector3f;

/**
 * The {@code FlyByCameraControl} class provides first-person camera control functionality, allowing
 * the user to move the camera in all directions and adjust its orientation using mouse and keyboard
 * input. This control simulates a "fly-by" movement, typically used in 3D games or applications
 * that require free-form camera navigation.
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
 * movement, with vertical angle limits set for pitch to prevent excessive rotations.
 */
public class FlyByCameraControl extends AbstractComponent {

  private static final float DEFAULT_MOUSE_SENSITIVITY = 10f;

  private static final float DEFAULT_MOVE_SPEED = 30f;

  private static final float MAX_VERTICAL_ANGLE = 80f;

  private static final float MIN_VERTICAL_ANGLE = -80f;

  private final Vector3f forward = new Vector3f();

  private final Vector3f target = new Vector3f();

  private float mouseSensitivity = DEFAULT_MOUSE_SENSITIVITY;

  private float moveSpeed = DEFAULT_MOVE_SPEED;

  private Input input;

  private Camera camera;

  /**
   * Constructs a new {@code FlyByCameraControl} with the specified input and camera.
   *
   * @param input The {@link Input} instance to capture user input.
   * @param camera The {@link PerspectiveCamera} to control.
   * @throws IllegalArgumentException if either {@code input} or {@code camera} is {@code null}.
   */
  public FlyByCameraControl(Input input, PerspectiveCamera camera) {
    if (input == null || camera == null) {
      throw new IllegalArgumentException("Input and Camera cannot be null.");
    }
    this.input = input;
    this.camera = camera;
  }

  /**
   * Updates the camera control state, handling mouse input for camera rotation and keyboard input
   * for movement.
   *
   * <p>This method is typically called once per frame.
   *
   * @param tpf The time per frame (delta time) used for movement scaling.
   */
  @Override
  public void onUpdate(float tpf) {
    float mouseX = input.getMouseDeltaX() * mouseSensitivity * tpf;
    float mouseY = input.getMouseDeltaY() * mouseSensitivity * tpf;

    handleRotation(mouseX, mouseY);

    Vector3f velocity = calculateVelocity();
    if (velocity.length() > 0) {
      applyMovement(velocity, tpf);
    }
    updateTarget();
  }

  /**
   * Handles camera rotation based on mouse input, applying yaw and pitch to the camera's transform.
   * The vertical rotation (pitch) is clamped within defined limits to prevent excessive rotation.
   *
   * @param mouseX The mouse movement on the X-axis (horizontal movement).
   * @param mouseY The mouse movement on the Y-axis (vertical movement).
   */
  private void handleRotation(float mouseX, float mouseY) {
    // Handle yaw (left-right rotation)
    float yaw = mouseX;
    yaw = Mathf.clamp(yaw, -180f, 180f);
    camera.getTransform().rotate(0, Mathf.toRadians(yaw), 0);

    // Handle pitch (up-down rotation)
    Vector3f rotation = camera.getTransform().getRotation();
    float currentPitch = rotation.x;
    currentPitch += Mathf.toRadians(mouseY);
    currentPitch =
        Mathf.clamp(
            currentPitch, Mathf.toRadians(MIN_VERTICAL_ANGLE), Mathf.toRadians(MAX_VERTICAL_ANGLE));

    rotation.x = currentPitch;
    camera.getTransform().setRotation(rotation);
  }

  /**
   * Calculates the movement velocity vector based on the current keyboard input. The velocity is a
   * vector that indicates the desired direction of movement.
   *
   * @return A {@link Vector3f} representing the movement velocity.
   */
  private Vector3f calculateVelocity() {
    Vector3f velocity = new Vector3f();
    Vector3f forward = camera.getTransform().getForward();
    Vector3f right = camera.getTransform().getRight();

    // Movement controls (WASD, Space, Shift)
    if (input.isKeyPressed(Key.W)) velocity.addLocal(forward);
    if (input.isKeyPressed(Key.S)) velocity.addLocal(forward.negate());
    if (input.isKeyPressed(Key.A)) velocity.addLocal(right.negate());
    if (input.isKeyPressed(Key.D)) velocity.addLocal(right);
    if (input.isKeyPressed(Key.SPACE)) velocity.addLocal(0, -1, 0);
    if (input.isKeyPressed(Key.SHIFT)) velocity.addLocal(0, 1, 0);

    return velocity;
  }

  /**
   * Applies the calculated velocity to the camera's position, updating the camera's location based
   * on user movement input.
   *
   * @param velocity The movement velocity to apply.
   * @param tpf The time per frame (delta time) to scale the movement.
   */
  private void applyMovement(Vector3f velocity, float tpf) {
    velocity.normalizeLocal();
    Vector3f position = camera.getTransform().getPosition();
    position.addLocal(velocity.mult(moveSpeed * tpf));
    camera.getTransform().setPosition(position);
  }

  /**
   * Updates the target position for the camera. The target is a point in the scene that the camera
   * looks at, and it is updated based on the camera's current position and rotation.
   */
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

  /** This method is called when the component is attached to an entity. Currently not used. */
  @Override
  public void onAttach() {
      input.setMouseMode(MouseMode.RELATIVE);
  }

  /** This method is called when the component is detached from an entity. Currently not used. */
  @Override
  public void onDetach() {
      input.setMouseMode(MouseMode.ABSOLUTE);
  }

  /**
   * Returns the current movement speed of the camera.
   *
   * @return The movement speed in units per second.
   */
  public float getMoveSpeed() {
    return moveSpeed;
  }

  /**
   * Sets the movement speed of the camera.
   *
   * @param moveSpeed The new movement speed in units per second.
   */
  public void setMoveSpeed(float moveSpeed) {
    this.moveSpeed = moveSpeed;
  }

  /**
   * Returns the current mouse sensitivity used for camera rotation.
   *
   * <p>The mouse sensitivity determines how much the camera rotates based on mouse movement. Higher
   * sensitivity values result in larger rotations for smaller mouse movements.
   *
   * @return The current mouse sensitivity.
   */
  public float getMouseSensitivity() {
    return mouseSensitivity;
  }

  /**
   * Sets the mouse sensitivity used for camera rotation.
   *
   * <p>The mouse sensitivity determines how much the camera rotates based on mouse movement. Higher
   * sensitivity values result in larger rotations for smaller mouse movements.
   *
   * @param mouseSensitivity The new mouse sensitivity value.
   */
  public void setMouseSensitivity(float mouseSensitivity) {
    this.mouseSensitivity = mouseSensitivity;
  }
}
