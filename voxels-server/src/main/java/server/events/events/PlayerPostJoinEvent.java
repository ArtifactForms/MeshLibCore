package server.events.events;

import java.util.UUID;

import server.events.GameEvent;

public class PlayerPostJoinEvent extends GameEvent {

  private final UUID playerId;

  public PlayerPostJoinEvent(UUID playerId) {
    this.playerId = playerId;
  }

  public UUID getPlayerId() {
    return playerId;
  }
}
