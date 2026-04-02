package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

/**
 * Event fired when a player sends a chat message.
 *
 * <p>This event is triggered whenever a player attempts to send a message to the chat. It is fired
 * before the message is broadcast to other players.
 *
 * <p>This event is cancellable. If cancelled, the message will not be delivered.
 *
 * <p>Listeners may:
 *
 * <ul>
 *   <li>Cancel the message
 *   <li>Modify the message content
 * </ul>
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>Chat filtering (profanity, spam)
 *   <li>Custom chat formatting
 *   <li>Command parsing via chat (if applicable)
 * </ul>
 *
 * <p>Note: This event only represents the message intent. Actual message delivery happens after
 * this event completes successfully.
 */
public class PlayerChatEvent extends CancellableEvent {

  /** The unique identifier of the player who sent the message. */
  private final UUID playerId;

  /**
   * The chat message content.
   *
   * <p>This value may be modified by listeners to alter the final message that will be broadcast.
   */
  private String message;

  /**
   * Creates a new {@code PlayerChatEvent}.
   *
   * @param playerId The unique identifier of the player sending the message.
   * @param message The original chat message.
   */
  public PlayerChatEvent(UUID playerId, String message) {
    this.playerId = playerId;
    this.message = message;
  }

  /**
   * Returns the unique identifier of the player who sent the message.
   *
   * @return The player UUID.
   */
  public UUID getPlayerId() {
    return playerId;
  }

  /**
   * Returns the current chat message.
   *
   * @return The message content.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the chat message.
   *
   * <p>This can be used to modify or filter the message before it is delivered.
   *
   * @param message The new message content.
   */
  public void setMessage(String message) {
    this.message = message;
  }
}
