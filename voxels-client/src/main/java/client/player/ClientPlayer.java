package client.player;

import java.util.UUID;
import math.Vector3f;

/**
 * Represents the local player on the client side. Holds position, rotation, and movement
 * attributes.
 */
public class ClientPlayer {

  private final UUID uuid;

  // Player position
  private final Vector3f position = new Vector3f();

  // Rotation in radians
  private float yaw;
  private float pitch;

  // Movement speed
  private float speed = 12f;

  private boolean teleportDirty = false;

  public ClientPlayer() {
    this.uuid = UUID.randomUUID();
  }

  public boolean consumeTeleportFlag() {
    boolean flag = teleportDirty;
    teleportDirty = false;
    return flag;
  }

  // -------------------------
  // Position
  // -------------------------

  public Vector3f getPosition() {
    return position;
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

  public void setPosition(float x, float y, float z) {
    position.set(x, y, z);
  }

  public void setPositionFromTeleport(float x, float y, float z) {
    this.position.set(x, y, z);
    this.teleportDirty = true;
  }

  public void setX(float x) {
    position.x = x;
  }

  public void setY(float y) {
    position.y = y;
  }

  public void setZ(float z) {
    position.z = z;
  }

  // -------------------------
  // Rotation
  // -------------------------

  public float getYaw() {
    return yaw;
  }

  public void setYaw(float yaw) {
    this.yaw = yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  // -------------------------
  // Attributes
  // -------------------------

  public float getSpeed() {
    return speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }

  // -------------------------
  // Identity
  // -------------------------

  public UUID getUuid() {
    return uuid;
  }
}
