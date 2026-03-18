package server.events.events;

import java.util.UUID;

import server.events.GameEvent;

public class PlayerQuitEvent extends GameEvent {

  private final UUID playerId;

  private String quitMessage;

  public PlayerQuitEvent(UUID playerId, String quitMessage) {
    this.playerId = playerId;
    this.quitMessage = quitMessage;
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public String getQuitMessage() {
    return quitMessage;
  }

  public void setQuitMessage(String quitMessage) {
    this.quitMessage = quitMessage;
  }
}
