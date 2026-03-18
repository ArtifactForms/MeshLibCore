package server.events.events;

import java.util.UUID;

import server.events.GameEvent;

/**
 * Event fired when a player joins the server.
 *
 * <p>This event is triggered after the player has been authenticated and is about to be fully
 * initialized in the game world. At this stage, the server has already validated the connection and
 * created the player context, but has not yet finalized spawn position or broadcasted the join
 * message.
 *
 * <p>Listeners may:
 *
 * <ul>
 *   <li>Modify the player's spawn position
 *   <li>Customize or suppress the join message
 * </ul>
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>Spawn management systems
 *   <li>Custom join messages or localization
 *   <li>Player initialization logic (kits, stats, etc.)
 * </ul>
 *
 * <p>Note: This event assumes that all player identification and connection validation has already
 * been performed by the calling system.
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
