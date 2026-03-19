package client.player;

import java.util.UUID;
import math.Vector3f;

public class RemotePlayer {

  private final Vector3f position = new Vector3f(); // smooth
  private final Vector3f targetPosition = new Vector3f(); // server

  private float yaw;
  private float pitch;

  private UUID uuid;
  private String name;

  public RemotePlayer(UUID uuid) {
    this.uuid = uuid;
  }

  public void setTargetPosition(float x, float y, float z) {
    targetPosition.set(x, y, z);
  }

  public void update(float tpf) {
    float alpha = 10f * tpf; // smoothing speed

    position.x += (targetPosition.x - position.x) * alpha;
    position.y += (targetPosition.y - position.y) * alpha;
    position.z += (targetPosition.z - position.z) * alpha;
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
