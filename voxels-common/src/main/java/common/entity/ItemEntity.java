package common.entity;

import common.game.block.BlockType;
import math.Vector3f;

/**
 * Represents a physical item dropped in the world. It handles its own simple physics like gravity
 * and velocity.
 */
public class ItemEntity {

  private final long entityId;

  private final BlockType blockType;

  private Vector3f position;

  private Vector3f velocity;

  // Constants for simple physics
  private static final float GRAVITY = -0.02f;

  private static final float DRAG = 0.98f;

  public ItemEntity(long id, BlockType type, Vector3f pos) {
    this.entityId = id;
    this.blockType = type;
    this.position = new Vector3f(pos.x, pos.y, pos.z);
    this.velocity = new Vector3f(0, 0, 0);
  }

  /**
   * Updates the entity's position based on velocity and applies gravity. This should be called
   * every tick on the server.
   */
  public void update() {
    // Apply gravity to vertical velocity
    velocity.y += GRAVITY;

    // Apply drag (air resistance)
    velocity.y += GRAVITY;

    velocity.x *= DRAG;
    velocity.z *= DRAG;

    // Move the entity
    position.x += velocity.x;
    position.y += velocity.y;
    position.z += velocity.z;

    // Simple ground collision: stop at y=0 if no world collision is implemented yet
    if (position.y < 0) {
      position.y = 0;
      velocity.y = 0;
    }
  }

  // Getters and Setters
  public long getEntityId() {
    return entityId;
  }

  public BlockType getBlockType() {
    return blockType;
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f position) {
    this.position.set(position);
  }

  public Vector3f getVelocity() {
    return velocity;
  }

  public void setVelocity(Vector3f velocity) {
    this.velocity = velocity;
  }

  /**
   * Checks if this item is within pickup range of a player.
   *
   * @param playerPos The current position of the player.
   * @return true if close enough to be picked up.
   */
  public boolean isNear(Vector3f playerPos) {
    float dx = position.x - playerPos.x;
    float dy = position.y - playerPos.y;
    float dz = position.z - playerPos.z;
    // Using squared distance for better performance (no square root needed)
    float distSq = dx * dx + dy * dy + dz * dz;
    return distSq < 2.25f; // 1.5 meters range (1.5 * 1.5 = 2.25)
  }

  public void applyDropImpulse(Vector3f direction) {

    velocity.x = direction.x * 0.25f;
    velocity.y = 0.25f;
    velocity.z = direction.z * 0.25f;
  }
}
