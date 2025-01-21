package engine.demos.voxels;

import engine.components.StaticGeometry;
import engine.render.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import mesh.Face3D;
import mesh.Mesh3D;

public class ChunkMesher {

  private int width;
  private int height;
  private int depth;
  private Chunk chunk;
  private ChunkManager chunkManager;
  private Mesh3D blockMesh;
  private StaticGeometry geometry;

  private static Material sharedMaterial;
  private static TextureAtlas textureAtlas;

  static {
    sharedMaterial = new Material();
    textureAtlas = new TextureAtlas();
    Texture texture = textureAtlas.getTexture();
    texture.setFilterMode(FilterMode.POINT);
    sharedMaterial.setDiffuseTexture(texture);
  }

  public ChunkMesher(Chunk chunk, ChunkManager chunkManager) {
    this.width = chunk.getWidth();
    this.height = chunk.getHeight();
    this.depth = chunk.getDepth();
    this.chunk = chunk;
    this.chunkManager = chunkManager;
    //    chunk.generateData();
  }

  private int getBlockData(int x, int y, int z) {
    return chunk.getBlockData(x, y, z);
  }

  public StaticGeometry createMeshFromBlockData() {
    blockMesh = new Mesh3D();

    synchronized (textureAtlas) {
      blockMesh.setUvs(textureAtlas.getUVCoordinates());
    }

    for (int x = 0; x < width; x++) {
      for (int z = 0; z < depth; z++) {
        int heightValue = chunk.getHeightValueAt(x, z);
        // FIXME We can improve this by using max height value from the height map
        //        for (int y = 0; y < height; y++) {
        for (int y = 0; y <= heightValue; y++) {
          createBlock(x, y, z);
        }
      }
    }
    geometry = new StaticGeometry(blockMesh, sharedMaterial);

    return geometry;
  }

  private boolean isSolid(int x, int y, int z) {
    // Avoid render invisible bottom layer
    if (y < 0) return true;

    // Check if the block is within the current chunk
    if (x >= 0 && x < Chunk.CHUNK_SIZE && z >= 0 && z < Chunk.CHUNK_SIZE) {
      return chunk.isBlockSolid(x, y, z);
    }

    // Handle neighbor chunks
    int neighborChunkX = chunk.getChunkX() + (x < 0 ? -1 : x >= Chunk.CHUNK_SIZE ? 1 : 0);
    int neighborChunkZ = chunk.getChunkZ() + (z < 0 ? -1 : z >= Chunk.CHUNK_SIZE ? 1 : 0);
    Chunk neighborChunk = chunkManager.getChunk(neighborChunkX, neighborChunkZ);

    if (neighborChunk != null) {
      int neighborX = (x + Chunk.CHUNK_SIZE) % Chunk.CHUNK_SIZE;
      int neighborZ = (z + Chunk.CHUNK_SIZE) % Chunk.CHUNK_SIZE;
      return neighborChunk.isBlockSolid(neighborX, y, neighborZ);
    }

    // If no neighbor chunk, assume the block is not solid
    return false;
  }

  private void createBlock(int x, int y, int z) {
    if (getBlockData(x, y, z) == 0) return;

    int indexOffset = blockMesh.getVertexCount();
    float blockSize = 1.0f;
    float radius = blockSize * 0.5f;

    boolean create = false;

    //    int[] uvIndices2 = new int[] {0, 1, 2, 3};

    int blockId = chunk.getBlockData(x, y, z);
    int[] uvIndices = textureAtlas.getUVIndices(blockId);

    // Top Face
    if (!isSolid(x, y + 1, z)) {
      int[] indices =
          new int[] {4 + indexOffset, 7 + indexOffset, 6 + indexOffset, 5 + indexOffset};
      blockMesh.add(new Face3D(indices, uvIndices));
      create = true;
    }

    // Bottom Face
    if (!isSolid(x, y - 1, z)) {
      int[] indices =
          new int[] {0 + indexOffset, 1 + indexOffset, 2 + indexOffset, 3 + indexOffset};
      blockMesh.add(new Face3D(indices, uvIndices));
      create = true;
    }

    // Front Face (+z)
    if (!isSolid(x, y, z + 1)) {
      int[] indices =
          new int[] {5 + indexOffset, 6 + indexOffset, 2 + indexOffset, 1 + indexOffset};
      blockMesh.add(new Face3D(indices, uvIndices));
      create = true;
    }

    // Back Face (-z)
    if (!isSolid(x, y, z - 1)) {
      int[] indices =
          new int[] {7 + indexOffset, 4 + indexOffset, 0 + indexOffset, 3 + indexOffset};
      blockMesh.add(new Face3D(indices, uvIndices));
      create = true;
    }

    // Right Face (+x)
    if (!isSolid(x + 1, y, z)) {
      int[] indices =
          //          new int[] {5 + indexOffset, 1 + indexOffset, 0 + indexOffset, 4 +
          // indexOffset};
          new int[] {4 + indexOffset, 5 + indexOffset, 1 + indexOffset, 0 + indexOffset};

      blockMesh.add(new Face3D(indices, uvIndices));
      create = true;
    }

    // Left Face (-x)
    if (!isSolid(x - 1, y, z)) {
      int[] indices =
          new int[] {6 + indexOffset, 7 + indexOffset, 3 + indexOffset, 2 + indexOffset};
      blockMesh.add(new Face3D(indices, uvIndices));
      create = true;
    }

    if (create) {
      // Add vertices for the block, flipping y to adapt to Processing's -y-up convention
      blockMesh.addVertex(+radius + x, +radius - y, -radius + z); // 0
      blockMesh.addVertex(+radius + x, +radius - y, +radius + z); // 1
      blockMesh.addVertex(-radius + x, +radius - y, +radius + z); // 2
      blockMesh.addVertex(-radius + x, +radius - y, -radius + z); // 3
      blockMesh.addVertex(+radius + x, -radius - y, -radius + z); // 4
      blockMesh.addVertex(+radius + x, -radius - y, +radius + z); // 5
      blockMesh.addVertex(-radius + x, -radius - y, +radius + z); // 6
      blockMesh.addVertex(-radius + x, -radius - y, -radius + z); // 7
    }
  }
}
