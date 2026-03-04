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
        int rawHeight = (int) (heightNoise * 50 + 80);
        int height = Math.max(0, Math.min(rawHeight, Chunk.SIZE_Y - 1));

        chunk.setHeight(x, z, height);

        int dirtStart = Math.max(0, height - 3);

        for (int y = 0; y < height; y++) {
          short id = y >= dirtStart ? Blocks.DIRT : Blocks.STONE;
          chunk.setBlock(x, y, z, id);
        }

        chunk.setBlock(x, height, z, Blocks.GRASS);
      }
    }
  }
}
