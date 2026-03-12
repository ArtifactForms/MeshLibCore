package server.events.events;

import server.events.CancellableEvent;
import server.player.ServerPlayer;

public class BlockBreakEvent extends CancellableEvent {

  private final ServerPlayer player;

  private int x;
  private int y;
  private int z;

  public BlockBreakEvent(ServerPlayer player, int x, int y, int z) {
    this.player = player;
    this.x = x;
    this.y = y;
    this.z = z;
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
}
