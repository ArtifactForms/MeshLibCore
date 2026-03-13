package client.player;

import java.util.UUID;

public class RemotePlayer {

  private float x;
  private float y;
  private float z;

  private float yaw;
  private float pitch;

  private UUID uuid;
  private String name;

  public RemotePlayer(UUID uuid) {
    this.uuid = uuid;
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getZ() {
    return z;
  }

  public void setZ(float z) {
    this.z = z;
  }

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

  public UUID getUuid() {
    return uuid;
  }
}
