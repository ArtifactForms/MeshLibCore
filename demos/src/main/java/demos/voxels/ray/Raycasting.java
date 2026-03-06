package demos.voxels.ray;

import demos.voxels.chunk.Chunk;
import demos.voxels.world.World;
import engine.physics.ray.Raycaster;
import engine.scene.camera.Camera;
import math.Ray3f;
import math.Vector3f;

public class Raycasting {

  private static final int MAX_DISTANCE = 500;

  private Raycasting() {}

  public static RaycastResult raycast(Camera camera, World world) {
    Ray3f ray = Raycaster.crossHairRay(camera);
    Vector3f origin = ray.getOrigin();
    Vector3f dir = ray.getDirection().normalize();

    // Welt-Raum Transformation (Y-Spiegelung beibehalten)
    float startX = origin.x;
    float startY = -origin.y;
    float startZ = origin.z;

    float dirX = dir.x;
    float dirY = -dir.y;
    float dirZ = dir.z;

    // Start-Voxel (Synchron mit World-Logik)
    int x = (int) Math.floor(startX + 0.5f);
    int y = (int) Math.floor(startY + 0.5f);
    int z = (int) Math.floor(startZ + 0.5f);

    int stepX = dirX >= 0 ? 1 : -1;
    int stepY = dirY >= 0 ? 1 : -1;
    int stepZ = dirZ >= 0 ? 1 : -1;

    float tDeltaX = Math.abs(1.0f / dirX);
    float tDeltaY = Math.abs(1.0f / dirY);
    float tDeltaZ = Math.abs(1.0f / dirZ);

    float tMaxX, tMaxY, tMaxZ;

    // DDA Startdistanzen
    if (dirX >= 0) tMaxX = (float) ((Math.floor(startX + 0.5f) + 0.5 - startX) * tDeltaX);
    else tMaxX = (float) ((startX - (Math.floor(startX + 0.5f) - 0.5)) * tDeltaX);

    if (dirY >= 0) tMaxY = (float) ((Math.floor(startY + 0.5f) + 0.5 - startY) * tDeltaY);
    else tMaxY = (float) ((startY - (Math.floor(startY + 0.5f) - 0.5)) * tDeltaY);

    if (dirZ >= 0) tMaxZ = (float) ((Math.floor(startZ + 0.5f) + 0.5 - startZ) * tDeltaZ);
    else tMaxZ = (float) ((startZ - (Math.floor(startZ + 0.5f) - 0.5)) * tDeltaZ);

    // Wir tracken die Normale des getroffenen Gesichts
    int hitNormalX = 0, hitNormalY = 0, hitNormalZ = 0;
    float t = 0;

    while (t <= MAX_DISTANCE) {
      if (world.isSolid(x, y, z)) {
        // Platzierungs-Position ist der getroffene Block + die Normale der Fläche
        int placeX = x + hitNormalX;
        int placeY = y + hitNormalY;
        int placeZ = z + hitNormalZ;
        return new RaycastResult(true, x, y, z, placeX, placeY, placeZ);
      }

      // DDA Step
      if (tMaxX < tMaxY) {
        if (tMaxX < tMaxZ) {
          t = tMaxX;
          tMaxX += tDeltaX;
          x += stepX;
          // Wir sind in X-Richtung gesprungen, also kam der Strahl von der Seite
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
