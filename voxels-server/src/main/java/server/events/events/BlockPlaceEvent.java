package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

public class BlockPlaceEvent extends CancellableEvent {

  private final UUID player;

  private int x;
  private int y;
  private int z;

  private short blockId;

  public BlockPlaceEvent(UUID player, int x, int y, int z, short blockId) {
    this.player = player;
    this.x = x;
    this.y = y;
    this.z = z;
    this.blockId = blockId;
  }

  public UUID getPlayer() {
    return player;
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

  public short getBlockId() {
    return blockId;
  }
}
