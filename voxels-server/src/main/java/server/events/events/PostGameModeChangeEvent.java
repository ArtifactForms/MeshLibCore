package server.events.events;

import java.util.UUID;

import common.game.GameMode;
import server.events.GameEvent;

public class PostGameModeChangeEvent extends GameEvent {

  private final UUID playerId;

  private final GameMode gameMode;

  public PostGameModeChangeEvent(UUID playerId, GameMode gameMode) {
    this.playerId = playerId;
    this.gameMode = gameMode;
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public GameMode getGameMode() {
    return gameMode;
  }
}
