package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

public class PlayerInventoryClearedEvent extends CancellableEvent {

  private final UUID playerId;

  public PlayerInventoryClearedEvent(UUID playerId) {
    this.playerId = playerId;
  }

  public UUID getPlayerId() {
    return playerId;
  }
}
