package server.world.structures.fin;

import java.util.Random;

import common.game.block.Blocks;
import common.world.ChunkData;

public class TreeStructureGenerator implements StructureGenerator {

  private final int maxRadius = 5;

//  private static final float BASE_TREE_CHANCE = 0.12f;
  private static final float BASE_TREE_CHANCE = 0.2f;

  @Override
  public int getMaxRadius() {
    return maxRadius;
  }

  @Override
  public void generateAt(ChunkData chunk, int centerX, int centerZ, long seed) {

    long posSeed = seed ^ (centerX * 341873128712L) ^ (centerZ * 132897987541L);
    Random rng = new Random(posSeed);

    float density = forestDensity(seed, centerX, centerZ);

    float spawnChance = BASE_TREE_CHANCE + density * 0.35f;

    if (rng.nextFloat() > spawnChance) return;

    int worldX = centerX * ChunkData.WIDTH + ChunkData.WIDTH / 2;
    int worldZ = centerZ * ChunkData.DEPTH + ChunkData.DEPTH / 2;

    generateSingleTree(chunk, worldX, worldZ, rng);

    // ---- Cluster Bäume ----
    int cluster = rng.nextInt(3);

    for (int i = 0; i < cluster; i++) {

      int ox = worldX + rng.nextInt(10) - 5;
      int oz = worldZ + rng.nextInt(10) - 5;

      generateSingleTree(chunk, ox, oz, rng);
    }
  }

  private void generateSingleTree(ChunkData chunk, int worldX, int worldZ, Random rng) {

    int chunkWorldX = chunk.getChunkX() * ChunkData.WIDTH;
    int chunkWorldZ = chunk.getChunkZ() * ChunkData.DEPTH;

    int localX = worldX - chunkWorldX;
    int localZ = worldZ - chunkWorldZ;

    if (localX < 0 || localZ < 0 || localX >= ChunkData.WIDTH || localZ >= ChunkData.DEPTH) return;

    int groundY = chunk.getHeightValue(localX, localZ);

    if (groundY <= 8) return;

    if (chunk.getBlock(localX, groundY, localZ) != Blocks.GRASS_BLOCK) return;

    int treeHeight = 6 + rng.nextInt(3);
    int topY = groundY + treeHeight;

    // ---- TRUNK ----
    for (int i = 0; i < treeHeight; i++) {

      int wx = worldX;
      int wy = groundY + 1 + i;
      int wz = worldZ;

      int lx = wx - chunkWorldX;
      int lz = wz - chunkWorldZ;

      if (chunk.isInside(lx, wy, lz)) {
        chunk.setBlockAt(Blocks.OAK_WOOD, lx, wy, lz);
      }
    }

    // ---- LEAVES ----
    int radius = 3;

    for (int dx = -radius; dx <= radius; dx++) {
      for (int dz = -radius; dz <= radius; dz++) {
        for (int dy = -radius; dy <= radius; dy++) {

          if (dx * dx + dy * dy + dz * dz > radius * radius) continue;

          int wx = worldX + dx;
          int wy = topY + dy;
          int wz = worldZ + dz;

          int lx = wx - chunkWorldX;
          int lz = wz - chunkWorldZ;

          if (chunk.isInside(lx, wy, lz)) {

            if (chunk.getBlock(lx, wy, lz) == Blocks.AIR) {
              chunk.setBlockAt(Blocks.LEAF, lx, wy, lz);
            }
          }
        }
      }
    }
  }

  // ---- Forest Density ----
  private float forestDensity(long seed, int x, int z) {

    long s = seed ^ (x * 918273645L) ^ (z * 12378123L);
    Random r = new Random(s);

    return r.nextFloat();
  }
}
