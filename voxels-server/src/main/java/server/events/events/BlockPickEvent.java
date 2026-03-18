package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

/**
 * Event fired when a player attempts to pick a block into their hotbar.
 *
 * <p>This event is triggered on the server after all basic validations have passed, including slot
 * validation, ability checks, permission checks, block existence, and reach distance verification.
 *
 * <p>Listeners may cancel this event to prevent the block from being picked. If cancelled, the
 * server will not modify the player's inventory and will resynchronize the client state if
 * necessary.
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>Custom permission systems
 *   <li>Gameplay restrictions (e.g. protected regions)
 *   <li>Logging or analytics
 *   <li>Modifying or denying block pick behavior
 * </ul>
 *
 * <p>Note: This event does not perform any validation itself and assumes that all input data is
 * already sanitized by the calling use case.
 */
public class BlockPickEvent extends CancellableEvent {

  /** The unique identifier of the player performing the block pick. */
  private final UUID playerId;

  /** The x-coordinate of the targeted block. */
  private final int x;

  /** The y-coordinate of the targeted block. */
  private final int y;

  /** The z-coordinate of the targeted block. */
  private final int z;

  /** The ID of the block being picked. */
  private final short blockId;

  /** The hotbar slot index that will receive the block. */
  private final int selectedSlot;

  /**
   * Creates a new {@code BlockPickEvent}.
   *
   * @param player The unique identifier of the player performing the action.
   * @param x The x-coordinate of the targeted block.
   * @param y The y-coordinate of the targeted block.
   * @param z The z-coordinate of the targeted block.
   * @param blockId The ID of the block being picked.
   * @param selectedSlot The hotbar slot index that will receive the block.
   */
  public BlockPickEvent(UUID playerId, int x, int y, int z, short blockId, int selectedSlot) {
    this.playerId = playerId;
    this.x = x;
    this.y = y;
    this.z = z;
    this.blockId = blockId;
    this.selectedSlot = selectedSlot;
  }

  /** @return The unique identifier of the player performing the block pick. */
  public UUID getPlayerId() {
    return playerId;
  }

  /** @return The x-coordinate of the targeted block. */
  public int getX() {
    return x;
  }

  /** @return The y-coordinate of the targeted block. */
  public int getY() {
    return y;
  }

  /** @return The z-coordinate of the targeted block. */
  public int getZ() {
    return z;
  }

  /** @return The ID of the block being picked. */
  public short getBlockId() {
    return blockId;
  }

  /** @return The hotbar slot index that will receive the block. */
  public int getSelectedSlot() {
    return selectedSlot;
  }
}
