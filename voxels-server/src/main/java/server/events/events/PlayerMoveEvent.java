package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

public class PlayerMoveEvent extends CancellableEvent {

  private final UUID playerId;

  private float x;
  private float y;
  private float z;

  private float yaw;
  private float pitch;

  public PlayerMoveEvent(UUID playerId, float x, float y, float z, float yaw, float pitch) {
    this.playerId = playerId;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getZ() {
    return z;
  }

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public void setPosition(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
}
