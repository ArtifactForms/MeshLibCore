package client.player;

import client.app.GameClient;
import client.settings.KeyBinds;
import engine.components.AbstractComponent;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.scene.camera.Camera;
import engine.scene.screen.GameScreen;
import math.Mathf;
import math.Vector3f;

public class PlayerController extends AbstractComponent {

  private static final float DEFAULT_MOUSE_SENSITIVITY = 10f;

  private static final float MAX_VERTICAL_ANGLE = 80f;
  private static final float MIN_VERTICAL_ANGLE = -80f;

  private static final float TELEPORT_ROTATION = 90f;

  private float mouseSensitivity = DEFAULT_MOUSE_SENSITIVITY;

  private float smoothedMouseX = 0f;
  private float smoothedMouseY = 0f;
  private float mouseSmoothingFactor = 0.25f;

  private float acceleration = 10f;
  private float deceleration = 8f;
  private float speedBoostMultiplier = 3f;

  private float syncSmoothing = 10f;

  private boolean skipNextSync = false;

  private final Vector3f currentVelocity = new Vector3f();
  private final Vector3f targetVelocity = new Vector3f();
  private final Vector3f target = new Vector3f();

  private final Vector3f syncTargetPosition = new Vector3f();
  private final Vector3f syncTargetRotation = new Vector3f();

  private final Input input;
  private final Camera camera;
  private final GameClient client;
  private final ClientPlayer player;

  public PlayerController(Input input, Camera camera, GameClient client) {

    if (input == null || camera == null || client == null) {
      throw new IllegalArgumentException("Input, Camera and Client cannot be null.");
    }

    this.input = input;
    this.camera = camera;
    this.client = client;
    this.player = client.getPlayer();
  }

  @Override
  public void onUpdate(float tpf) {
	GameScreen screen = getOwner().getScene().getTopScreen();
	
	if (screen == null || screen.blocksGameplay()) {
		return;
	}

    if (client.getView().getChatView().isOpen()) return;

    // --------------------------------------------------
    // SERVER SYNC TARGET
    // --------------------------------------------------

    Vector3f playerPos = player.getPosition();

    syncTargetPosition.set(playerPos.x, -playerPos.y, playerPos.z);

    syncTargetRotation.set(-player.getPitch(), player.getYaw(), 0);

    // --------------------------------------------------
    // TELEPORT HANDLING
    // --------------------------------------------------

    if (player.consumeTeleportFlag()) {

      camera.getTransform().setPosition(syncTargetPosition);
      camera.getTransform().setRotation(syncTargetRotation);

      currentVelocity.set(0, 0, 0);
      targetVelocity.set(0, 0, 0);

      smoothedMouseX = 0;
      smoothedMouseY = 0;

      skipNextSync = true;
    }

    //    // --------------------------------------------------
    //    // POSITION SYNC
    //    // --------------------------------------------------
    //
    //    Vector3f camPos = camera.getTransform().getPosition();
    //
    //    if (!skipNextSync) {
    //      camPos.lerpLocal(syncTargetPosition, syncSmoothing * tpf);
    //      camera.getTransform().setPosition(camPos);
    //    }
    //
    //    // --------------------------------------------------
    //    // ROTATION SYNC
    //    // --------------------------------------------------
    //
    //    Vector3f camRot = camera.getTransform().getRotation();
    //
    //    float rotDiff = camRot.distance(syncTargetRotation);
    //
    //    if (rotDiff > Mathf.toRadians(TELEPORT_ROTATION)) {
    //
    //      camera.getTransform().setRotation(syncTargetRotation);
    //
    //    } else if (!skipNextSync) {
    //
    //      camRot.lerpLocal(syncTargetRotation, syncSmoothing * tpf);
    //      camera.getTransform().setRotation(camRot);
    //    }
    //
    //    skipNextSync = false;

    // --------------------------------------------------
    // MOUSE LOOK
    // --------------------------------------------------

    float rawMouseX = input.getMouseDeltaX() * mouseSensitivity * tpf;
    float rawMouseY = input.getMouseDeltaY() * mouseSensitivity * tpf;

    smoothedMouseX = Mathf.lerp(smoothedMouseX, rawMouseX, mouseSmoothingFactor);
    smoothedMouseY = Mathf.lerp(smoothedMouseY, rawMouseY, mouseSmoothingFactor);

    handleRotation(smoothedMouseX, smoothedMouseY);

    // --------------------------------------------------
    // MOVEMENT
    // --------------------------------------------------

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

    // --------------------------------------------------
    // CAMERA TARGET
    // --------------------------------------------------

    updateCameraTarget();

    // --------------------------------------------------
    // NETWORK SYNC
    // --------------------------------------------------

    syncToPlayer();
  }

