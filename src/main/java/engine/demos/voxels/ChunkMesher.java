package engine.demos.voxels;

import java.util.ArrayList;

import engine.components.StaticGeometry;
import engine.processing.BufferedShape;
import engine.render.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import math.Vector2f;

public class ChunkMesher {

  private float blockSize = 1.0f;
  private float radius = blockSize * 0.5f;

  private Chunk chunk;
  private ChunkManager chunkManager;
  //  private Mesh3D blockMesh;
  private BufferedShape shape;
  private StaticGeometry geometry;

  private static ArrayList<Vector2f> uvs;
  public static Material sharedMaterial;
  private static TextureAtlas textureAtlas;

  static {
    sharedMaterial = new Material();
    textureAtlas = new TextureAtlas();
    Texture texture = textureAtlas.getTexture();
    texture.setFilterMode(FilterMode.POINT);
    sharedMaterial.setDiffuseTexture(texture);
    uvs = textureAtlas.getUVCoordinates();
  }

  public ChunkMesher(Chunk chunk, ChunkManager chunkManager) {
    this.chunk = chunk;
    this.chunkManager = chunkManager;
  }

  private int getBlockData(int x, int y, int z) {
    return chunk.getBlockData(x, y, z);
  }

  public StaticGeometry createMeshFromBlockData(BufferedShape shape) {
    this.shape = shape;
    //    blockMesh = new Mesh3D();

    shape.begin(BufferedShape.QUADS);
    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int z = 0; z < Chunk.DEPTH; z++) {
        int heightValue = chunk.getHeightValueAt(x, z);
        // FIXME We can improve this by using max height value from the height map
        //        for (int y = 0; y < height; y++) {
        for (int y = 0; y <= heightValue; y++) {
          createBlock(x, y, z);
        }
      }
    }
    shape.end();

    geometry = new StaticGeometry(shape.getVBO(), sharedMaterial);

    return geometry;
  }

  private boolean isSolid(int x, int y, int z) {
    // Avoid render invisible bottom layer
    if (y < 0) return true;

    // Check if the block is within the current chunk
    if (x >= 0 && x < Chunk.WIDTH && z >= 0 && z < Chunk.DEPTH) {
      return chunk.isBlockSolid(x, y, z);
    }

    // Handle neighbor chunks
    int neighborChunkX = chunk.getChunkX() + (x < 0 ? -1 : x >= Chunk.WIDTH ? 1 : 0);
    int neighborChunkZ = chunk.getChunkZ() + (z < 0 ? -1 : z >= Chunk.DEPTH ? 1 : 0);
    Chunk neighborChunk = chunkManager.getChunk(neighborChunkX, neighborChunkZ);

    if (neighborChunk != null) {
      int neighborX = (x + Chunk.WIDTH) % Chunk.WIDTH;
      int neighborZ = (z + Chunk.DEPTH) % Chunk.DEPTH;
      return neighborChunk.isBlockSolid(neighborX, y, neighborZ);
    }

    // If no neighbor chunk, assume the block is not solid
    return false;
  }

  private void createBaseBlock(int x, int y, int z) {
    int blockId = chunk.getBlockData(x, y, z);
    int[] uvIndices = textureAtlas.getUVIndices(blockId);

    Vector2f uv0 = uvs.get(uvIndices[0]);
    Vector2f uv1 = uvs.get(uvIndices[1]);
    Vector2f uv2 = uvs.get(uvIndices[2]);
    Vector2f uv3 = uvs.get(uvIndices[3]);

    // Top Face
    if (!isSolid(x, y + 1, z)) {
      shape.vertex(+radius + x, -radius - y, -radius + z, uv0.x, uv0.y); // 4
      shape.vertex(-radius + x, -radius - y, -radius + z, uv1.x, uv1.y); // 7
      shape.vertex(-radius + x, -radius - y, +radius + z, uv2.x, uv2.y); // 6
      shape.vertex(+radius + x, -radius - y, +radius + z, uv3.x, uv3.y); // 5
    }

    // Bottom Face
    if (!isSolid(x, y - 1, z)) {
      shape.vertex(+radius + x, +radius - y, -radius + z, uv0.x, uv0.y); // 0
      shape.vertex(+radius + x, +radius - y, +radius + z, uv1.x, uv1.y); // 1
      shape.vertex(-radius + x, +radius - y, +radius + z, uv2.x, uv2.y); // 2
      shape.vertex(-radius + x, +radius - y, -radius + z, uv3.x, uv3.y); // 3
    }

    // Front Face (+z)
    if (!isSolid(x, y, z + 1)) {
      shape.vertex(+radius + x, -radius - y, +radius + z, uv0.x, uv0.y); // 5
      shape.vertex(-radius + x, -radius - y, +radius + z, uv1.x, uv1.y); // 6
      shape.vertex(-radius + x, +radius - y, +radius + z, uv2.x, uv2.y); // 2
      shape.vertex(+radius + x, +radius - y, +radius + z, uv3.x, uv3.y); // 1
    }

    // Back Face (-z)
    if (!isSolid(x, y, z - 1)) {
      shape.vertex(-radius + x, -radius - y, -radius + z, uv0.x, uv0.y); // 7
      shape.vertex(+radius + x, -radius - y, -radius + z, uv1.x, uv1.y); // 4
      shape.vertex(+radius + x, +radius - y, -radius + z, uv2.x, uv2.y); // 0
      shape.vertex(-radius + x, +radius - y, -radius + z, uv3.x, uv3.y); // 3
    }

    // Right Face (+x)
    if (!isSolid(x + 1, y, z)) {
      shape.vertex(+radius + x, -radius - y, -radius + z, uv0.x, uv0.y); // 4
      shape.vertex(+radius + x, -radius - y, +radius + z, uv1.x, uv1.y); // 5
      shape.vertex(+radius + x, +radius - y, +radius + z, uv2.x, uv2.y); // 1
      shape.vertex(+radius + x, +radius - y, -radius + z, uv3.x, uv3.y); // 0
    }

    // Left Face (-x)
    if (!isSolid(x - 1, y, z)) {
      shape.vertex(-radius + x, -radius - y, +radius + z, uv0.x, uv0.y); // 6
      shape.vertex(-radius + x, -radius - y, -radius + z, uv1.x, uv1.y); // 7
      shape.vertex(-radius + x, +radius - y, -radius + z, uv2.x, uv2.y); // 3
      shape.vertex(-radius + x, +radius - y, +radius + z, uv3.x, uv3.y); // 2
    }
  }

  private void createBlock(int x, int y, int z) {
    if (getBlockData(x, y, z) == 0) return;

    int blockId = chunk.getBlockData(x, y, z);

    if (blockId == BlockType.GRASS.getId()) {
      createBillBoard(x, y, z);
    } else {
      createBaseBlock(x, y, z);
    }
  }

  private void createBillBoard(int x, int y, int z) {
    int blockId = chunk.getBlockData(x, y, z);
    int[] uvIndices = textureAtlas.getUVIndices(blockId);
    Vector2f uv0 = uvs.get(uvIndices[0]);
    Vector2f uv1 = uvs.get(uvIndices[1]);
    Vector2f uv2 = uvs.get(uvIndices[2]);
    Vector2f uv3 = uvs.get(uvIndices[3]);

    //
    shape.vertex(+radius + x, -radius - y, +radius + z, uv0.x, uv0.y); // 5
    shape.vertex(-radius + x, -radius - y, -radius + z, uv1.x, uv1.y); // 7
    shape.vertex(-radius + x, +radius - y, -radius + z, uv2.x, uv2.y); // 3
    shape.vertex(+radius + x, +radius - y, +radius + z, uv3.x, uv3.y); // 1

    //
    shape.vertex(+radius + x, -radius - y, -radius + z, uv0.x, uv0.y); // 4
    shape.vertex(-radius + x, -radius - y, +radius + z, uv1.x, uv1.y); // 6
    shape.vertex(-radius + x, +radius - y, +radius + z, uv2.x, uv2.y); // 2
    shape.vertex(+radius + x, +radius - y, -radius + z, uv3.x, uv3.y); // 0
  }
}
