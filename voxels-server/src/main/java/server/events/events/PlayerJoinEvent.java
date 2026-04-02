package server.events.events;

import java.util.UUID;

import server.events.GameEvent;

/**
 * Event fired when a player is joining the server and is about to be initialized.
 *
 * <p>This event is triggered after the player has passed all pre-join checks (e.g. whitelist, bans)
 * but before they are fully spawned into the world.
 *
 * <p>At this stage:
 *
 * <ul>
 *   <li>The player is accepted and will join the server
 *   <li>The spawn position can still be modified
 *   <li>The join message can still be changed or suppressed
 * </ul>
 *
 * <p>Listeners may:
 *
 * <ul>
 *   <li>Modify the player's spawn position
 *   <li>Customize or suppress the join message
 *   <li>Initialize player state (kits, stats, etc.)
 * </ul>
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>Spawn management systems
 *   <li>Custom join messages or localization
 *   <li>Player initialization logic
 * </ul>
 *
 * <p>Note: The player is not yet fully present in the world. Systems that depend on a fully
 * initialized player should use {@link PlayerPostJoinEvent}.
 */
public class PlayerJoinEvent extends GameEvent {

  /** The unique identifier of the joining player. */
  private final UUID playerId;

  /** The x-coordinate of the spawn position. */
  private int spawnX;

  /** The y-coordinate of the spawn position. */
  private int spawnY;

  /** The z-coordinate of the spawn position. */
  private int spawnZ;

  /** The message broadcast when the player joins. */
  private String joinMessage;

  /**
   * Creates a new {@code PlayerJoinEvent}.
   *
   * @param playerId The unique identifier of the joining player.
   * @param joinMessage The initial join message to be broadcast.
   * @param spawnX The initial spawn x-coordinate.
   * @param spawnY The initial spawn y-coordinate.
   * @param spawnZ The initial spawn z-coordinate.
   */
  public PlayerJoinEvent(UUID playerId, String joinMessage, int spawnX, int spawnY, int spawnZ) {
    this.playerId = playerId;
    this.joinMessage = joinMessage;
    this.spawnX = spawnX;
    this.spawnY = spawnY;
    this.spawnZ = spawnZ;
  }

  /** @return The unique identifier of the joining player. */
  public UUID getPlayerId() {
    return playerId;
  }

  /** @return The spawn x-coordinate. */
  public int getSpawnX() {
    return spawnX;
  }

  /** @return The spawn y-coordinate. */
  public int getSpawnY() {
    return spawnY;
  }

  /** @return The spawn z-coordinate. */
  public int getSpawnZ() {
    return spawnZ;
  }

  /**
   * Sets the spawn position of the player.
   *
   * @param x The new spawn x-coordinate.
   * @param y The new spawn y-coordinate.
   * @param z The new spawn z-coordinate.
   */
  public void setSpawn(int x, int y, int z) {
    this.spawnX = x;
    this.spawnY = y;
    this.spawnZ = z;
  }

  /** @return The join message to be broadcast. */
  public String getJoinMessage() {
    return joinMessage;
  }

  /**
   * Sets the join message.
   *
   * <p>Can be set to {@code null} or empty to suppress the message entirely.
   *
   * @param joinMessage The new join message.
   */
  public void setJoinMessage(String joinMessage) {
    this.joinMessage = joinMessage;
  }
}
