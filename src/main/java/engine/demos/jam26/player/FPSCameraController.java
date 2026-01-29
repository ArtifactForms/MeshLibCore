package engine.demos.jam26.player;

import engine.components.AbstractComponent;
import engine.components.Transform;
import engine.input.Input;
import engine.input.Key;
import engine.input.MouseMode;
import engine.scene.camera.Camera;
import engine.scene.camera.PerspectiveCamera;
import math.Mathf;
import math.Vector3f;

/** Classic FPS-style camera controller. Fixed for Y-down coordinate systems where -Y is up. */
public class FPSCameraController extends AbstractComponent {

  private static final float DEFAULT_MOUSE_SENSITIVITY = 10f;
  private static final float DEFAULT_MOVE_SPEED = 20f;
  private static final float MAX_PITCH = 80f;
  private static final float MIN_PITCH = -80f;

  private final Input input;
  private final Camera camera;

  private float mouseSensitivity = DEFAULT_MOUSE_SENSITIVITY;
  private float moveSpeed = DEFAULT_MOVE_SPEED;
  private float acceleration = 12f;
  private float deceleration = 10f;
  private float speedBoostMultiplier = 2.5f;

  private float smoothedMouseX = 0f;
  private float smoothedMouseY = 0f;
  private float mouseSmoothingFactor = 0.25f;

  private final Vector3f currentVelocity = new Vector3f();
  private final Vector3f targetVelocity = new Vector3f();

  // Head bob
  private float bobTime = 0f;
  private float bobSpeed = 10f;
  private float bobAmount = 0.015f;
  private float sideAmount = 0.0025f;
  private float bobFade = 0f;
  private boolean headBob = true;

  public FPSCameraController(Input input, PerspectiveCamera camera) {
    if (input == null || camera == null) {
      throw new IllegalArgumentException("Input and Camera cannot be null.");
    }
    this.input = input;
    this.camera = camera;
  }

  @Override
  public void onUpdate(float tpf) {
    handleMouseLook(tpf);
    updateTargetVelocity();

    // Handle deceleration/interpolation
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

    updateCameraTarget();
    applyHeadBob(tpf);
  }

  private void handleMouseLook(float tpf) {
    float rawX = input.getMouseDeltaX() * mouseSensitivity * tpf;
    float rawY = input.getMouseDeltaY() * mouseSensitivity * tpf;

    smoothedMouseX = Mathf.lerp(smoothedMouseX, rawX, mouseSmoothingFactor);
    smoothedMouseY = Mathf.lerp(smoothedMouseY, rawY, mouseSmoothingFactor);

    // Yaw (Rotation around global Y-axis)
    camera.getTransform().rotate(0, Mathf.toRadians(smoothedMouseX), 0);

    // Pitch (Local X-axis)
    Vector3f rotation = camera.getTransform().getRotation();

    // FIX: Subtracting mouse delta to align with the corrected Y-down Forward vector math.
    // Moving mouse "up" (negative deltaY) now correctly looks up.
    rotation.x -= Mathf.toRadians(smoothedMouseY);
    rotation.x = Mathf.clamp(rotation.x, Mathf.toRadians(MIN_PITCH), Mathf.toRadians(MAX_PITCH));

    camera.getTransform().setRotation(rotation);
  }

  private void updateTargetVelocity() {
    targetVelocity.set(0, 0, 0);

    // Calculate movement vectors flattened to the XZ plane
    Vector3f forward = camera.getTransform().getForward();
    forward.y = 0;
    forward.normalizeLocal();

    Vector3f right = camera.getTransform().getRight();
    right.y = 0;
    right.normalizeLocal();

    // Keyboard Input
    if (input.isKeyPressed(Key.W)) targetVelocity.addLocal(forward);
    if (input.isKeyPressed(Key.S)) targetVelocity.subtractLocal(forward);
    if (input.isKeyPressed(Key.A)) targetVelocity.subtractLocal(right);
    if (input.isKeyPressed(Key.D)) targetVelocity.addLocal(right);

    float speedMultiplier = input.isKeyPressed(Key.CTRL) ? speedBoostMultiplier : 1f;

    if (!targetVelocity.isZero()) {
      targetVelocity.normalizeLocal().multLocal(speedMultiplier);
    }
  }

  private void applyMovement(Vector3f velocity, float tpf) {
    Vector3f delta = velocity.mult(moveSpeed * tpf);
    Transform transform = camera.getTransform();

    // Optional movement filtering (collision, etc.)
    MovementFilter filter = getOwner().getComponent(MovementFilter.class);
    if (filter != null) {
      delta = filter.filter(transform, delta, tpf);
    }

    camera.getTransform().translate(delta);
  }

  private void applyHeadBob(float tpf) {
    if (!headBob) return;

    float speed = currentVelocity.length();
    if (speed > 0.05f) {
      bobFade = Mathf.lerp(bobFade, 1f, tpf * 6f);
      bobTime += tpf * bobSpeed * speed;
    } else {
      bobFade = Mathf.lerp(bobFade, 0f, tpf * 6f);
      bobTime = 0f;
    }

    float bobY = Mathf.sin(bobTime) * bobAmount * bobFade;
    float bobX = Mathf.cos(bobTime * 0.5f) * sideAmount * bobFade;

    // Apply translation directly to the transform
    camera.getTransform().translate(bobX, bobY, 0f);
  }

  private void updateCameraTarget() {
    Vector3f position = camera.getTransform().getPosition();
    Vector3f forward = camera.getTransform().getForward();
    camera.setTarget(new Vector3f(position).addLocal(forward));
  }

  @Override
  public void onAttach() {
    input.setMouseMode(MouseMode.RELATIVE);
  }

  @Override
  public void onDetach() {
    input.setMouseMode(MouseMode.ABSOLUTE);
  }

  public void setMoveSpeed(float moveSpeed) {
    this.moveSpeed = moveSpeed;
  }

  public void setMouseSensitivity(float mouseSensitivity) {
    this.mouseSensitivity = mouseSensitivity;
  }

  public void setAcceleration(float acceleration) {
    this.acceleration = acceleration;
  }

  public void setDeceleration(float deceleration) {
    this.deceleration = deceleration;
  }

  public void setMouseSmoothingFactor(float mouseSmoothingFactor) {
    this.mouseSmoothingFactor = mouseSmoothingFactor;
  }

  public void setSpeedBoostMultiplier(float speedBoostMultiplier) {
    this.speedBoostMultiplier = speedBoostMultiplier;
  }

  public void setHeadBob(boolean headBob) {
    this.headBob = headBob;
  }
}
