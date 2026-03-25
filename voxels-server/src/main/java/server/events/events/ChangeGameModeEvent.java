package server.events.events;

import java.util.UUID;

import common.game.GameMode;
import server.events.CancellableEvent;

/**
 * Event fired when a player attempts to change game mode.
 *
 * <p>This event is triggered on the server before the player's game mode is actually updated. It
 * allows listeners to intercept, validate, or prevent the change.
 *
 * <p>Listeners may cancel this event to block the game mode change. If cancelled, the player's game
 * mode will remain unchanged.
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>Permission checks (e.g. restricting creative mode access)
 *   <li>Custom game rules or server logic
 *   <li>Logging or analytics
 *   <li>Overriding or modifying game mode transitions
 * </ul>
 *
 * <p>Note: This event does not perform any validation itself and assumes that all input data is
 * already sanitized by the calling use case.
 */
public class ChangeGameModeEvent extends CancellableEvent {

  /** The unique identifier of the player whose game mode is being changed. */
  private final UUID playerId;

  /** The target game mode the player is attempting to switch to. */
  private final GameMode gameMode;

  /**
   * Creates a new {@code ChangeGameModeEvent}.
   *
   * @param playerId The unique identifier of the player.
   * @param gameMode The target game mode.
   */
  public ChangeGameModeEvent(UUID playerId, GameMode gameMode) {
    this.playerId = playerId;
    this.gameMode = gameMode;
  }

  /** @return The unique identifier of the player. */
  public UUID getPlayerId() {
    return playerId;
  }

  /** @return The target game mode. */
  public GameMode getGameMode() {
    return gameMode;
  }
}
