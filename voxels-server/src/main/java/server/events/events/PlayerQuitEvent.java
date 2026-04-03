package server.events.events;

import java.util.UUID;

import server.events.GameEvent;

/**
 * Event fired when a player disconnects from the server.
 *
 * <p>This event is triggered after the player has been removed from the active player list, but
 * before the quit message is broadcast to other players.
 *
 * <p>Listeners may:
 *
 * <ul>
 *   <li>Modify the quit message
 *   <li>Suppress the quit message by setting it to {@code null} or empty
 * </ul>
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>Custom join/quit message systems
 *   <li>Silent disconnects (e.g. vanish, moderation tools)
 *   <li>Logging player activity
 * </ul>
 *
 * <p>Note: This event is not cancellable. The player has already disconnected and cannot be kept
 * online at this stage.
 */
public class PlayerQuitEvent extends GameEvent {

  /** The unique identifier of the player who disconnected. */
  private final UUID playerId;

  /**
   * The message broadcast to other players when this player leaves.
   *
   * <p>May be set to {@code null} or empty to suppress the message.
   */
  private String quitMessage;

  /**
   * Creates a new {@code PlayerQuitEvent}.
   *
   * @param playerId The UUID of the player who disconnected.
   * @param quitMessage The default quit message.
   */
  public PlayerQuitEvent(UUID playerId, String quitMessage) {
    this.playerId = playerId;
    this.quitMessage = quitMessage;
  }

  /**
   * Returns the UUID of the player who disconnected.
   *
   * @return The player UUID.
   */
  public UUID getPlayerId() {
    return playerId;
  }

  /**
   * Returns the quit message.
   *
   * @return The quit message, or {@code null}/empty if none is set.
   */
  public String getQuitMessage() {
    return quitMessage;
  }

  /**
   * Sets the quit message.
   *
   * <p>This message will be broadcast to other players. Setting it to {@code null} or empty will
   * suppress the message.
   *
   * @param quitMessage The new quit message.
   */
  public void setQuitMessage(String quitMessage) {
    this.quitMessage = quitMessage;
  }
}