  private void handleRotation(float mouseX, float mouseY) {

    camera.getTransform().rotate(0, Mathf.toRadians(mouseX), 0);

    Vector3f rotation = camera.getTransform().getRotation();

    rotation.x =
        Mathf.clamp(
            rotation.x - Mathf.toRadians(mouseY),
            Mathf.toRadians(MIN_VERTICAL_ANGLE),
            Mathf.toRadians(MAX_VERTICAL_ANGLE));

    camera.getTransform().setRotation(rotation);
  }

  private void updateTargetVelocity() {

    targetVelocity.set(0, 0, 0);

    Vector3f forward = camera.getTransform().getForward();
    Vector3f right = camera.getTransform().getRight();
    Vector3f worldUp = new Vector3f(0, -1, 0);

    if (input.isKeyPressed(KeyBinds.walkForwards)) targetVelocity.addLocal(forward);
    if (input.isKeyPressed(KeyBinds.walkBackwards)) targetVelocity.subtractLocal(forward);
    if (input.isKeyPressed(KeyBinds.strafeLeft)) targetVelocity.subtractLocal(right);
    if (input.isKeyPressed(KeyBinds.strafeRight)) targetVelocity.addLocal(right);

    if (input.isKeyPressed(Key.SPACE)) targetVelocity.addLocal(worldUp);
    if (input.isKeyPressed(Key.SHIFT)) targetVelocity.subtractLocal(worldUp);

    float speedMultiplier = input.isKeyPressed(Key.CTRL) ? speedBoostMultiplier : 1f;

    if (!targetVelocity.isZero()) {
      targetVelocity.normalizeLocal().multLocal(speedMultiplier);
    }
  }

  private void applyMovement(Vector3f velocity, float tpf) {

    Vector3f position = camera.getTransform().getPosition();

    position.addLocal(velocity.mult(player.getSpeed() * tpf));

    camera.getTransform().setPosition(position);
  }

  private void updateCameraTarget() {

    Vector3f position = camera.getTransform().getPosition();
    Vector3f forward = camera.getTransform().getForward();

    target.set(position).addLocal(forward);

    camera.setTarget(target);
  }

  private void syncToPlayer() {

    Vector3f camPos = camera.getTransform().getPosition();
    Vector3f camRot = camera.getTransform().getRotation();

    player.setPosition(camPos.x, -camPos.y, camPos.z);

    player.setYaw(camRot.y);
    player.setPitch(-camRot.x);
  }

  @Override
  public void onAttach() {
    Vector3f pPos = player.getPosition();

    syncTargetPosition.set(pPos.x, -pPos.y, pPos.z);

    syncTargetRotation.set(-player.getPitch(), player.getYaw(), 0);

    camera.getTransform().setPosition(syncTargetPosition);
    camera.getTransform().setRotation(syncTargetRotation);
  }

  @Override
  public void onDetach() {
    // Do nothing
  }

  public void setMouseSensitivity(float mouseSensitivity) {
    this.mouseSensitivity = mouseSensitivity;
  }
}
