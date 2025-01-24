package engine.demos.voxels;

import math.Mathf;
import math.PerlinNoise;
import math.PerlinNoise3;
import math.Vector3f;
import java.util.Random;

public class ChunkGenerator3 implements ChunkGenerator {

  private long seed = 0; // Global seed
  private int baseHeight = 0;
  private float scale = 0.005f; // Base noise scale
  private int heightMultiplier = 200;

  private int octaves = 4; // Number of octaves
  private float persistence = 0.5f; // Controls amplitude decay per octave
  private float lacunarity = 2.0f; // Controls frequency increase per octave

  private Random rng;
  private PerlinNoise noise;
  private PerlinNoise biomeNoise;
  private PerlinNoise3 caveNoise;

  public ChunkGenerator3(long seed) {
    this.seed = seed;
    noise = new PerlinNoise(seed);
    biomeNoise = new PerlinNoise(seed);
    caveNoise = new PerlinNoise3(seed);
  }

  public void generate(Chunk chunk) {
    Vector3f position = chunk.getPosition();

    // Create a seeded RNG for this chunk based on global seed and chunk position
    long chunkSeed = seed ^ ((long) (position.x * 31 + position.z * 17));
    rng = new Random(chunkSeed);

    // Precompute height map for the entire chunk
    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int z = 0; z < Chunk.DEPTH; z++) {
        float wx = position.x + x;
        float wz = position.z + z;

        // Determine biome for this column
        BiomeType biome = getBiomeAt(wx, wz);

        // Generate height with octaves
        float noiseValue =
            generateOctaveNoise(noise, wx, wz, scale, octaves, persistence, lacunarity);
        //        int heightValue = (int) Mathf.map(noiseValue, -1, 1, -100, 200) + baseHeight;
        int heightValue = (int) Mathf.map(noiseValue, 0, 1, 0, heightMultiplier) + baseHeight;

        chunk.getHeightMap()[x + z * Chunk.WIDTH] = heightValue;

        // Fill blocks based on height value with layered texturing
        for (int y = 0; y <= heightValue; y++) {
          float wy = position.y + y;

          BlockType blockType = getBlockType(x, y, z, heightValue, biome);
          chunk.setBlockAt(blockType, x, y, z);

//          float caveNoiseScale = 0.05f;
//          if (y < 60) {
//            if (caveNoise.noise(wx * caveNoiseScale, wy * caveNoiseScale, wz * caveNoiseScale)
//                > 0.6f) {
//              chunk.setBlockAt(BlockType.AIR, x, y, z);
//            }
//          }
        }
      }
    }

    createTrees(chunk);
  }

  private void createTrees(Chunk chunk) {
    Vector3f position = chunk.getPosition();

    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int z = 0; z < Chunk.DEPTH; z++) {
        float wx = position.x + x;
        float wz = position.z + z;

        int heightValue = chunk.getHeightValueAt(x, z);

        // Determine biome for this column
        BiomeType biome = getBiomeAt(wx, wz);
        BlockType blockType = chunk.getBlock(x, heightValue, z);
        if (!(blockType == BlockType.GRASS_BLOCK || blockType == BlockType.SAND)) continue;

        if (rng.nextFloat() < 0.02f // 2% chance
        ) {

          //        BiomeType biome = getBiomeAt(wx, wz);

          int treeHeight =
              biome == BiomeType.FOREST
                  ? rng.nextInt(5) + 6
                  : rng.nextInt(4) + 4; // Taller trees in forests
          int trunkBase = heightValue;

          // Generate tree trunk
          for (int y = trunkBase; y < trunkBase + treeHeight; y++) {
            chunk.setBlockAt(BlockType.WOOD, x, y, z);
          }

          // Generate leaves based on biome
          if (biome == BiomeType.FOREST) {
            generateLeafCluster(chunk, x, trunkBase + treeHeight, z, 3); // Dense cluster
          } else if (biome == BiomeType.PLAINS) {
            generateLeafCluster(chunk, x, trunkBase + treeHeight, z, 2); // Smaller cluster
          } else if (biome == BiomeType.DESERT) {
            // Generate cactus-like structure
            for (int y = trunkBase; y < trunkBase + treeHeight; y++) {
              chunk.setBlockAt(BlockType.CACTUS, x, y, z);
            }
            return;
          }

//          // Add vines or other features for specific biomes
//          if (biome == BiomeType.FOREST && rng.nextFloat() < 0.1f) { // 10% chance for vines
//            chunk.setBlockAt(BlockType.VINE, x, trunkBase + treeHeight - 1, z - 1);
//          }
        }
      }
    }
  }

  // Generate leaf clusters (helper method)
  private void generateLeafCluster(Chunk chunk, int x, int baseHeight, int z, int radius) {
    for (int dx = -radius; dx <= radius; dx++) {
      for (int dz = -radius; dz <= radius; dz++) {
        for (int dy = -radius / 2; dy <= radius / 2; dy++) { // Vertical thickness of leaves
          int distance = Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
          if (distance <= radius && chunk.isWithinBounds(x + dx, baseHeight + dy, z + dz)) {
            chunk.setBlockAt(BlockType.LEAF, x + dx, baseHeight + dy, z + dz);
          }
        }
      }
    }
  }

  //  private BlockType getBlockType(int x, int y, int z, int heightValue, BiomeType biome) {
  //      switch (biome) {
  //        case DESERT:
  //          return y == heightValue ? BlockType.SAND : (y < heightValue - 3 ? BlockType.STONE :
  // BlockType.SAND_STONE);
  //        case PLAINS:
  //          return y == heightValue ? BlockType.GRASS_BLOCK : (y >= heightValue - 3 ?
  // BlockType.DIRT : BlockType.STONE);
  //        case FOREST:
  //          return y == heightValue ? BlockType.GRASS_BLOCK : (y >= heightValue - 3 ?
  // BlockType.DIRT : BlockType.STONE);
  //        case MOUNTAIN:
  //          return y > heightValue - 10 ? BlockType.STONE : BlockType.SNOW;
  //        case SNOW:
  //          return BlockType.SNOW;
  //        default:
  //          return BlockType.AIR;
  //      }
  //    }

  private BiomeType getBiomeAt(float wx, float wz) {
    float biomeNoiseScale = 0.004f;

    float biomeValue =
        (float)
            biomeNoise.noise(
                wx * biomeNoiseScale, wz * biomeNoiseScale); // Scale down for biome regions

    if (biomeValue < 0.2f) {
      return BiomeType.DESERT;
    } else if (biomeValue < 0.5f) {
      return BiomeType.PLAINS;
    } else if (biomeValue < 0.8f) {
      return BiomeType.FOREST;
    } else {
      return BiomeType.MOUNTAIN;
    }
  }

  private BlockType getBlockType(int x, int y, int z, int heightValue, BiomeType biome) {
    if (y > 140) {
      return BlockType.SNOW;
    }

    if (y > 110) {
      return BlockType.STONE;
    }

    // Simulate layers: Grass on top, then dirt, then stone
    if (y == heightValue) {
      // Topmost layer is grass
      return biome == BiomeType.DESERT ? BlockType.SAND : BlockType.GRASS_BLOCK;
    } else if (y >= heightValue - 3) {
      // Next 3 layers are dirt
      return BlockType.DIRT;
    } else if (y < heightValue - 3) {
      // Below dirt, use stone
      return BlockType.STONE;
    }

    return BlockType.AIR; // Default, though we shouldn't hit this
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
    float maxValue = 0; // Used for normalizing

    for (int i = 0; i < octaves; i++) {
      total += noise.noise(x * frequency, z * frequency) * amplitude;
      maxValue += amplitude;

      amplitude *= persistence; // Decay amplitude
      frequency *= lacunarity; // Increase frequency
    }

    return total / maxValue; // Normalize to -1..1
  }
}
