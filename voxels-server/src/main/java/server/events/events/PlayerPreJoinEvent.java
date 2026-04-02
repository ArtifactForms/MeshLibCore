package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

/**
 * Event fired before a player is allowed to join the server.
 *
 * <p>This is the earliest stage of the player connection lifecycle. The player has been identified
 * (UUID is known), but has not yet been added to the world or fully initialized.
 *
 * <p>This event is cancellable. If cancelled, the player will be denied access to the server.
 *
 * <p>Listeners may:
 *
 * <ul>
 *   <li>Cancel the join attempt
 *   <li>Provide a custom cancel reason (kick message)
 * </ul>
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>Whitelist checks
 *   <li>Ban systems
 *   <li>Access control (maintenance mode, permissions)
 * </ul>
 *
 * <p>Note: No player entity or world state exists yet. Only the player identifier is available.
 */
public class PlayerPreJoinEvent extends CancellableEvent {

  /** The unique identifier of the player attempting to join. */
  private final UUID playerId;

  /**
   * The reason displayed to the player if the join is cancelled.
   *
   * <p>May be empty or {@code null} if no specific message is provided.
   */
  private String cancelReason;

  /**
   * Creates a new {@code PlayerPreJoinEvent} with no cancel reason.
   *
   * @param playerId The unique identifier of the player attempting to join.
   */
  public PlayerPreJoinEvent(UUID playerId) {
    this(playerId, null);
  }

  /**
   * Creates a new {@code PlayerPreJoinEvent}.
   *
   * @param playerId The unique identifier of the player attempting to join.
   * @param cancelReason The initial cancel reason (kick message).
   */
  public PlayerPreJoinEvent(UUID playerId, String cancelReason) {
    this.playerId = playerId;
    this.cancelReason = cancelReason;
  }

  /**
   * Returns the unique identifier of the player attempting to join.
   *
   * @return The player UUID.
   */
  public UUID getPlayerId() {
    return playerId;
  }

  /**
   * Returns the reason displayed to the player if the join is cancelled.
   *
   * @return The cancel reason, or empty/null if none is set.
   */
  public String getCancelReason() {
    return cancelReason;
  }

  /**
   * Sets the reason displayed to the player if the join is cancelled.
   *
   * <p>This message will be shown as the kick message if the event is cancelled.
   *
   * @param cancelReason The new cancel reason.
   */
  public void setCancelReason(String cancelReason) {
    this.cancelReason = cancelReason;
  }
}
