package server.events.events.world;

public class WorldSavedEvent extends WorldEvent {

  private final int savedChunksCount;

  public WorldSavedEvent(int savedChunksCount) {
    this.savedChunksCount = savedChunksCount;
  }

  public int getSavedChunksCount() {
    return savedChunksCount;
  }
}
