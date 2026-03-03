package voxels.mesh;

import mesh.Mesh3D;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TranslateModifier;
import voxels.world.Blocks;
import voxels.world.Chunk;
import voxels.world.VoxelWorld;

public class ChunkMesher {

  private Mesh3D mesh;
  private int nextIndex;

  private Chunk chunk;
  private VoxelWorld world;

  public Mesh3D create(Chunk chunk, VoxelWorld world) {

    this.chunk = chunk;
    this.world = world;
    this.mesh = new Mesh3D();
    this.nextIndex = 0;

    greedyMesh();

    new ScaleModifier(1, -1, 1).modify(mesh);

    float worldX = Chunk.SIZE_X * chunk.getChunkX();
    float worldZ = Chunk.SIZE_Z * chunk.getChunkZ();
    new TranslateModifier(worldX, 0, worldZ).modify(mesh);

    return mesh;
  }

  private void greedyMesh() {

    int[] dims = {Chunk.SIZE_X, Chunk.SIZE_Y, Chunk.SIZE_Z};

    int[] x = new int[3];
    int[] q = new int[3];

    for (int d = 0; d < 3; d++) {

      int u = (d + 1) % 3;
      int v = (d + 2) % 3;

      q[0] = q[1] = q[2] = 0;
      q[d] = 1;

      int[] mask = new int[dims[u] * dims[v]];

      for (x[d] = -1; x[d] < dims[d]; ) {

        // Build mask
        int n = 0;

        for (x[v] = 0; x[v] < dims[v]; x[v]++) {
          for (x[u] = 0; x[u] < dims[u]; x[u]++) {

            short a = getBlock(x[0], x[1], x[2]);
            short b = getBlock(x[0] + q[0], x[1] + q[1], x[2] + q[2]);

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

        // Greedy 2D merge
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
                  if (mask[n + k + h * dims[u]] != c) {
                    break outer;
                  }
                }
              }

              x[u] = i;
              x[v] = j;

              int[] du = new int[3];
              int[] dv = new int[3];

              du[u] = w;
              dv[v] = h;

              addQuad(x, du, dv, c > 0);

              for (int l = 0; l < h; l++) {
                for (int k = 0; k < w; k++) {
                  mask[n + k + l * dims[u]] = 0;
                }
              }

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

  private short getBlock(int x, int y, int z) {

    int worldX = chunk.getChunkX() * Chunk.SIZE_X + x;
    int worldZ = chunk.getChunkZ() * Chunk.SIZE_Z + z;

    return world.getBlock(worldX, y, worldZ);
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
