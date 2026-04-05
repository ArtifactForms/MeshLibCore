package server.usecases.changegamemode;

import java.util.UUID;

import common.game.GameMode;
import server.usecases.changegamemode.ChangeGamemode.ChangeGameModeRequest;

public class ChangeGameModeRequestModel implements ChangeGameModeRequest {

  private final UUID playerId;

  private final GameMode gameMode;

  public ChangeGameModeRequestModel(UUID playerId, GameMode gameMode) {
    this.playerId = playerId;
    this.gameMode = gameMode;
  }

  @Override
  public UUID getPlayerId() {
    return playerId;
  }

  @Override
  public GameMode getGameMode() {
    return gameMode;
  }
}
