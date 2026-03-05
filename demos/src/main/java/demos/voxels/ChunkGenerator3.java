package demos.voxels;

import java.util.Random;

import math.Mathf;
import math.PerlinNoise;
import math.PerlinNoise3;
import math.Vector3f;

public class ChunkGenerator3 implements ChunkGenerator {

  private int seaLevel = 80;
  private int beachSize = 3;
  private long seed;
  private Random rng;

  // Noise layers
  private PerlinNoise continentNoise;
  private PerlinNoise terrainNoise;
  private PerlinNoise biomeNoise;
  private PerlinNoise treeNoise;
  private PerlinNoise riverNoise;
  private PerlinNoise3 caveNoise;

  // Terrain params
  private float terrainScale = 0.01f;
  private int heightMultiplier = 180;
  private int octaves = 3;
  private float persistence = 0.5f;
  private float lacunarity = 2.0f;

  public ChunkGenerator3(long seed) {
    this.seed = seed;
    continentNoise = new PerlinNoise(seed + 12345);
    terrainNoise = new PerlinNoise(seed + 67890);
    biomeNoise = new PerlinNoise(seed + 11111);
    treeNoise = new PerlinNoise(seed + 22222);
    riverNoise = new PerlinNoise(seed + 33333);
    caveNoise = new PerlinNoise3(seed + 44444);
  }

  @Override
  public void generate(Chunk chunk) {
    Vector3f pos = chunk.getPosition();
    long chunkSeed = seed ^ ((long) (pos.x * 31 + pos.z * 17));
    rng = new Random(chunkSeed);

    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int z = 0; z < Chunk.DEPTH; z++) {
        float wx = pos.x + x;
        float wz = pos.z + z;

        // --- Biome Layer ---
        BiomeType biome = getBiomeAt(wx, wz);

        // --- Continent + Terrain Layer ---
        float continent = generateOctaveNoise(continentNoise, wx, wz, 0.0005f, 3, 0.5f, 2f);
        float terrain =
            generateOctaveNoise(
                terrainNoise, wx, wz, terrainScale, octaves, persistence, lacunarity);

        float finalHeight = continent * 0.7f + terrain * 0.3f;
        int heightValue = (int) Mathf.map(finalHeight, 0, 1, 0, heightMultiplier);

        // --- River Layer ---
        float river = Mathf.abs((float) riverNoise.noise(wx * 0.001f, wz * 0.001f));
        if (river < 0.02f) heightValue -= 10; // river carve

        chunk.getHeightMap()[x + z * Chunk.WIDTH] = heightValue;

        // --- Block Layer ---
        for (int y = 0; y <= heightValue; y++) {
          BlockType blockType = getBlockType(x, y, z, heightValue, biome, chunk);
          chunk.setBlockAt(blockType, x, y, z);
        }
      }
    }

    // --- Feature Layers ---
    createTrees(chunk);
    createWater(chunk);
