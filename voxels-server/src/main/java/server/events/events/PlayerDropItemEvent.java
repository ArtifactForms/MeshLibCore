package server.events.events;

import java.util.UUID;
import server.events.CancellableEvent;

/**
 * Event fired when a player attempts to drop an item from their inventory.
 *
 * <p>This event is triggered on the server after all basic validations have passed, such as
 * ensuring the player exists, has the item in their inventory, and the amount is valid.
 *
 * <p>Listeners may cancel this event to prevent the item from being dropped. If cancelled, the
 * server will not remove the item from the player's inventory and may resynchronize the client
 * state.
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>Custom permission or gameplay restriction systems (e.g., preventing drops in protected
 *       areas)
 *   <li>Logging or analytics
 *   <li>Modifying or adjusting drop behavior (e.g., limiting quantity, changing drop location)
 * </ul>
 *
 * <p>Note: This event does not perform any validation itself and assumes all input data is already
 * sanitized by the calling use case.
 */
public class PlayerDropItemEvent extends CancellableEvent {

  /** The unique identifier of the player attempting to drop the item. */
  private final UUID player;

  /** The ID of the item being dropped. */
  private final short itemId;

  /** The quantity of the item being dropped. */
  private final int amount;

  /**
   * Creates a new {@code PlayerDropItemEvent}.
   *
   * @param player The unique identifier of the player performing the drop.
   * @param itemId The ID of the item being dropped.
   * @param amount The quantity of the item to drop.
   */
  public PlayerDropItemEvent(UUID player, short itemId, int amount) {
    this.player = player;
    this.itemId = itemId;
    this.amount = amount;
  }

  /** @return The unique identifier of the player attempting to drop the item. */
  public UUID getPlayerId() {
    return player;
  }

  /** @return The ID of the item being dropped. */
  public short getItemId() {
    return itemId;
  }

  /** @return The quantity of the item being dropped. */
  public int getAmount() {
    return amount;
  }
}
