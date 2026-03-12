package server.events.events;

import server.events.CancellableEvent;
import server.player.ServerPlayer;

public class PlayerMoveEvent extends CancellableEvent {

  private final ServerPlayer player;

  private float x;
  private float y;
  private float z;

  private float yaw;
  private float pitch;

  public PlayerMoveEvent(ServerPlayer player, float x, float y, float z, float yaw, float pitch) {
    this.player = player;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public ServerPlayer getPlayer() {
    return player;
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
