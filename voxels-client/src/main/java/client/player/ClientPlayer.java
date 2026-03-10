package client.player;

/**
 * Represents the local player on the client side. Holds position, rotation, and movement
 * attributes.
 */
public class ClientPlayer {

  private float x;
  private float y;
  private float z;

  // Rotation in degrees (Yaw = horizontal, Pitch = vertical)
  private float yaw;
  private float pitch;

  private float speed = 5f;

  // --- Position Getters & Setters ---

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getZ() {
    return z;
  }

  public void setZ(float z) {
    this.z = z;
  }

  // --- Rotation Getters & Setters ---

  public float getYaw() {
    return yaw;
  }

  public void setYaw(float yaw) {
    this.yaw = yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  // --- Attributes ---

  public float getSpeed() {
    return speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }

  /** Helper to set position in one call. */
  public void setPosition(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
}
