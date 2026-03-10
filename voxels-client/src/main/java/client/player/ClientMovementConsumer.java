package client.player;

import client.app.ApplicationContext;
import common.network.packets.PlayerMovePacket;
import common.world.SoundEffect;
import engine.scene.SceneNode;
import engine.scene.audio.SoundManager;
import math.Mathf;
import math.Vector3f;

public class ClientMovementConsumer {

  private Vector3f position = new Vector3f();

  private float walkSpeed = 4.3f;
  private float sprintSpeed = 6.5f;

  private float speed;
  private boolean sprint;

  private float walked;
  private float bobTime;

  private float headBobY;
  private float headBobRot;
  private float headBobRoll;

  private float lerpFactor = 0.25f;

  private SceneNode player;

  public ClientMovementConsumer(SceneNode player) {
    this.player = player;
  }

  public void addMovementInput(Vector3f direction, float tpf) {

    Vector3f oldPosition = new Vector3f(position);

    speed = sprint ? sprintSpeed : walkSpeed;

    /* ---------------- MOVEMENT ---------------- */

    position.x += direction.x * speed * tpf;
    position.z += direction.z * speed * tpf;

    /* ---------------- TERRAIN SNAP ---------------- */

    if (ApplicationContext.chunkManager != null) {
      float groundHeight = ApplicationContext.chunkManager.getTerrainHeight(position.x, position.z);
      position.y = -groundHeight;
    }

    /* ---------------- NETWORK ---------------- */

    sendMovementPacket();

    /* ---------------- VISUAL SMOOTHING ---------------- */

    Vector3f visual = player.getTransform().getPosition();

    visual.x = Mathf.lerp(visual.x, position.x, lerpFactor);
    visual.y = Mathf.lerp(visual.y, position.y, lerpFactor);
    visual.z = Mathf.lerp(visual.z, position.z, lerpFactor);

    player.getTransform().setPosition(visual);

    /* ---------------- DISTANCE ---------------- */

    float distance =
        Mathf.sqrt(
            (position.x - oldPosition.x) * (position.x - oldPosition.x)
                + (position.z - oldPosition.z) * (position.z - oldPosition.z));

    walked += distance;

    /* ---------------- HEAD BOB ---------------- */

    updateHeadBob(direction, tpf);

    /* ---------------- FOOTSTEPS ---------------- */

    playFootsteps();
  }

  private void updateHeadBob(Vector3f direction, float tpf) {

    boolean moving = direction.lengthSquared() > 0.0001f;

    if (moving) {

      float bobSpeed = sprint ? 11.5f : 8.5f;
      bobTime += tpf * bobSpeed;

      float amplitudeY = sprint ? 0.014f : 0.010f;
      float amplitudeRot = sprint ? 0.45f : 0.30f;
      float amplitudeRoll = sprint ? 0.30f : 0.20f;

      float sin = Mathf.sin(bobTime);
      float cos = Mathf.cos(bobTime);

      headBobY = sin * amplitudeY;
      headBobRot = cos * Mathf.toRadians(amplitudeRot);
      headBobRoll = cos * Mathf.toRadians(amplitudeRoll);

    } else {

      headBobY = Mathf.lerp(headBobY, 0f, 0.15f);
      headBobRot = Mathf.lerp(headBobRot, 0f, 0.15f);
      headBobRoll = Mathf.lerp(headBobRoll, 0f, 0.15f);
    }

    if (ApplicationContext.fpsController != null) {
      ApplicationContext.fpsController.setHeadBob(headBobY, headBobRot, headBobRoll);
    }
  }

  private void playFootsteps() {

    float stepDistance = sprint ? sprintSpeed / 2 : walkSpeed / 2;

    while (walked > stepDistance) {

      String[] sounds =
          new String[] {
            SoundEffect.FOOT_STEP_GRASS_1,
            SoundEffect.FOOT_STEP_GRASS_2,
            SoundEffect.FOOT_STEP_GRASS_3,
            SoundEffect.FOOT_STEP_GRASS_4
          };
      int r = (int) (Math.random() * 4);
      SoundManager.playEffect(sounds[r]);

      walked -= stepDistance;
    }
  }

  private void sendMovementPacket() {

    if (ApplicationContext.network == null) return;

    float yaw = 0;
    float pitch = 0;

    if (ApplicationContext.activeScene != null
        && ApplicationContext.activeScene.getActiveCamera() != null) {

      Vector3f rot = ApplicationContext.activeScene.getActiveCamera().getTransform().getRotation();

      pitch = rot.x;
      yaw = rot.y;
    }

    ApplicationContext.network.send(
        new PlayerMovePacket(position.x, -position.y, position.z, yaw, pitch));
  }

  public void applyServerCorrection(float x, float y, float z) {
    position.x = x;
    position.y = -y;
    position.z = z;
  }

  public void sprint(boolean sprint) {
    this.sprint = sprint;
  }

  public Vector3f getPosition() {
    return position;
  }
}
