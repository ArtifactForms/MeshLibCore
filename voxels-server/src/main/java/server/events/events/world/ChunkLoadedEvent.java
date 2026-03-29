package server.events.events.world;

import common.world.ChunkData;

public class ChunkLoadedEvent extends WorldEvent {

  private final ChunkData data;

  public ChunkLoadedEvent(ChunkData data) {
    this.data = data;
  }

  public ChunkData getData() {
    return data;
  }
}
