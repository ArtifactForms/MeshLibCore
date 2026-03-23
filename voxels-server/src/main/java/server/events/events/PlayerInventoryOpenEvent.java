package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

public class PlayerInventoryOpenEvent extends CancellableEvent {

  private final UUID playerId;

  public PlayerInventoryOpenEvent(UUID playerId) {
    this.playerId = playerId;
  }

  public UUID getPlayerId() {
    return playerId;
  }
}
