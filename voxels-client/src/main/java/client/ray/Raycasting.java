package client.ray;

import client.world.Chunk;
import common.game.block.BlockType;
import common.interaction.BlockTarget;
import common.world.BlockFace;
import common.world.World;
import engine.physics.ray.Raycaster;
import engine.runtime.input.Input;
import engine.scene.camera.Camera;
import math.Ray3f;
import math.Vector3f;

/**
 * Utility class providing voxel-based raycasting for block interaction.
 *
 * <p>This class implements a grid traversal using the <b>Amanatides & Woo Voxel DDA algorithm</b>
 * to efficiently step through the voxel grid along a ray.
 *
 * <p>The algorithm visits each voxel intersected by the ray exactly once until one of the following
 * conditions is met:
 *
 * <ul>
 *   <li>a solid block is encountered
 *   <li>the maximum ray distance is exceeded
 *   <li>the vertical world bounds are left
 * </ul>
 *
 * <p>If a block is hit, the returned {@link RaycastResult} contains a {@link BlockTarget}
 * describing:
 *
 * <ul>
 *   <li>the coordinates of the hit block
 *   <li>the face that was intersected
 *   <li>the adjacent block position where a new block could be placed
 * </ul>
 *
 * <p>This raycasting system is primarily used for:
 *
 * <ul>
 *   <li>block selection
 *   <li>block breaking
 *   <li>block placement
 * </ul>
 *
 * <p><b>Coordinate note:</b><br>
 * The Y axis is inverted to convert from render space to world voxel space.
 */
public final class Raycasting {

  private Raycasting() {}

  /**
   * Casts a ray from the camera crosshair into the world.
   *
   * @param camera the active camera
   * @param maxDistance maximum ray distance in blocks
   * @return the raycast result
   */
  public static RaycastResult raycastCrossHair(World world, Camera camera, int maxDistance) {
    Ray3f ray = Raycaster.crossHairRay(camera);
    return raycast(ray, world, maxDistance);
  }

  /**
   * Casts a ray from the current mouse cursor position.
   *
   * @param camera the active camera
   * @param input the input system
   * @param maxDistance maximum ray distance in blocks
   * @return the raycast result
   */
  public static RaycastResult raycastScreenPoint(
      World world, Camera camera, Input input, int maxDistance) {
    Ray3f ray = Raycaster.screenPointToRay(camera, input);
    return raycast(ray, world, maxDistance);
  }

  /**
   * Performs voxel traversal along a ray using the Amanatides & Woo algorithm.
   *
   * <p>The ray is stepped through the voxel grid until a solid block is hit or the maximum distance
   * is reached.
   *
   * @param ray the ray in world space
   * @param world the voxel world
   * @param maxDistance maximum traversal distance
   * @return a {@link RaycastResult} containing a {@link BlockTarget}, or {@link
   *     RaycastResult#miss()}
   */
  public static RaycastResult raycast(Ray3f ray, World world, int maxDistance) {

    Vector3f origin = ray.getOrigin();
    Vector3f dir = ray.getDirection().normalize();

    // Convert render space → voxel world space
    float startX = origin.x;
    float startY = -origin.y;
    float startZ = origin.z;

    float dirX = dir.x;
    float dirY = -dir.y;
    float dirZ = dir.z;

    // Starting voxel
    int x = (int) Math.floor(startX + 0.5f);
    int y = (int) Math.floor(startY + 0.5f);
    int z = (int) Math.floor(startZ + 0.5f);

    // Step direction
    int stepX = dirX >= 0 ? 1 : -1;
    int stepY = dirY >= 0 ? 1 : -1;
    int stepZ = dirZ >= 0 ? 1 : -1;

    // Distance required to cross a voxel on each axis
    float tDeltaX = dirX != 0 ? Math.abs(1f / dirX) : Float.MAX_VALUE;
    float tDeltaY = dirY != 0 ? Math.abs(1f / dirY) : Float.MAX_VALUE;
    float tDeltaZ = dirZ != 0 ? Math.abs(1f / dirZ) : Float.MAX_VALUE;

    float tMaxX;
    float tMaxY;
    float tMaxZ;

    // Initial boundary distances
    if (dirX >= 0) tMaxX = (float) ((Math.floor(startX + 0.5f) + 0.5 - startX) * tDeltaX);
    else tMaxX = (float) ((startX - (Math.floor(startX + 0.5f) - 0.5)) * tDeltaX);

    if (dirY >= 0) tMaxY = (float) ((Math.floor(startY + 0.5f) + 0.5 - startY) * tDeltaY);
    else tMaxY = (float) ((startY - (Math.floor(startY + 0.5f) - 0.5)) * tDeltaY);

    if (dirZ >= 0) tMaxZ = (float) ((Math.floor(startZ + 0.5f) + 0.5 - startZ) * tDeltaZ);
    else tMaxZ = (float) ((startZ - (Math.floor(startZ + 0.5f) - 0.5)) * tDeltaZ);

    BlockFace hitFace = BlockFace.NONE;
    float t = 0;

    while (t <= maxDistance) {

      BlockType type = world.getBlock(x, y, z);
      // Check if current voxel contains a solid block
      if (world.getBlock(x, y, z).isSelectable()) {

        int placeX = x + hitFace.x;
        int placeY = y + hitFace.y;
        int placeZ = z + hitFace.z;

        BlockTarget target = new BlockTarget(x, y, z, placeX, placeY, placeZ, hitFace, type);

        return new RaycastResult(true, target);
      }

      // Advance to next voxel boundary
      if (tMaxX < tMaxY) {

        if (tMaxX < tMaxZ) {

          t = tMaxX;
          tMaxX += tDeltaX;
          x += stepX;

          hitFace = (stepX > 0) ? BlockFace.WEST : BlockFace.EAST;

        } else {

          t = tMaxZ;
          tMaxZ += tDeltaZ;
          z += stepZ;

          hitFace = (stepZ > 0) ? BlockFace.NORTH : BlockFace.SOUTH;
        }

      } else {

        if (tMaxY < tMaxZ) {

          t = tMaxY;
          tMaxY += tDeltaY;
          y += stepY;

          hitFace = (stepY > 0) ? BlockFace.DOWN : BlockFace.UP;

        } else {

          t = tMaxZ;
          tMaxZ += tDeltaZ;
          z += stepZ;

          hitFace = (stepZ > 0) ? BlockFace.NORTH : BlockFace.SOUTH;
        }
      }

      // Stop if leaving vertical world bounds
      if (y < 0 || y >= Chunk.HEIGHT) {
        break;
      }
    }

    return RaycastResult.miss();
  }
}
