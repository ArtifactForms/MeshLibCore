package server.world.structures.fin;

import java.util.Collection;
import java.util.Random;

import common.game.block.BlockIds;
import common.game.block.BlockRegistry;
import common.game.block.Blocks;
import common.world.ChunkData;
import math.Bounds;
import math.Vector3f;
import voxels.Voxel;

public class MeshStructureGenerator implements StructureGenerator {

  private final VoxelStructure structure;
  private final int maxRadius;
  private final float spawnChance;

  public MeshStructureGenerator(VoxelStructure structure, int maxRadius, float spawnChance) {
    this.structure = structure;
    this.maxRadius = maxRadius;
    this.spawnChance = spawnChance;
  }

  @Override
  public int getMaxRadius() {
    return maxRadius;
  }

  @Override
  public void generateAt(ChunkData chunk, int centerX, int centerZ, long worldSeed) {
    // 1. Eindeutiger Seed für diesen Punkt
    long posSeed = worldSeed ^ (centerX * 341873128712L) ^ (centerZ * 132897987541L);
    Random rng = new Random(posSeed);

    // 2. Spawn-Chance prüfen
    if (rng.nextFloat() > spawnChance) return;

    // 3. Welt-Zentrum (Mitte des Ziel-Chunks)
    Vector3f origin =
        new Vector3f(
            centerX * ChunkData.WIDTH + (ChunkData.WIDTH / 2f),
            150, // Testwert für die Höhe
            centerZ * ChunkData.DEPTH + (ChunkData.DEPTH / 2f));

    // 4. Bounds-Check (Performance)
    Bounds local = structure.getLocalBounds(posSeed);
    Bounds worldBounds = new Bounds(local.getMin().add(origin), local.getMax().add(origin));

    // Wenn der aktuelle Chunk die Struktur nicht berührt -> Abbruch
    if (!worldBounds.intersects(chunk.getChunkBounds())) {
      return;
    }

    // 5. Voxel setzen
    Collection<Voxel> voxels = structure.getVoxels(posSeed);
    int chunkWorldX = chunk.getChunkX() * ChunkData.WIDTH;
    int chunkWorldZ = chunk.getChunkZ() * ChunkData.DEPTH;

    for (Voxel v : voxels) {
      int wx = (int) (origin.x + v.getX());
      int wy = (int) (origin.y + v.getY());
      int wz = (int) (origin.z + v.getZ());

      int lx = wx - chunkWorldX;
      int lz = wz - chunkWorldZ;

      if (chunk.isInside(lx, wy, lz)) {
        chunk.setBlockAt(BlockRegistry.get(BlockIds.STONE), lx, wy, lz);
      }
    }
  }
}
