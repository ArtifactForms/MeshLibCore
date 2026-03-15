package client.player;

import java.util.UUID;

import common.player.PlayerData;

/** Represents the local player on the client side. */
public class ClientPlayer extends PlayerData {

  private boolean teleportDirty = false;

  public ClientPlayer(UUID uuid, String name) {
    super(uuid, name);
  }

  public boolean consumeTeleportFlag() {
    boolean flag = teleportDirty;
    teleportDirty = false;
    return flag;
  }

  public void setPositionFromTeleport(float x, float y, float z) {
    this.position.set(x, y, z);
    this.teleportDirty = true;
  }
}
