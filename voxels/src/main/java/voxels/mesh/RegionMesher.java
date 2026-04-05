package voxels.mesh;

import mesh.Mesh3D;
import mesh.modifier.transform.TranslateModifier;
import voxels.render.ProceduralBlockAtlas;
import voxels.world.BlockAccess;
import voxels.world.Blocks;
import voxels.world.Chunk;
import voxels.world.Region;
import voxels.world.VoxelWorld;

public class RegionMesher {

  private Mesh3D mesh;

  private int nextIndex;

  private BlockAccess blocks;

  private final ProceduralBlockAtlas atlas;

  public RegionMesher(ProceduralBlockAtlas atlas) {
    if (atlas == null) throw new IllegalArgumentException("atlas cannot be null");
    this.atlas = atlas;
  }

  public RegionMesher() {
    this(new ProceduralBlockAtlas());
  }

  public Mesh3D create(Region region, VoxelWorld world) {
    return create(region, (BlockAccess) world);
  }

  public Mesh3D create(Region region, BlockAccess blocks) {

    this.blocks = blocks;
    this.mesh = new Mesh3D();
    this.nextIndex = 0;

    atlas.appendUVs(mesh.getSurfaceLayer());

    int startChunkX = region.getRegionX() * Region.REGION_SIZE;
    int startChunkZ = region.getRegionZ() * Region.REGION_SIZE;

    int sizeX = Region.REGION_SIZE * Chunk.SIZE_X;
    int sizeZ = Region.REGION_SIZE * Chunk.SIZE_Z;
    int sizeY = computeRegionMaxHeight(region);

    greedyMesh(startChunkX, startChunkZ, sizeX, sizeY, sizeZ);

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

              x[u] = i;
              x[v] = j;

              int[] du = new int[3];
              int[] dv = new int[3];

              du[u] = 1;
              dv[v] = 1;

              addQuad(x, du, dv, c > 0, (short) Math.abs(c), d);

              mask[n] = 0;
              i++;
              n++;

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

  private void addQuad(int[] pos, int[] du, int[] dv, boolean frontFace, short blockId, int axis) {
    float x = pos[0];
    float y = -pos[1];
    float z = pos[2];

    float[] v0 = {x, y, z};
    float[] v1 = {x + du[0], y - du[1], z + du[2]};
    float[] v2 = {x + du[0] + dv[0], y - du[1] - dv[1], z + du[2] + dv[2]};
    float[] v3 = {x + dv[0], y - dv[1], z + dv[2]};

    float[] a = v0;
    float[] b = v1;
    float[] c = v2;
    float[] d = v3;

    if (frontFace) {
      a = v0;
      b = v3;
      c = v2;
      d = v1;
    } else {
      a = v0;
      b = v1;
      c = v2;
      d = v3;
    }

    if ((axis == 0 && !frontFace)
        || // -X
        (axis == 2 && frontFace)) { // +Z

      float[] tmp = d;
      d = c;
      c = b;
      b = a;
      a = tmp;
    }

    addFace(a, b, c, d, blockId, axis, frontFace);
  }

  private void addFace(
      float[] a, float[] b, float[] c, float[] d, short blockId, int axis, boolean frontFace) {

    mesh.addVertex(a[0], a[1], a[2]);
    mesh.addVertex(b[0], b[1], b[2]);
    mesh.addVertex(c[0], c[1], c[2]);
    mesh.addVertex(d[0], d[1], d[2]);

    int faceIndex = mesh.getFaceCount();
    mesh.addFace(nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3);

    int faceOrdinal = toFaceOrdinal(axis, frontFace);
    mesh.getSurfaceLayer()
        .setFaceUVIndices(faceIndex, atlas.getFaceUVIndices(blockId, faceOrdinal));

    nextIndex += 4;
  }

  private int toFaceOrdinal(int axis, boolean frontFace) {

    if (axis == 1)
      return frontFace ? ProceduralBlockAtlas.FACE_TOP : ProceduralBlockAtlas.FACE_BOTTOM;

    if (axis == 2)
      return frontFace ? ProceduralBlockAtlas.FACE_FRONT : ProceduralBlockAtlas.FACE_BACK;

    return frontFace ? ProceduralBlockAtlas.FACE_RIGHT : ProceduralBlockAtlas.FACE_LEFT;
  }
}
