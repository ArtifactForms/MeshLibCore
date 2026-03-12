package server.events.events;

import server.events.CancellableEvent;
import server.player.ServerPlayer;

public class PlayerJoinEvent extends CancellableEvent {

  private final ServerPlayer player;

  private int spawnX;
  private int spawnY;
  private int spawnZ;
  private String joinMessage;

  public PlayerJoinEvent(
      ServerPlayer player, String joinMessage, int spawnX, int spawnY, int spawnZ) {
    this.player = player;
    this.joinMessage = joinMessage;
    this.spawnX = spawnX;
    this.spawnY = spawnY;
    this.spawnZ = spawnZ;
  }

  public ServerPlayer getPlayer() {
    return player;
  }

  public int getSpawnX() {
    return spawnX;
  }

  public int getSpawnY() {
    return spawnY;
  }

  public int getSpawnZ() {
    return spawnZ;
  }

  public void setSpawn(int x, int y, int z) {
    this.spawnX = x;
    this.spawnY = y;
    this.spawnZ = z;
  }

  public String getJoinMessage() {
    return joinMessage;
  }

  public void setJoinMessage(String joinMessage) {
    this.joinMessage = joinMessage;
  }
}
