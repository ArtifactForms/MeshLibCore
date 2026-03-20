package client.player;

import java.util.UUID;

import client.models.CharacterModel;
import math.Vector3f;

public class RemotePlayer {

  private final Vector3f position = new Vector3f();

  private float yaw;
  private float pitch;

  private final Vector3f targetPosition = new Vector3f();

  private float targetYaw;
  private float targetPitch;

  private final UUID uuid;
  private String name;

  private final CharacterModel model;

  public RemotePlayer(UUID uuid) {
    this.uuid = uuid;
    this.model = new CharacterModel();
  }

  public void setTargetPosition(float x, float y, float z) {
    targetPosition.set(x, y, z);
  }

  public void setTargetRotation(float yaw, float pitch) {
    this.targetYaw = yaw;
    this.targetPitch = Math.max(-89f, Math.min(89f, pitch)); // clamp
  }

  public void update(float deltaTime) {

    // -------------------------
    // SNAP
    // -------------------------
    float dx = targetPosition.x - position.x;
    float dy = targetPosition.y - position.y;
    float dz = targetPosition.z - position.z;

    float distSq = dx * dx + dy * dy + dz * dz;

    if (distSq > 25f) { // > 5 blocks
      position.set(targetPosition);
      yaw = targetYaw;
      pitch = targetPitch;
      
      model.getTransform().setPosition(position.x, -position.y - 0.5f, position.z);
      model.getTransform().setRotation(0, yaw, 0);
      model.getHead().getTransform().setRotation(-pitch, 0, 0);
      return;
    }

    // -------------------------
    // SMOOTHING
    // -------------------------
    float posAlpha = 10f * deltaTime;
    float rotAlpha = 15f * deltaTime;

    // Position
    position.x += dx * posAlpha;
    position.y += dy * posAlpha;
    position.z += dz * posAlpha;

    // Rotation
    yaw = lerpAngle(yaw, targetYaw, rotAlpha);
    pitch += (targetPitch - pitch) * rotAlpha;

    // Move model
    model.getTransform().setPosition(position.x, -position.y - 0.5f, position.z);

    // Body Rotation (Yaw)
    model.getTransform().setRotation(0, yaw, 0);

    // Head Rotation (Pitch)
    model.getHead().getTransform().setRotation(-pitch, 0, 0);
  }

  private float lerpAngle(float current, float target, float alpha) {
    float diff = target - current;

    while (diff > 180f) diff -= 360f;
    while (diff < -180f) diff += 360f;

    return current + diff * alpha;
  }

  public float getX() {
    return position.x;
  }

  public float getY() {
    return position.y;
  }

  public float getZ() {
    return position.z;
  }

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CharacterModel getModel() {
    return model;
  }
}
