package server.usecases.changegamemode;

import java.util.UUID;

import common.game.GameMode;

public interface ChangeGamemode {

  void execute(ChangeGameModeRequest request, ChangeGameModeResponse response);

  public interface ChangeGameModeRequest {

    UUID getPlayerId();

    GameMode getGameMode();
  }

  public interface ChangeGameModeResponse {

    void onGameModeChanged(UUID playerId, GameMode gameMode);

    void onFailed(UUID playerId);
  }
}
