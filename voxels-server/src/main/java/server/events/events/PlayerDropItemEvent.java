package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

public class PlayerDropItemEvent extends CancellableEvent {

  private final UUID player;

  private final short itemId;
  private final int amount;

  public PlayerDropItemEvent(UUID player, short itemId, int amount) {
    this.player = player;
    this.amount = amount;
    this.itemId = itemId;
  }

  public UUID getPlayerId() {
    return player;
  }

  public short getItemId() {
    return itemId;
  }

  public int getAmount() {
    return amount;
  }
}