//    createCaves(chunk);
  }

  private void createCaves(Chunk chunk) {
    Vector3f pos = chunk.getPosition();
    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int z = 0; z < Chunk.DEPTH; z++) {
        int heightValue = chunk.getHeightValueAt(x, z);
        for (int y = 0; y <= heightValue; y++) {
          float wX = pos.x + x;
          float wY = pos.y + y;
          float wZ = pos.z + z;

          float cave = (float) caveNoise.noise(wX * 0.05f, wY * 0.05f, wZ * 0.05f);
          float depthFactor = 1.0f - ((float) y / 60f);
          if (cave > 0.6f + depthFactor * 0.2f) {
            chunk.setBlockAt(BlockType.AIR, x, y, z);
          }
        }
      }
    }
  }

  private BiomeType getBiomeAt(float wx, float wz) {
    float temp = (float) biomeNoise.noise(wx * 0.002f, wz * 0.002f);
    float moisture = (float) biomeNoise.noise(wx * 0.002f + 1000, wz * 0.002f + 1000);

    if (temp > 0.7 && moisture < 0.3f) return BiomeType.DESERT;
    if (temp > 0.6 && moisture > 0.6f) return BiomeType.FOREST;
    if (temp < 0.3f) return BiomeType.SNOW;
    return BiomeType.PLAINS;
  }

  private BlockType getBlockType(
      int x, int y, int z, int heightValue, BiomeType biome, Chunk chunk) {
    float wx = chunk.getPosition().x + x;
    float wz = chunk.getPosition().z + z;

    // Beaches
    if (y < seaLevel - beachSize - 8) return BlockType.GRAVEL;
    if (y < seaLevel + beachSize) return BlockType.SAND;

    // Mountain tops
    if (y > 140) return BlockType.SNOW;
    if (y > 110) {
      return terrainNoise.noise(wx * 0.05f, wz * 0.05f) < 0.7f
          ? BlockType.STONE
          : BlockType.COBBLE_STONE;
    }

    // Layers: Grass, Dirt, Stone
    if (y == heightValue) return biome == BiomeType.DESERT ? BlockType.SAND : BlockType.GRASS_BLOCK;
    if (y >= heightValue - 3) return BlockType.DIRT;
    return BlockType.STONE;
  }

  private void createTrees(Chunk chunk) {
    Vector3f pos = chunk.getPosition();

    boolean[][] treeMap = new boolean[Chunk.WIDTH][Chunk.DEPTH];

    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int z = 0; z < Chunk.DEPTH; z++) {
        int heightValue = chunk.getHeightValueAt(x, z);
        if (heightValue <= seaLevel) continue;

        BiomeType biome = getBiomeAt(pos.x + x, pos.z + z);
        BlockType topBlock = chunk.getBlock(x, heightValue, z);
        if (!(topBlock == BlockType.GRASS_BLOCK || topBlock == BlockType.SAND)) continue;

        int hL = chunk.getHeightValueAt(Math.max(x - 1, 0), z);
        int hR = chunk.getHeightValueAt(Math.min(x + 1, Chunk.WIDTH - 1), z);
        int hF = chunk.getHeightValueAt(x, Math.max(z - 1, 0));
        int hB = chunk.getHeightValueAt(x, Math.min(z + 1, Chunk.DEPTH - 1));
        float slope = Math.abs(hL - hR) + Math.abs(hF - hB);

        float baseChance = (float) treeNoise.noise((pos.x + x) * 0.01f, (pos.z + z) * 0.01f);
        float slopeFactor = 1.0f - Mathf.clamp(slope / 5f, 0f, 1f);
        float finalChance = baseChance * slopeFactor;

        int minDistance = biome == BiomeType.FOREST ? 2 : 3;
        if (finalChance > 0.65f && isAreaClear(treeMap, x, z, minDistance)) {
          int treeHeight = biome == BiomeType.FOREST ? rng.nextInt(5) + 6 : rng.nextInt(4) + 4;
          BlockType treeType = rng.nextFloat() < 0.08f ? BlockType.BIRCH_WOOD : BlockType.OAK_WOOD;

          for (int y = heightValue; y < heightValue + treeHeight; y++) {
            chunk.setBlockAt(treeType, x, y, z);
          }
          generateLeafCluster(
              chunk, x, heightValue + treeHeight, z, biome == BiomeType.FOREST ? 3 : 2);

          treeMap[x][z] = true;
        }
      }
    }
  }
  
  private boolean isAreaClear(boolean[][] treeMap, int x, int z, int radius) {
    int startX = Math.max(x - radius, 0);
    int endX = Math.min(x + radius, Chunk.WIDTH - 1);
    int startZ = Math.max(z - radius, 0);
    int endZ = Math.min(z + radius, Chunk.DEPTH - 1);

    for (int i = startX; i <= endX; i++) {
      for (int j = startZ; j <= endZ; j++) {
        if (treeMap[i][j]) return false;
      }
    }
    return true;
  }

  private void generateLeafCluster(Chunk chunk, int x, int baseHeight, int z, int radius) {
    for (int dx = -radius; dx <= radius; dx++) {
      for (int dz = -radius; dz <= radius; dz++) {
        for (int dy = -radius / 2; dy <= radius / 2; dy++) {
          int dist = Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
          if (dist <= radius && chunk.isWithinBounds(x + dx, baseHeight + dy, z + dz)) {
            chunk.setBlockAt(BlockType.LEAF, x + dx, baseHeight + dy, z + dz);
          }
        }
      }
    }
  }

  private void createWater(Chunk chunk) {
    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int z = 0; z < Chunk.DEPTH; z++) {
        for (int y = 0; y < seaLevel; y++) {
          BlockType type = chunk.getBlock(x, y, z);
          if (type == BlockType.AIR) {
            chunk.setBlockAt(BlockType.WATER, x, y, z);
          }
        }
      }
    }
  }

  private float generateOctaveNoise(
      PerlinNoise noise,
      float x,
      float z,
      float baseScale,
      int octaves,
      float persistence,
      float lacunarity) {
    float total = 0;
    float amplitude = 1;
    float frequency = baseScale;
    float max = 0;

    for (int i = 0; i < octaves; i++) {
      total += noise.noise(x * frequency, z * frequency) * amplitude;
      max += amplitude;
      amplitude *= persistence;
      frequency *= lacunarity;
    }

    return total / max;
  }
}
