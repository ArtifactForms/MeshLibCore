package engine.components;

import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.runtime.input.MouseMode;
import engine.scene.camera.Camera;
import engine.scene.camera.PerspectiveCamera;
import math.Mathf;
import math.Vector3f;

/**
 * Provides smooth first-person / fly-by camera controls.
 *
 * <p>This component enables free camera movement and mouse-look behavior similar to FPS or editor
 * cameras. Movement is frame-rate independent and supports acceleration, deceleration, and speed
 * boosting.
 *
 * <h2>Coordinate System</h2>
 *
 * <ul>
 *   <li>Right-handed coordinate system
 *   <li>Forward direction is derived from {@link engine.components.Transform#getForward()}
 *   <li>Up direction is derived from {@link engine.components.Transform#getUp()}
 *   <li>The engine assumes a <b>Y-down world</b> (negative Y is up)
 * </ul>
 *
 * <h2>Controls</h2>
 *
 * <ul>
 *   <li><b>W / S</b> – Move forward / backward
 *   <li><b>A / D</b> – Strafe left / right
 *   <li><b>SPACE</b> – Move up (world up)
 *   <li><b>SHIFT</b> – Move down
 *   <li><b>CTRL</b> – Speed boost
 *   <li><b>Mouse</b> – Look around (yaw & pitch)
 * </ul>
 *
 * <p>Mouse movement is smoothed using linear interpolation to reduce jitter. Pitch rotation is
 * clamped to prevent flipping.
 */
public class SmoothFlyByCameraControl extends AbstractComponent {

  /** Default mouse sensitivity multiplier */
  private static final float DEFAULT_MOUSE_SENSITIVITY = 10f;

  /** Default camera movement speed */
  private static final float DEFAULT_MOVE_SPEED = 30f;

  /** Maximum upward pitch angle in degrees */
  private static final float MAX_VERTICAL_ANGLE = 80f;

  /** Maximum downward pitch angle in degrees */
  private static final float MIN_VERTICAL_ANGLE = -80f;

  /** Mouse sensitivity factor */
  private float mouseSensitivity = DEFAULT_MOUSE_SENSITIVITY;

  /** Smoothed mouse delta for yaw */
  private float smoothedMouseX = 0f;

  /** Smoothed mouse delta for pitch */
  private float smoothedMouseY = 0f;

  /** Interpolation factor for mouse smoothing */
  private float mouseSmoothingFactor = 0.25f;

  /** Base movement speed */
  private float moveSpeed = DEFAULT_MOVE_SPEED;

  /** Acceleration factor when movement input is applied */
  private float acceleration = 10f;

  /** Deceleration factor when movement input stops */
  private float deceleration = 8f;

  /** Multiplier applied when speed boost key is pressed */
  private float speedBoostMultiplier = 3f;

  /** Current interpolated movement velocity */
  private final Vector3f currentVelocity = new Vector3f();

  /** Target velocity derived from input */
  private final Vector3f targetVelocity = new Vector3f();

  /** Cached camera target position */
  private final Vector3f target = new Vector3f();

  /** Input provider */
  private final Input input;

  /** Controlled camera instance */
  private final Camera camera;

  /**
   * Creates a new smooth fly-by camera controller.
   *
   * @param input input system used to query keyboard and mouse state
   * @param camera camera to control
   * @throws IllegalArgumentException if input or camera is {@code null}
   */
  public SmoothFlyByCameraControl(Input input, PerspectiveCamera camera) {
    if (input == null || camera == null) {
      throw new IllegalArgumentException("Input and Camera cannot be null.");
    }
    this.input = input;
    this.camera = camera;
  }

  /**
   * Updates camera rotation and movement.
   *
   * <p>This method should be called once per frame. Mouse input controls yaw and pitch, while
   * keyboard input controls movement. Velocity is smoothly interpolated using acceleration and
   * deceleration.
   *
   * @param tpf time per frame (delta time in seconds)
   */
  @Override
  public void onUpdate(float tpf) {
    float rawMouseX = input.getMouseDeltaX() * mouseSensitivity * tpf;
    float rawMouseY = input.getMouseDeltaY() * mouseSensitivity * tpf;

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

    if (!currentVelocity.isZero()) {
      applyMovement(currentVelocity, tpf);
    }

    updateTarget();
  }

