package engine.demos.voxels;

import math.Mathf;
import math.PerlinNoise;
import math.Vector3f;
import java.util.Random;

public class ChunkGenerator2 implements ChunkGenerator {

  private long seed = 1323; // Global seed
  private int baseHeight = 0;
  private float scale = 0.01f; // Noise scale
  private float dirtScale = 0.1f;
  private int heightMultiplier = 60;

  public void generate(Chunk chunk) {
    Vector3f position = chunk.getPosition();

    // Seeded noise generators
    PerlinNoise noise = new PerlinNoise(seed);
    PerlinNoise dirtNoise = new PerlinNoise(seed + 1);

    // Create a seeded RNG for this chunk based on global seed and chunk position
    long chunkSeed = seed ^ ((long) (position.x * 31 + position.z * 17));
    Random rng = new Random(chunkSeed);

    // Precompute height map for the entire chunk
    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int z = 0; z < Chunk.DEPTH; z++) {
        float wx = position.x + x;
        float wz = position.z + z;

        // Generate and scale noise value
        float noiseValue = (float) noise.noise(wx * scale, wz * scale);
        int heightValue = (int) Mathf.map(noiseValue, 0, 1, 0, heightMultiplier) + baseHeight;

        float dirtNoiseValue = (float) dirtNoise.noise(wx * dirtScale, wz * dirtScale);

        chunk.getHeightMap()[x + z * Chunk.WIDTH] = heightValue;

        // Fill blocks based on height value
        for (int y = 0; y <= heightValue; y++) {
          if (y < 40 && y > 20) {
            if (dirtNoiseValue >= 0.5f) {
              chunk.setBlockAt(BlockType.GRASS_BLOCK, x, y, z);
            } else {
              chunk.setBlockAt(BlockType.DIRT, x, y, z);
            }

          } else {
            chunk.setBlockAt(BlockType.STONE, x, y, z);
          }
        }

        // Generate trees consistently using the seeded RNG
        if (rng.nextFloat() < 0.05f // 5% chance
            && chunk.getBlockData(x, heightValue, z) == BlockType.DIRT.getId()) {
          int treeHeight = rng.nextInt(6) + 5; // Random tree height between 5 and 10
          int trunkBase = heightValue;

          // Generate tree stem (trunk)
          for (int y = trunkBase; y < trunkBase + treeHeight; y++) {
            chunk.setBlockAt(BlockType.BIRCH_WOOD, x, y, z);
          }

          // Generate tree stem (trunk)
          for (int i = 0; i < 3; i++) {
            chunk.setBlockAt(BlockType.LEAF, x, trunkBase + treeHeight + i, z);
          }

          chunk.getHeightMap()[x + z * Chunk.WIDTH] = heightValue + treeHeight + 3;
        }
      }
    }
  }
}
