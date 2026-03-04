package voxels.mesh;

import mesh.Mesh3D;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TranslateModifier;
import voxels.world.Blocks;
import voxels.world.Chunk;
import voxels.world.VoxelWorld;

public class ChunkMesher2 {

  private static final float blockSize = 1.0f;
  private static final float radius = blockSize * 0.5f;

  private int nextIndex;
  private Mesh3D mesh;

  private Chunk chunk;
  private VoxelWorld world;

  public Mesh3D create(Chunk chunk, VoxelWorld world) {
    this.chunk = chunk;
    this.world = world;
    this.mesh = new Mesh3D();
    this.nextIndex = 0;

    for (int x = 0; x < Chunk.SIZE_X; x++) {
      for (int y = 0; y < Chunk.SIZE_Y; y++) {
        for (int z = 0; z < Chunk.SIZE_Z; z++) {
          createBaseBlock(x, y, z);
        }
      }
    }

    new ScaleModifier(1, -1, 1).modify(mesh);

    float worldX = Chunk.SIZE_X * chunk.getChunkX();
    float worldZ = Chunk.SIZE_Z * chunk.getChunkZ();
    new TranslateModifier(worldX, 0, worldZ).modify(mesh);

    return mesh;
  }

  private boolean shouldRenderWorld(int worldX, int worldY, int worldZ) {
    return world.getBlock(worldX, worldY, worldZ) == Blocks.AIR;
  }

  private void addFace() {
    mesh.addFace(nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3);
    nextIndex += 4;
  }

  private void createBaseBlock(int x, int y, int z) {

    short type = chunk.getBlock(x, y, z);
    if (type == Blocks.AIR) return;

    int worldX = chunk.getChunkX() * Chunk.SIZE_X + x;
    int worldY = y;
    int worldZ = chunk.getChunkZ() * Chunk.SIZE_Z + z;

    // (+Y)
    if (shouldRenderWorld(worldX, worldY + 1, worldZ)) {
      mesh.addVertex(-radius + x, +radius + y, -radius + z);
      mesh.addVertex(-radius + x, +radius + y, +radius + z);
      mesh.addVertex(+radius + x, +radius + y, +radius + z);
      mesh.addVertex(+radius + x, +radius + y, -radius + z);
      addFace();
    }

    // (-Y)
    if (shouldRenderWorld(worldX, worldY - 1, worldZ)) {
      mesh.addVertex(-radius + x, -radius + y, -radius + z);
      mesh.addVertex(+radius + x, -radius + y, -radius + z);
      mesh.addVertex(+radius + x, -radius + y, +radius + z);
      mesh.addVertex(-radius + x, -radius + y, +radius + z);
      addFace();
    }

    // (+Z)
    if (shouldRenderWorld(worldX, worldY, worldZ + 1)) {
      mesh.addVertex(-radius + x, -radius + y, +radius + z);
      mesh.addVertex(+radius + x, -radius + y, +radius + z);
      mesh.addVertex(+radius + x, +radius + y, +radius + z);
      mesh.addVertex(-radius + x, +radius + y, +radius + z);
      addFace();
    }

    // (-Z)
    if (shouldRenderWorld(worldX, worldY, worldZ - 1)) {
      mesh.addVertex(-radius + x, -radius + y, -radius + z);
      mesh.addVertex(-radius + x, +radius + y, -radius + z);
      mesh.addVertex(+radius + x, +radius + y, -radius + z);
      mesh.addVertex(+radius + x, -radius + y, -radius + z);
      addFace();
    }

    // (+X)
    if (shouldRenderWorld(worldX + 1, worldY, worldZ)) {
      mesh.addVertex(+radius + x, -radius + y, -radius + z);
      mesh.addVertex(+radius + x, +radius + y, -radius + z);
      mesh.addVertex(+radius + x, +radius + y, +radius + z);
      mesh.addVertex(+radius + x, -radius + y, +radius + z);
      addFace();
    }

    // (-X)
    if (shouldRenderWorld(worldX - 1, worldY, worldZ)) {
      mesh.addVertex(-radius + x, -radius + y, -radius + z);
      mesh.addVertex(-radius + x, -radius + y, +radius + z);
      mesh.addVertex(-radius + x, +radius + y, +radius + z);
      mesh.addVertex(-radius + x, +radius + y, -radius + z);
      addFace();
    }
  }
}