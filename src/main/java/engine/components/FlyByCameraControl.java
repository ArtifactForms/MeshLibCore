package engine.components;

import engine.input.Input;
import engine.input.Key;
import engine.scene.camera.Camera;
import engine.scene.camera.PerspectiveCamera;
import math.Mathf;
import math.Vector3f;

public class FlyByCameraControl extends AbstractComponent {

  private final Vector3f forward = new Vector3f();

  private final Vector3f target = new Vector3f();

  private float mouseSensitivity = 10f;

  private float moveSpeed = 30f;

  private Input input;

  private Camera camera;

  // Additional parameters for controlling vertical look limits
  private float maxVerticalAngle = 80f; // Max pitch angle in degrees

  private float minVerticalAngle = -80f; // Min pitch angle in degrees

  public FlyByCameraControl(Input input, PerspectiveCamera camera) {
    if (input == null) {
      throw new IllegalArgumentException("Input cannot be null.");
    }
    if (camera == null) {
      throw new IllegalArgumentException("Camera cannot be null.");
    }
    this.input = input;
    this.camera = camera;
  }

  @Override
  public void update(float tpf) {
    float mouseX = input.getMouseDeltaX() * mouseSensitivity * tpf;
    float mouseY = input.getMouseDeltaY() * mouseSensitivity * tpf;

    float yaw = mouseX;
    // To avoid issues with yaw wrapping at 360 or -360 degrees, we can clamp yaw between -180 and
    // 180
    yaw = Mathf.clamp(yaw, -180f, 180f);

    camera.getTransform().rotate(0, Mathf.toRadians(yaw), 0);

    updateTarget();

    Vector3f rotation = camera.getTransform().getRotation();
    float currentPitch = rotation.x; // Current pitch around the X-axis
    currentPitch += Mathf.toRadians(mouseY); // Adjust based on mouse movement

    // Clamp the vertical rotation (pitch) to avoid excessive rotation
    currentPitch =
        Mathf.clamp(
            currentPitch, Mathf.toRadians(minVerticalAngle), Mathf.toRadians(maxVerticalAngle));

    rotation.x = currentPitch;
    camera.getTransform().setRotation(rotation);

    // Update camera's target based on position and rotation
    updateTarget();

    Vector3f velocity = calculateVelocity();

    if (velocity.length() > 0) {
      velocity.normalizeLocal();
      Vector3f position = camera.getTransform().getPosition();
      position.addLocal(velocity.mult(moveSpeed * tpf));
      camera.getTransform().setPosition(position);
      updateTarget();
    }

    input.center();
  }

  private Vector3f calculateVelocity() {
    Vector3f velocity = new Vector3f();
    Vector3f forward = camera.getTransform().getForward();
    Vector3f right = camera.getTransform().getRight();

    if (input.isKeyPressed(Key.W)) velocity.addLocal(forward);
    if (input.isKeyPressed(Key.S)) velocity.addLocal(forward.negate());
    if (input.isKeyPressed(Key.A)) velocity.addLocal(right.negate()); // Strafe left
    if (input.isKeyPressed(Key.D)) velocity.addLocal(right); // Strafe right

    if (input.isKeyPressed(Key.SPACE)) {
      velocity.addLocal(0, -1, 0);
    }
    if (input.isKeyPressed(Key.SHIFT)) {
      velocity.addLocal(0, 1, 0);
    }
    return velocity;
  }

  private void updateTarget() {
    Vector3f position = camera.getTransform().getPosition();
    Vector3f rotation = camera.getTransform().getRotation();
    float yaw = rotation.y;
    float pitch = rotation.x;

    forward
        .set(Mathf.cos(yaw) * Mathf.cos(pitch), Mathf.sin(pitch), Mathf.sin(yaw) * Mathf.cos(pitch))
        .normalizeLocal();

    target.set(position).addLocal(forward);

    camera.setTarget(target);
  }

  @Override
  public void onAttach() {
    // Not used yet
  }

  @Override
  public void onDetach() {
    // Not used yet
  }
}
