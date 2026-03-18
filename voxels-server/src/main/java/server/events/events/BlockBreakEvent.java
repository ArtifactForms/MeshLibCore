package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

public class BlockBreakEvent extends CancellableEvent {

  private final UUID playerId;

  private final int x;
  private final int y;
  private final int z;

  public BlockBreakEvent(UUID player, int x, int y, int z) {
    this.playerId = player;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }
}
