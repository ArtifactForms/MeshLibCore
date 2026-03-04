package voxels.mesh;

import mesh.Mesh3D;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TranslateModifier;
import voxels.world.BlockAccess;
import voxels.world.Blocks;
import voxels.world.Chunk;
import voxels.world.Region;
import voxels.world.VoxelWorld;
import voxels.render.ProceduralBlockAtlas;

public class RegionMesher {

  private Mesh3D mesh;
  private int nextIndex;
  private BlockAccess blocks;
  private final ProceduralBlockAtlas atlas;

  public RegionMesher(ProceduralBlockAtlas atlas) {
    if (atlas == null) {
      throw new IllegalArgumentException("atlas cannot be null");
    }
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

              // Atlas-mapped block textures should repeat per block. With the current atlas UV scheme,
              // merged greedy quads would stretch a single tile over multiple blocks, so clamp merge
              // extent to one block per face to preserve per-block texel density.
              w = 1;
              h = 1;

              x[u] = i;
              x[v] = j;

              int[] du = new int[3];
              int[] dv = new int[3];

              du[u] = w;
              dv[v] = h;

              addQuad(x, du, dv, c > 0, (short) Math.abs(c), d);

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

  private void addQuad(int[] pos, int[] du, int[] dv, boolean frontFace, short blockId, int axis) {

    float x = pos[0];
    float y = pos[1];
    float z = pos[2];

    float[] v0 = {x, y, z};
    float[] v1;
    float[] v2;
    float[] v3;

    float[] duv = {du[0], du[1], du[2]};
    float[] dvv = {dv[0], dv[1], dv[2]};

    float[] xdu = {x + duv[0], y + duv[1], z + duv[2]};
    float[] xdv = {x + dvv[0], y + dvv[1], z + dvv[2]};
    float[] xduv = {x + duv[0] + dvv[0], y + duv[1] + dvv[1], z + duv[2] + dvv[2]};

    if (frontFace) {
      v1 = xdu;
      v2 = xduv;
      v3 = xdv;
    } else {
      v1 = xdv;
      v2 = xduv;
      v3 = xdu;
    }

    float[] expected = expectedFaceNormal(axis, frontFace);
    float[] actual = faceNormal(v0, v1, v2);

    // Ensure deterministic outward winding for all axes.
    if (dot(actual, expected) < 0f) {
      float[] tmp = v1;
      v1 = v3;
      v3 = tmp;
    }

    mesh.addVertex(v0[0], v0[1], v0[2]);
    mesh.addVertex(v1[0], v1[1], v1[2]);
    mesh.addVertex(v2[0], v2[1], v2[2]);
    mesh.addVertex(v3[0], v3[1], v3[2]);

    int faceIndex = mesh.getFaceCount();
    mesh.addFace(nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3);

    int faceOrdinal = toFaceOrdinal(axis, frontFace);
    mesh.getSurfaceLayer().setFaceUVIndices(faceIndex, atlas.getFaceUVIndices(blockId, faceOrdinal));

    nextIndex += 4;
  }

  private float[] expectedFaceNormal(int axis, boolean frontFace) {
    float sign = frontFace ? 1f : -1f;
    if (axis == 0) {
      return new float[] {sign, 0f, 0f};
    }
    if (axis == 1) {
      return new float[] {0f, sign, 0f};
    }
    return new float[] {0f, 0f, sign};
  }

  private float[] faceNormal(float[] v0, float[] v1, float[] v2) {
    float ax = v1[0] - v0[0];
    float ay = v1[1] - v0[1];
    float az = v1[2] - v0[2];

    float bx = v2[0] - v0[0];
    float by = v2[1] - v0[1];
    float bz = v2[2] - v0[2];

    return new float[] {
      ay * bz - az * by,
      az * bx - ax * bz,
      ax * by - ay * bx
    };
  }

  private float dot(float[] a, float[] b) {
    return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
  }

  private int toFaceOrdinal(int axis, boolean frontFace) {
    if (axis == 1) {
      return frontFace ? ProceduralBlockAtlas.FACE_TOP : ProceduralBlockAtlas.FACE_BOTTOM;
    }
    if (axis == 2) {
      return frontFace ? ProceduralBlockAtlas.FACE_FRONT : ProceduralBlockAtlas.FACE_BACK;
    }
    return frontFace ? ProceduralBlockAtlas.FACE_RIGHT : ProceduralBlockAtlas.FACE_LEFT;
  }
}
