package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

/**
 * Fired when a player is about to be kicked from the server.
 *
 * <p>This event is {@link server.events.CancellableEvent cancellable}, allowing listeners to
 * prevent the kick from happening entirely.
 *
 * <p>The kick reason can also be modified by listeners before the kick is executed.
 *
 * <h3>Use Cases</h3>
 *
 * <ul>
 *   <li>Prevent kicks under certain conditions (e.g. admin protection)
 *   <li>Modify the kick message shown to the player
 *   <li>Log or audit player removals
 *   <li>Implement custom moderation systems
 * </ul>
 *
 * <h3>Cancellation Behavior</h3>
 *
 * <ul>
 *   <li>If the event is cancelled, the player will NOT be kicked.
 *   <li>If not cancelled, the kick proceeds using the (possibly modified) reason.
 * </ul>
 */
public class PlayerKickEvent extends CancellableEvent {

  private final UUID playerId;
  private final String playerName;
  private String reason;

  /**
   * Creates a new {@code PlayerKickEvent}.
   *
   * @param playerId the unique ID of the player being kicked
   * @param playerName the name of the player being kicked
   * @param reason the initial kick reason (can be modified)
   */
  public PlayerKickEvent(UUID playerId, String playerName, String reason) {
    this.playerId = playerId;
    this.playerName = playerName;
    this.reason = reason;
  }

  /**
   * Returns the unique ID of the player being kicked.
   *
   * @return the player's UUID
   */
  public UUID getPlayerId() {
    return playerId;
  }

  /**
   * Returns the name of the player being kicked.
   *
   * @return the player's name
   */
  public String getPlayerName() {
    return playerName;
  }

  /**
   * Returns the current kick reason.
   *
   * <p>This value may have been modified by event listeners.
   *
   * @return the kick reason
   */
  public String getReason() {
    return reason;
  }

  /**
   * Sets a new kick reason.
   *
   * <p>This allows listeners to customize the message shown to the player.
   *
   * @param reason the new kick reason
   */
  public void setReason(String reason) {
    this.reason = reason;
  }
}
