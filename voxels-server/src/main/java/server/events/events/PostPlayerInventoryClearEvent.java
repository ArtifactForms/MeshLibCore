package server.events.events;

import java.util.UUID;

import server.events.GameEvent;

public class PlayerInventoryClearEvent extends GameEvent {

  private final UUID playerId;

  public PlayerInventoryClearEvent(UUID playerId) {
    this.playerId = playerId;
  }

  public UUID getPlayerId() {
    return playerId;
  }
}
