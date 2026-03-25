package server.usecases.changegamemode;

import java.util.UUID;

import common.game.GameMode;

public interface ChangeGamemode {

  boolean execute(UUID playerId, GameMode gameMode);
}
