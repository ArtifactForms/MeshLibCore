package server.events.events;

import java.util.UUID;

import common.game.GameMode;
import server.events.GameEvent;

/**
 * Event fired after a player's game mode has been successfully changed.
 *
 * <p>This event is dispatched once the new {@link GameMode} has already been applied
 * to the player. It is intended for post-processing logic such as syncing state,
 * updating UI, triggering effects, or notifying other systems.
 *
 * <p>This event is not cancellable.
 *
 * @see ChangeGameModeEvent
 */
public class PostGameModeChangeEvent extends GameEvent {

  /**
   * The unique identifier of the player whose game mode was changed.
   */
  private final UUID playerId;

  /**
   * The new game mode applied to the player.
   */
  private final GameMode gameMode;

  /**
   * Creates a new {@code PostGameModeChangeEvent}.
   *
   * @param playerId the unique identifier of the affected player
   * @param gameMode the new game mode applied to the player
   */
  public PostGameModeChangeEvent(UUID playerId, GameMode gameMode) {
    this.playerId = playerId;
    this.gameMode = gameMode;
  }

  /**
   * Returns the unique identifier of the player whose game mode was changed.
   *
   * @return the player ID
   */
  public UUID getPlayerId() {
    return playerId;
  }

  /**
   * Returns the new game mode applied to the player.
   *
   * @return the game mode
   */
  public GameMode getGameMode() {
    return gameMode;
  }
}