package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

public class PlayerPreJoinEvent extends CancellableEvent {

  private final UUID playerId;

  public PlayerPreJoinEvent(UUID playerId) {
    this.playerId = playerId;
  }

  public UUID getPlayerId() {
    return playerId;
  }
}
