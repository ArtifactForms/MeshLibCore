package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

public class PlayerChatEvent extends CancellableEvent {

  private final UUID playerId;

  private final String message;

  public PlayerChatEvent(UUID playerId, String message) {
    this.playerId = playerId;
    this.message = message;
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public String getMessage() {
    return message;
  }
}
