package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

/**
 * Event fired when a player attempts to place a block in the world.
 *
 * <p>This event is triggered on the server after all basic validations have passed, including
 * permission checks, placement rules, block existence, and reach distance verification.
 *
 * <p>Listeners may cancel this event to prevent the block from being placed. If cancelled, the
 * server will not modify the world and may resynchronize the client state if necessary.
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>Custom permission systems
 *   <li>Gameplay restrictions (e.g. protected regions)
 *   <li>Logging or analytics
 *   <li>Preventing or modifying block placement behavior
 * </ul>
 *
 * <p>Note: This event does not perform any validation itself and assumes that all input data is
 * already sanitized by the calling use case.
 */
public class BlockPlaceEvent extends CancellableEvent {

  /** The unique identifier of the player performing the block placement. */
  private final UUID player;

  /** The x-coordinate of the target position. */
  private final int x;

  /** The y-coordinate of the target position. */
  private final int y;

  /** The z-coordinate of the target position. */
  private final int z;

  /** The ID of the block being placed. */
  private short blockId;

  /**
   * Creates a new {@code BlockPlaceEvent}.
   *
   * @param player The unique identifier of the player performing the action.
   * @param x The x-coordinate of the target position.
   * @param y The y-coordinate of the target position.
   * @param z The z-coordinate of the target position.
   * @param blockId The ID of the block being placed.
   */
  public BlockPlaceEvent(UUID player, int x, int y, int z, short blockId) {
    this.player = player;
    this.x = x;
    this.y = y;
    this.z = z;
    this.blockId = blockId;
  }

  /** @return The unique identifier of the player performing the block placement. */
  public UUID getPlayer() {
    return player;
  }

  /** @return The x-coordinate of the target position. */
  public int getX() {
    return x;
  }

  /** @return The y-coordinate of the target position. */
  public int getY() {
    return y;
  }

  /** @return The z-coordinate of the target position. */
  public int getZ() {
    return z;
  }

  /** @return The ID of the block being placed. */
  public short getBlockId() {
    return blockId;
  }
}