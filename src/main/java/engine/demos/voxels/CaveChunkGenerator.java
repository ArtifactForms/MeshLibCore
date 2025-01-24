package engine.demos.voxels;

import math.Mathf;
import math.PerlinNoise;
import math.PerlinNoise3;
import math.Vector3f;

public class CaveChunkGenerator implements ChunkGenerator {

  @Override
  public void generate(Chunk chunk) {
    Vector3f position = chunk.getPosition();
    int baseHeight = 0;
    float terrainScale = 0.01f;
    float caveScale = 0.05f; // Scale for cave noise
    float caveThreshold = 0.4f; // Adjust for cave density
    PerlinNoise terrainNoise = new PerlinNoise(0); // Terrain noise
    PerlinNoise3 caveNoise = new PerlinNoise3(1); // Cave noise

    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int z = 0; z < Chunk.DEPTH; z++) {
        float wx = position.x + x;
        float wz = position.z + z;

        // Generate terrain height
        float terrainValue = (float) terrainNoise.noise(wx * terrainScale, wz * terrainScale);
        int heightValue = (int) Mathf.map(terrainValue, 0, 1, 0, 60) + baseHeight;
        chunk.getHeightMap()[x + z * Chunk.WIDTH] = heightValue;

        for (int y = 0; y < heightValue; y++) {
          float wy = position.y + y;

          // Generate cave noise only below baseHeight
//          if (y < baseHeight) {
            float caveValue =
                (float) caveNoise.noise(wx * caveScale, wy * caveScale, wz * caveScale);

            // Check if the block is part of a cave
            if (caveValue > caveThreshold) {
              chunk.setBlockAt(BlockType.AIR, x, y, z);
//              continue;
            } else {
        	chunk.setBlockAt(BlockType.STONE, x, y, z);
            }
//          }

          // Fill terrain blocks
//          if (y < 40 && y > 20) {
//            chunk.setBlockAt(BlockType.GRASS, x, y, z);
//          } else {
//            chunk.setBlockAt(BlockType.STONE, x, y, z);
//          }
        }
      }
    }
  }
}
