package client.world;

import common.world.World;

public class ClientWorld extends World {

  private final ChunkManager chunkManager;

  public ClientWorld(ChunkManager chunkManager) {
    this.chunkManager = chunkManager;
  }

  public void applyChunkData(int cx, int cz, short[] blocks) {
    Chunk chunk = chunkManager.getOrCreateChunk(cx, cz);

    System.arraycopy(blocks, 0, chunk.getRawBlockData(), 0, blocks.length);

    chunk.setDataReady();
    chunk.markDirty();
    addChunk(chunk);

    chunkManager.notifyNeighborsOfDataReady(cx, cz);
  }

  public void onServerBlockUpdate(int x, int y, int z, short blockId) {
    chunkManager.setBlockAt(x, y, z, blockId);
    setBlock(x, y, z, blockId);
  }
}
