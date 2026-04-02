package server.events.events;

import java.util.UUID;

import server.events.GameEvent;

/**
 * Event fired after a player has fully joined the server.
 *
 * <p>This event is triggered once the player has been completely initialized and spawned into the
 * world. At this stage, the player is fully part of the game and visible to other systems.
 *
 * <p>Listeners may:
 *
 * <ul>
 *   <li>Send welcome messages
 *   <li>Trigger UI updates
 *   <li>Start gameplay-related systems (scoreboards, effects, etc.)
 * </ul>
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>User interface initialization
 *   <li>Analytics or logging
 *   <li>Visual or audio feedback
 * </ul>
 *
 * <p>Note: Unlike {@link PlayerJoinEvent}, all core initialization is already complete. This event
 * should not be used to modify spawn position or join behavior.
 */
public class PlayerPostJoinEvent extends GameEvent {

  /** The unique identifier of the player who has joined. */
  private final UUID playerId;

  /**
   * Creates a new {@code PlayerPostJoinEvent}.
   *
   * @param playerId The unique identifier of the player who has fully joined the server.
   */
  public PlayerPostJoinEvent(UUID playerId) {
    this.playerId = playerId;
  }

  /**
   * Returns the unique identifier of the player who has joined.
   *
   * @return The player UUID.
   */
  public UUID getPlayerId() {
    return playerId;
  }
}
