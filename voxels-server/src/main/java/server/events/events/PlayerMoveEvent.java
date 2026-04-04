package server.events.events;

import java.util.UUID;

import server.events.CancellableEvent;

/**
 * Event fired when a player attempts to move from one position to another.
 *
 * <p>This event is triggered before the movement is applied to the player.
 *
 * <p>The event provides both the previous position ({@code from}) and the target position ({@code
 * to}). Listeners may modify the target position or cancel the movement entirely.
 *
 * <p>If cancelled, the player will remain at the {@code from} position.
 *
 * <p>Listeners may:
 *
 * <ul>
 *   <li>Cancel movement (e.g. freeze player, region protection)
 *   <li>Modify the destination (e.g. teleport correction)
 *   <li>Apply custom movement rules or physics
 * </ul>
 *
 * <p>Note: This event may fire very frequently (multiple times per tick per player). Keep listeners
 * lightweight to avoid performance issues.
 */
public class PlayerMoveEvent extends CancellableEvent {

  private final UUID playerId;

  // Immutable "from"
  private final float fromX;

  private final float fromY;

  private final float fromZ;

  private final float fromYaw;

  private final float fromPitch;

  // Mutable "to"
  private float toX;

  private float toY;

  private float toZ;

  private float toYaw;

  private float toPitch;

  public PlayerMoveEvent(
      UUID playerId,
      float fromX,
      float fromY,
      float fromZ,
      float fromYaw,
      float fromPitch,
      float toX,
      float toY,
      float toZ,
      float toYaw,
      float toPitch) {
    this.playerId = playerId;

    this.fromX = fromX;
    this.fromY = fromY;
    this.fromZ = fromZ;
    this.fromYaw = fromYaw;
    this.fromPitch = fromPitch;

    this.toX = toX;
    this.toY = toY;
    this.toZ = toZ;
    this.toYaw = toYaw;
    this.toPitch = toPitch;
  }

  // -------------------------
  // Player
  // -------------------------

  public UUID getPlayerId() {
    return playerId;
  }

  // -------------------------
  // FROM (immutable)
  // -------------------------

  public float getFromX() {
    return fromX;
  }

  public float getFromY() {
    return fromY;
  }

  public float getFromZ() {
    return fromZ;
  }

  public float getFromYaw() {
    return fromYaw;
  }

  public float getFromPitch() {
    return fromPitch;
  }

  // -------------------------
  // TO (mutable)
  // -------------------------

  public float getToX() {
    return toX;
  }

  public float getToY() {
    return toY;
  }

  public float getToZ() {
    return toZ;
  }

  public void setToX(float toX) {
    this.toX = toX;
  }

  public void setToY(float toY) {
    this.toY = toY;
  }

  public void setToZ(float toZ) {
    this.toZ = toZ;
  }

  public float getToYaw() {
    return toYaw;
  }

  public float getToPitch() {
    return toPitch;
  }

  public void setToYaw(float toYaw) {
    this.toYaw = toYaw;
  }

  public void setToPitch(float toPitch) {
    this.toPitch = toPitch;
  }

  /** Sets the full target position. */
  public void setToPosition(float x, float y, float z) {
    this.toX = x;
    this.toY = y;
    this.toZ = z;
  }
}
