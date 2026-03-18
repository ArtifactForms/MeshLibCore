package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

/**
 * Event fired when a player attempts to break a block in the world.
 *
 * <p>This event is triggered on the server after all basic validations have passed, including
 * permission checks, block existence, and reach distance verification.
 *
 * <p>Listeners may cancel this event to prevent the block from being broken. If cancelled, the
 * server will not remove the block and may resynchronize the client state if necessary.
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>Custom permission systems
 *   <li>Gameplay restrictions (e.g. protected regions)
 *   <li>Logging or analytics
 *   <li>Preventing or modifying block breaking behavior
 * </ul>
 *
 * <p>Note: This event does not perform any validation itself and assumes that all input data is
 * already sanitized by the calling use case.
 */
public class BlockBreakEvent extends CancellableEvent {

  /** The unique identifier of the player performing the block break. */
  private final UUID playerId;

  /** The x-coordinate of the targeted block. */
  private final int x;

  /** The y-coordinate of the targeted block. */
  private final int y;

  /** The z-coordinate of the targeted block. */
  private final int z;

  /**
   * Creates a new {@code BlockBreakEvent}.
   *
   * @param playerId The unique identifier of the player performing the action.
   * @param x The x-coordinate of the targeted block.
   * @param y The y-coordinate of the targeted block.
   * @param z The z-coordinate of the targeted block.
   */
  public BlockBreakEvent(UUID playerId, int x, int y, int z) {
    this.playerId = playerId;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /** @return The unique identifier of the player performing the block break. */
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
}
