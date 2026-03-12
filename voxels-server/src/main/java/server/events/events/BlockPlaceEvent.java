package server.events.events;

import server.events.CancellableEvent;
import server.player.ServerPlayer;

public class BlockPlaceEvent extends CancellableEvent {

  private final ServerPlayer player;

  private int x;
  private int y;
  private int z;

  private short blockId;

  public BlockPlaceEvent(ServerPlayer player, int x, int y, int z, short blockId) {
    this.player = player;
    this.x = x;
    this.y = y;
    this.z = z;
    this.blockId = blockId;
  }

  public ServerPlayer getPlayer() {
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
