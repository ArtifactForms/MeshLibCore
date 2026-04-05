package server.events.events;

import java.util.UUID;

import server.events.GameEvent;

/**
 * Event fired after a player's inventory has been cleared.
 *
 * <p>This event is dispatched once the inventory has already been modified. It is intended for
 * post-processing logic such as syncing client state, updating UI, triggering effects, or notifying
 * other systems.
 *
 * <p>This event is not cancellable.
 */
public class PostPlayerInventoryClearEvent extends GameEvent {

  /** The unique identifier of the player whose inventory was cleared. */
  private final UUID playerId;

  /**
   * Creates a new {@code PlayerInventoryClearEvent}.
   *
   * @param playerId the unique identifier of the affected player
   */
  public PostPlayerInventoryClearEvent(UUID playerId) {
    this.playerId = playerId;
  }

  /**
   * Returns the unique identifier of the player whose inventory was cleared.
   *
   * @return the player ID
   */
  public UUID getPlayerId() {
    return playerId;
  }
}