  /**
   * Applies mouse-based rotation to the camera.
   *
   * <p>Yaw is applied around the Y-axis, pitch around the X-axis. Pitch rotation is clamped to
   * avoid camera flipping.
   *
   * @param mouseX smoothed horizontal mouse delta
   * @param mouseY smoothed vertical mouse delta
   */
  private void handleRotation(float mouseX, float mouseY) {
    camera.getTransform().rotate(0, Mathf.toRadians(mouseX), 0);

    Vector3f rotation = camera.getTransform().getRotation();

    rotation.x =
        Mathf.clamp(
            rotation.x - Mathf.toRadians(mouseY), // Changed + to - for intuitive Y-down look
            Mathf.toRadians(MIN_VERTICAL_ANGLE),
            Mathf.toRadians(MAX_VERTICAL_ANGLE));

    camera.getTransform().setRotation(rotation);
  }

  /**
   * Computes the desired movement direction based on keyboard input.
   *
   * <p>Movement directions are derived from the camera's local forward, right, and up vectors to
   * ensure consistent behavior regardless of camera orientation.
   */
  private void updateTargetVelocity() {
    targetVelocity.set(0, 0, 0);

    Vector3f forward = camera.getTransform().getForward();
    Vector3f right = camera.getTransform().getRight();
    Vector3f worldUp = new Vector3f(0, -1, 0);

    if (input.isKeyPressed(Key.W)) targetVelocity.addLocal(forward);
    if (input.isKeyPressed(Key.S)) targetVelocity.addLocal(forward.negate());
    if (input.isKeyPressed(Key.A)) targetVelocity.addLocal(right.negate());
    if (input.isKeyPressed(Key.D)) targetVelocity.addLocal(right);

    if (input.isKeyPressed(Key.SPACE)) targetVelocity.addLocal(worldUp);
    if (input.isKeyPressed(Key.SHIFT)) targetVelocity.addLocal(worldUp.negate());

    float speedMultiplier = input.isKeyPressed(Key.CTRL) ? speedBoostMultiplier : 1f;

    if (!targetVelocity.isZero()) {
      targetVelocity.normalizeLocal().multLocal(speedMultiplier);
    }
  }

  /**
   * Applies the calculated movement to the camera position.
   *
   * @param velocity movement direction (normalized)
   * @param tpf time per frame
   */
  private void applyMovement(Vector3f velocity, float tpf) {
    Vector3f position = camera.getTransform().getPosition();
    position.addLocal(velocity.mult(moveSpeed * tpf));
    camera.getTransform().setPosition(position);
  }

  /** Updates the camera target so it always looks forward. */
  private void updateTarget() {
    Vector3f position = camera.getTransform().getPosition();
    Vector3f forward = camera.getTransform().getForward();
    target.set(position).addLocal(forward);
    camera.setTarget(target);
  }

  /** Enables relative mouse mode when the component is attached. */
  @Override
  public void onAttach() {
    input.setMouseMode(MouseMode.LOCKED);
  }

  /** Restores absolute mouse mode when the component is detached. */
  @Override
  public void onDetach() {
    input.setMouseMode(MouseMode.ABSOLUTE);
  }

  /** @return current movement speed */
  public float getMoveSpeed() {
    return moveSpeed;
  }

  /** @param moveSpeed new movement speed */
  public void setMoveSpeed(float moveSpeed) {
    this.moveSpeed = moveSpeed;
  }

  /** @return mouse sensitivity factor */
  public float getMouseSensitivity() {
    return mouseSensitivity;
  }

  /** @param mouseSensitivity new mouse sensitivity */
  public void setMouseSensitivity(float mouseSensitivity) {
    this.mouseSensitivity = mouseSensitivity;
  }

  /** @return acceleration factor */
  public float getAcceleration() {
    return acceleration;
  }

  /** @param acceleration new acceleration factor */
  public void setAcceleration(float acceleration) {
    this.acceleration = acceleration;
  }

  /** @return deceleration factor */
  public float getDeceleration() {
    return deceleration;
  }

  /** @param deceleration new deceleration factor */
  public void setDeceleration(float deceleration) {
    this.deceleration = deceleration;
  }

  /** @return mouse smoothing factor */
  public float getMouseSmoothingFactor() {
    return mouseSmoothingFactor;
  }

  /** @param mouseSmoothingFactor new smoothing factor */
  public void setMouseSmoothingFactor(float mouseSmoothingFactor) {
    this.mouseSmoothingFactor = mouseSmoothingFactor;
  }

  /** @return speed boost multiplier */
  public float getSpeedBoostMultiplier() {
    return speedBoostMultiplier;
  }

  /** @param speedBoostMultiplier new speed boost multiplier */
  public void setSpeedBoostMultiplier(float speedBoostMultiplier) {
    this.speedBoostMultiplier = speedBoostMultiplier;
  }
}
