package voxels.mesh;

import mesh.Mesh3D;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TranslateModifier;
import voxels.world.BlockAccess;
import voxels.world.Blocks;
import voxels.world.Chunk;
import voxels.world.Region;
import voxels.world.VoxelWorld;

public class RegionMesher {

  private Mesh3D mesh;
  private int nextIndex;
  private BlockAccess blocks;


  public Mesh3D create(Region region, VoxelWorld world) {
    return create(region, (BlockAccess) world);
  }

  public Mesh3D create(Region region, BlockAccess blocks) {

    this.blocks = blocks;
    this.mesh = new Mesh3D();
    this.nextIndex = 0;

    int startChunkX = region.getRegionX() * Region.REGION_SIZE;
    int startChunkZ = region.getRegionZ() * Region.REGION_SIZE;

    int sizeX = Region.REGION_SIZE * Chunk.SIZE_X;
    int sizeZ = Region.REGION_SIZE * Chunk.SIZE_Z;
    int sizeY = computeRegionMaxHeight(region);

    greedyMesh(startChunkX, startChunkZ, sizeX, sizeY, sizeZ);

    new ScaleModifier(1, -1, 1).modify(mesh);

    float worldX = startChunkX * Chunk.SIZE_X;
    float worldZ = startChunkZ * Chunk.SIZE_Z;

    new TranslateModifier(worldX, 0, worldZ).modify(mesh);

    return mesh;
  }

  private int computeRegionMaxHeight(Region region) {
    int max = 0;
    for (Chunk chunk : region.getChunks()) {
      if (chunk.getMaxHeight() > max) {
        max = chunk.getMaxHeight();
      }
    }
    return max + 1;
  }

  private void greedyMesh(int chunkStartX, int chunkStartZ, int sizeX, int sizeY, int sizeZ) {

    int[] dims = {sizeX, sizeY, sizeZ};
    int[] x = new int[3];
    int[] q = new int[3];

    for (int d = 0; d < 3; d++) {

      int u = (d + 1) % 3;
      int v = (d + 2) % 3;

      q[0] = q[1] = q[2] = 0;
      q[d] = 1;

      int[] mask = new int[dims[u] * dims[v]];

      for (x[d] = -1; x[d] < dims[d]; ) {

        int n = 0;

        for (x[v] = 0; x[v] < dims[v]; x[v]++) {
          for (x[u] = 0; x[u] < dims[u]; x[u]++) {

            short a = getBlock(chunkStartX, chunkStartZ, x[0], x[1], x[2]);
            short b = getBlock(chunkStartX, chunkStartZ, x[0] + q[0], x[1] + q[1], x[2] + q[2]);

            if (a != Blocks.AIR && b == Blocks.AIR) {
              mask[n++] = a;
            } else if (a == Blocks.AIR && b != Blocks.AIR) {
              mask[n++] = -b;
            } else {
              mask[n++] = 0;
            }
          }
        }

        x[d]++;
        n = 0;

        for (int j = 0; j < dims[v]; j++) {
          for (int i = 0; i < dims[u]; ) {

            int c = mask[n];

            if (c != 0) {

              int w;
              for (w = 1; i + w < dims[u] && mask[n + w] == c; w++) ;

              int h;
              outer:
              for (h = 1; j + h < dims[v]; h++) {
                for (int k = 0; k < w; k++) {
                  if (mask[n + k + h * dims[u]] != c) break outer;
                }
              }

              x[u] = i;
              x[v] = j;

              int[] du = new int[3];
              int[] dv = new int[3];

              du[u] = w;
              dv[v] = h;

              addQuad(x, du, dv, c > 0);

              for (int l = 0; l < h; l++) for (int k = 0; k < w; k++) mask[n + k + l * dims[u]] = 0;

              i += w;
              n += w;

            } else {
              i++;
              n++;
            }
          }
        }
      }
    }
  }

  private short getBlock(int chunkStartX, int chunkStartZ, int x, int y, int z) {
    int worldX = chunkStartX * Chunk.SIZE_X + x;
    int worldZ = chunkStartZ * Chunk.SIZE_Z + z;
    return blocks.getBlock(worldX, y, worldZ);
  }

  private void addQuad(int[] pos, int[] du, int[] dv, boolean frontFace) {

    float x = pos[0];
    float y = pos[1];
    float z = pos[2];

    float x2 = x + du[0] + dv[0];
    float y2 = y + du[1] + dv[1];
    float z2 = z + du[2] + dv[2];

    if (frontFace) {
      mesh.addVertex(x, y, z);
      mesh.addVertex(x + du[0], y + du[1], z + du[2]);
      mesh.addVertex(x2, y2, z2);
      mesh.addVertex(x + dv[0], y + dv[1], z + dv[2]);
    } else {
      mesh.addVertex(x, y, z);
      mesh.addVertex(x + dv[0], y + dv[1], z + dv[2]);
      mesh.addVertex(x2, y2, z2);
      mesh.addVertex(x + du[0], y + du[1], z + du[2]);
    }

    mesh.addFace(nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3);
    nextIndex += 4;
  }
}
