package voxels.world;

import math.PerlinNoise;

public class NoiseTerrainGenerator implements TerrainGenerator {

  private final PerlinNoise noise;

  public NoiseTerrainGenerator(long seed) {
    this.noise = new PerlinNoise(seed);
  }

  @Override
  public void generate(Chunk chunk) {
    for (int x = 0; x < Chunk.SIZE_X; x++) {
      for (int z = 0; z < Chunk.SIZE_Z; z++) {

        int worldX = chunk.getChunkX() * Chunk.SIZE_X + x;
        int worldZ = chunk.getChunkZ() * Chunk.SIZE_Z + z;

        float noiseScale = 0.01f;
        float heightNoise = noise.sample(worldX * noiseScale, worldZ * noiseScale);
        int height = (int) (heightNoise * 50 + 80);

        chunk.setHeight(x, z, height);

        for (int y = 0; y <= height; y++) {
          chunk.setBlock(x, y, z, Blocks.STONE);
        }
      }
    }
  }
}
