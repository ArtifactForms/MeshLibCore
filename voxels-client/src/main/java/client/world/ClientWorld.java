package client.world;

import common.world.World;

/**
 * Client-side implementation of the world. Bridges the gap between raw world data and the visual
 * representation (ChunkManager).
 */
public class ClientWorld extends World {

  private final ChunkManager chunkManager;

  public ClientWorld(ChunkManager chunkManager) {
    this.chunkManager = chunkManager;
  }

  /**
   * Updates a specific chunk with raw block data received from the server. Also triggers mesh
   * updates for the chunk and its neighbors to fix seams. * @param cx The chunk's X coordinate.
   *
   * @param cz The chunk's Z coordinate.
   * @param blocks The raw block ID array.
   */
  public void applyChunkData(int cx, int cz, short[] blocks) {
    Chunk chunk = chunkManager.getOrCreateChunk(cx, cz);

    // Copy block data into the chunk's internal buffer
    System.arraycopy(blocks, 0, chunk.getRawBlockData(), 0, blocks.length);

    // Mark data as ready and flag for mesh reconstruction
    chunk.setDataReady();
    chunk.markDirty();

    // Register chunk in the base World structure
    addChunk(chunk);

    // Trigger neighbor updates to fix ambient occlusion or face culling at borders
    chunkManager.markChunkDirty(cx + 1, cz);
    chunkManager.markChunkDirty(cx - 1, cz);
    chunkManager.markChunkDirty(cx, cz + 1);
    chunkManager.markChunkDirty(cx, cz - 1);
  }

  /**
   * Handles a single block change sent by the server. * @param x Global X coordinate.
   *
   * @param y Global Y coordinate.
   * @param z Global Z coordinate.
   * @param blockId The new block ID.
   */
  public void onServerBlockUpdate(int x, int y, int z, short blockId) {
    // 1. Update the visual world (triggers mesh rebuild via ChunkManager)
    chunkManager.setBlockAt(x, y, z, blockId);

    // 2. Update the underlying data structure in the common World class
    setBlock(x, y, z, blockId);
  }
}
