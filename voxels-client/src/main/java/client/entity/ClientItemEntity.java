package client.entity;

import client.app.ApplicationContext;
import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.game.block.Blocks;
import engine.components.StaticGeometry;
import engine.rendering.Graphics;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;

/**
 * Represents a dropped item in the client-side world. Handles visual interpolation, simple physics
 * (gravity/bouncing), and rendering.
 */
public class ClientItemEntity {

  private final long entityId;
  private final BlockType blockType;
  private Vector3f position;
  private Vector3f velocity;
  private float rotation = 0;
  private static StaticGeometry geometry;

  // Physics Constants
  private static final float GRAVITY = -0.015f;
  private static final float AIR_DRAG = 0.98f;
  private static final float GROUND_FRICTION = 0.8f;
  private static final float BOUNCE_RESTITUTION = -0.4f; // 40% energy retention on bounce

  static {
    // Shared geometry for all item entities (small cube)
    Mesh3D mesh = new CubeCreator(0.1f).create();
    geometry = new StaticGeometry(mesh);
  }

  public ClientItemEntity(long id, BlockType type, Vector3f pos, Vector3f vel) {
    this.entityId = id;
    this.blockType = type;
    this.position = new Vector3f(pos.x, pos.y, pos.z);
    this.velocity = new Vector3f(vel.x, vel.y, vel.z);
  }

  public ClientItemEntity(long id, BlockType type, Vector3f pos) {
    this(id, type, pos, new Vector3f(0, 0, 0));
  }

  /** * Updates physics, handles terrain collision, and updates visual animations. */
  public void update() {
    // Slow constant rotation for visual appeal
    rotation += 0.05f;

    // 1. Apply Gravity: Only if the item is not already stationary on the ground
    if (Math.abs(velocity.y) > 0.001f || !isAtRest()) {
      velocity.y += GRAVITY;
    }

    // 2. Apply Air Drag
    velocity.x *= AIR_DRAG;
    velocity.y *= AIR_DRAG;
    velocity.z *= AIR_DRAG;

    // 3. Collision Prediction
    float nextX = position.x + velocity.x;
    float nextY = position.y + velocity.y;
    float nextZ = position.z + velocity.z;

    // Convert coordinates to block indices for collision checking
    int bx = Math.round(nextX);
    int by = (int) Math.floor(nextY);
    int bz = Math.round(nextZ);

    short blockId = ApplicationContext.clientWorld.getBlock(bx, by, bz).getId();

    if (BlockRegistry.get(blockId) != Blocks.AIR) {
      // BOUNCE LOGIC: If falling fast enough, reflect velocity
      if (Math.abs(velocity.y) > 0.05f) {
        velocity.y *= BOUNCE_RESTITUTION;
      } else {
        // STOP LOGIC: Snap to the top of the block when movement is negligible
        velocity.y = 0;
        position.y = (float) Math.floor(nextY) + 1.0f;
      }

      // Apply higher friction when touching the ground
      velocity.x *= GROUND_FRICTION;
      velocity.z *= GROUND_FRICTION;
    } else {
      // No obstacle: Apply the predicted movement
      position.x = nextX;
      position.y = nextY;
      position.z = nextZ;
    }

    // 4. Epsilon Check: Stop micro-jittering when velocity is near zero
    if (Math.abs(velocity.x) < 0.001f) velocity.x = 0;
    if (Math.abs(velocity.z) < 0.001f) velocity.z = 0;
    if (Math.abs(velocity.y) < 0.001f && position.y % 1.0f < 0.01f) velocity.y = 0;
  }

  /** * Returns true if the entity has effectively stopped moving vertically. */
  private boolean isAtRest() {
    return Math.abs(velocity.y) < 0.001f;
  }

  /**
   * Renders the item with a bobbing animation. Note: This flips the Y-axis for rendering to match
   * the engine's coordinate system.
   */
  public void render(Graphics g) {
    g.pushMatrix();

    // Visual bobbing effect (Sine wave)
    float bobbing = (float) Math.sin(System.currentTimeMillis() * 0.005) * 0.05f;

    // Coordinate Mapping: Positive logic Y maps to Negative render Y
    float renderY = -(position.y + bobbing);

    g.translate(position.x, renderY, position.z);
    g.rotateY(rotation);

    geometry.render(g);

    g.popMatrix();
  }

  public long getEntityId() {
    return entityId;
  }
}
