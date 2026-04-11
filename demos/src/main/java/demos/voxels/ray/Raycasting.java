package demos.voxels.ray;

import demos.voxels.chunk.Chunk;
import demos.voxels.world.World;
import engine.physics.ray.Raycaster;
import engine.scene.camera.Camera;
import math.Ray3f;
import math.Vector3f;

public class Raycasting {

  private static final int MAX_DISTANCE = 500;

  public static RaycastResult raycast(Camera camera, World world) {
    Ray3f ray = Raycaster.crossHairRay(camera);
    Vector3f origin = ray.getOrigin();
    Vector3f dir = ray.getDirection().normalize();

    // Amanatides & Woo algorithm
    // World-space transformation (keep Y inversion)
    float startX = origin.x;
    float startY = -origin.y;
    float startZ = origin.z;

    float dirX = dir.x;
    float dirY = -dir.y;
    float dirZ = dir.z;

    // Starting voxel (aligned with world logic)
    int x = (int) Math.floor(startX + 0.5f);
    int y = (int) Math.floor(startY + 0.5f);
    int z = (int) Math.floor(startZ + 0.5f);

    int stepX = dirX >= 0 ? 1 : -1;
    int stepY = dirY >= 0 ? 1 : -1;
    int stepZ = dirZ >= 0 ? 1 : -1;

    float tDeltaX = Math.abs(1.0f / dirX);
    float tDeltaY = Math.abs(1.0f / dirY);
    float tDeltaZ = Math.abs(1.0f / dirZ);

    float tMaxX;
    float tMaxY;
    float tMaxZ;

    // Initial DDA distances
    if (dirX >= 0) tMaxX = (float) ((Math.floor(startX + 0.5f) + 0.5 - startX) * tDeltaX);
    else tMaxX = (float) ((startX - (Math.floor(startX + 0.5f) - 0.5)) * tDeltaX);

    if (dirY >= 0) tMaxY = (float) ((Math.floor(startY + 0.5f) + 0.5 - startY) * tDeltaY);
    else tMaxY = (float) ((startY - (Math.floor(startY + 0.5f) - 0.5)) * tDeltaY);

    if (dirZ >= 0) tMaxZ = (float) ((Math.floor(startZ + 0.5f) + 0.5 - startZ) * tDeltaZ);
    else tMaxZ = (float) ((startZ - (Math.floor(startZ + 0.5f) - 0.5)) * tDeltaZ);

    // We track the normal of the hit face
    int hitNormalX = 0;
    int hitNormalY = 0;
    int hitNormalZ = 0;
    float t = 0;

    while (t <= MAX_DISTANCE) {
      if (world.isSolid(x, y, z)) {
        // Placement position is the hit block + the face normal
        int placeX = x + hitNormalX;
        int placeY = y + hitNormalY;
        int placeZ = z + hitNormalZ;
        return new RaycastResult(true, x, y, z, placeX, placeY, placeZ);
      }

      // DDA step
      if (tMaxX < tMaxY) {
        if (tMaxX < tMaxZ) {
          t = tMaxX;
          tMaxX += tDeltaX;
          x += stepX;
          // We stepped in X direction, so the ray came from the side
          hitNormalX = -stepX;
          hitNormalY = 0;
          hitNormalZ = 0;
        } else {
          t = tMaxZ;
          tMaxZ += tDeltaZ;
          z += stepZ;
          hitNormalX = 0;
          hitNormalY = 0;
          hitNormalZ = -stepZ;
        }
      } else {
        if (tMaxY < tMaxZ) {
          t = tMaxY;
          tMaxY += tDeltaY;
          y += stepY;
          hitNormalX = 0;
          hitNormalY = -stepY;
          hitNormalZ = 0;
        } else {
          t = tMaxZ;
          tMaxZ += tDeltaZ;
          z += stepZ;
          hitNormalX = 0;
          hitNormalY = 0;
          hitNormalZ = -stepZ;
        }
      }

      if (y < 0 || y >= Chunk.HEIGHT) break;
    }
    return RaycastResult.miss();
  }
}