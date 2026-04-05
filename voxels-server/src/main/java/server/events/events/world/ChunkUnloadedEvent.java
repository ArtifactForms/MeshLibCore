package server.events.events.world;

import common.world.ChunkData;

public class ChunkUnloadedEvent extends WorldEvent {

  private final ChunkData data;
  
  private final int loadedChunksCount;

  public ChunkUnloadedEvent(ChunkData data, int loadedChunksCount) {
    this.data = data;
    this.loadedChunksCount = loadedChunksCount;
  }

  public ChunkData getData() {
    return data;
  }

  public int getLoadedChunksCount() {
    return loadedChunksCount;
  }
}
