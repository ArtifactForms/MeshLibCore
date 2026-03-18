package common.player.interaction;

import math.Mathf;
import math.Vector3f;

public class Reach {
	
  private Reach() {
	  // No instances
  }	

  public static boolean isWithinReach(
      Vector3f playerPos, float eyeHeight, int x, int y, int z, float maxReach) {

    return distanceToBlock(playerPos, eyeHeight, x, y, z) <= maxReach;
  }

  public static float distanceToBlock(Vector3f playerPos, float eyeHeight, int x, int y, int z) {

    float eyeX = playerPos.x;
    float eyeY = playerPos.y + eyeHeight;
    float eyeZ = playerPos.z;

    // CENTER-BASED AABB
    float minX = x - 0.5f;
    float minY = y - 0.5f;
    float minZ = z - 0.5f;

    float maxX = x + 0.5f;
    float maxY = y + 0.5f;
    float maxZ = z + 0.5f;

    float closestX = Mathf.clamp(eyeX, minX, maxX);
    float closestY = Mathf.clamp(eyeY, minY, maxY);
    float closestZ = Mathf.clamp(eyeZ, minZ, maxZ);

    float dx = eyeX - closestX;
    float dy = eyeY - closestY;
    float dz = eyeZ - closestZ;

    return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
  }
}
