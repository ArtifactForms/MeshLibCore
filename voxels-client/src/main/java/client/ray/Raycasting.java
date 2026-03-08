package client.ray;

import client.world.Chunk;
import common.game.ReachDistance;
import common.world.BlockFace;
import common.world.World;
import engine.physics.ray.Raycaster;
import engine.scene.camera.Camera;
import math.Ray3f;
import math.Vector3f;

/**
 * Utility class providing voxel-based raycasting for block interaction.
 *
 * <p>This implementation performs a grid traversal using the <b>Amanatides & Woo (Voxel DDA)</b>
 * algorithm to efficiently step through blocks along a ray until a solid block is hit.
 *
 * <p>The ray originates from the camera's crosshair direction and traverses the voxel grid until
 * either:
 *
 * <ul>
 *   <li>a solid block is encountered
 *   <li>the maximum ray distance is exceeded
 *   <li>the vertical world bounds are left
 * </ul>
 *
 * <p>If a block is hit, the result also contains the adjacent position where a new block could be
 * placed, determined by the face normal of the hit surface.
 *
 * <p><b>Coordinate note:</b><br>
 * The Y-axis is inverted to match the world's coordinate system (render space → world space
 * conversion).
 *
 * <p>This method is typically used for:
 *
 * <ul>
 *   <li>block selection
 *   <li>block breaking
 *   <li>block placement
 * </ul>
 *
 * @author
 */
public final class Raycasting {

  /** Maximum distance the ray is allowed to travel. */
  private static final float MAX_DISTANCE = ReachDistance.VALUE - 1;

  private Raycasting() {}

  /**
   * Casts a ray from the camera's crosshair into the world and determines the first solid block
   * intersected by the ray.
   *
   * <p>The algorithm traverses the voxel grid step-by-step using a Digital Differential Analyzer
   * (DDA), which guarantees that each voxel along the ray path is visited exactly once.
   *
   * <p>If a block is hit, the returned {@link RaycastResult} contains:
   *
   * <ul>
   *   <li>the coordinates of the hit block
   *   <li>the face of the block that was intersected
   *   <li>the adjacent position where a new block could be placed
   * </ul>
   *
   * @param camera the active camera used to generate the crosshair ray
   * @param world the voxel world queried for solid blocks
   * @return a {@link RaycastResult} describing the intersection, or {@link RaycastResult#miss()} if
   *     no block was hit
   */
  public static RaycastResult raycast(Camera camera, World world) {

    Ray3f ray = Raycaster.crossHairRay(camera);
    Vector3f origin = ray.getOrigin();
    Vector3f dir = ray.getDirection().normalize();

    // Convert camera space to world voxel space
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

    // Step direction per axis
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

    while (t <= MAX_DISTANCE) {

      // Check if current voxel is solid
      if (world.isSolid(x, y, z)) {

        // Determine placement position using face normal
        int placeX = x + hitFace.x;
        int placeY = y + hitFace.y;
        int placeZ = z + hitFace.z;

        return new RaycastResult(true, x, y, z, placeX, placeY, placeZ, hitFace);
      }

      // Advance to the next voxel boundary
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
