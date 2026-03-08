package server.network.validation;

import server.player.ServerPlayer;

/** Interface for verifying if a block interaction is technically legal. */
public interface BlockActionValidator {
  /** @return true if the action is allowed, false if it should be rejected. */
  boolean isValid(ServerPlayer player, int x, int y, int z);
}
