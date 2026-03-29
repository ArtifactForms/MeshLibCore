package server.world;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import common.logging.Log;
import common.world.ChunkData;
import common.world.World;
import server.events.events.world.ChunkLoadedEvent;
import server.events.events.world.WorldSavedEvent;
import server.events.events.world.WorldTimeChangedEevent;
import server.gateways.EventGateway;
import server.persistance.ChunkRepository;
import server.world.generation.WorldGenerator;

/**
 * Server-side representation of the game world. Manages chunk generation, block updates, and world
 * queries.
 */
public class ServerWorld extends World {

  private final ChunkRepository repository;
  private final WorldGenerator generator;
  private final EventGateway events;

  public ServerWorld(WorldGenerator generator, ChunkRepository repository, EventGateway events) {
    this.generator = generator;
    this.repository = repository;
    this.events = events;
  }

  public void saveDirtyChunks() {
    int savedChunksCount = 0;
    for (ChunkData chunk : chunks.values()) {
      if (chunk.isDirty()) {
        repository.save(chunk);
        chunk.setDirty(false);
        savedChunksCount++;
      }
    }

    if (savedChunksCount > 0) {
      Log.info("Saved " + savedChunksCount + " changed chunks.");
    }

    events.fire(new WorldSavedEvent(savedChunksCount));
  }

  public void unloadUnusedChunks(Set<Long> requiredByPlayers) {
    int savedThisTick = 0;
    int maxSavesPerCleanup = 10; // Nur 10 Dateien pro Cleanup schreiben!

    Iterator<Map.Entry<Long, ChunkData>> it = chunks.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<Long, ChunkData> entry = it.next();
      if (!requiredByPlayers.contains(entry.getKey())) {
        ChunkData data = entry.getValue();
        //        if (data.isDirty() && savedThisTick < maxSavesPerCleanup) {
        //          repository.save(data);
        //          data.setDirty(false);
        //          savedThisTick++;
        //        }

        // Wenn er dirty ist, aber wir das Limit erreicht haben,
        // behalten wir ihn lieber noch 10 Sek im RAM statt zu laggen.
        //        if (!data.isDirty()) {
        it.remove();
        //        }
      }
    }
  }

  /**
   * Retrieves an existing chunk or generates a new one if it doesn't exist. * @param cx Chunk X
   * coordinate
   *
   * @param cz Chunk Z coordinate
   * @return The ChunkData for the specified coordinates
   */
  public ChunkData getOrCreateChunk(int cx, int cz) {
    long key = getChunkKey(cx, cz);
    ChunkData data = chunks.get(key);

    if (data == null) {
      data =
          repository
              .load(cx, cz)
              .orElseGet(
                  () -> {
                    ChunkData newChunk = new ChunkData(cx, cz);
                    generator.generate(newChunk);
                    return newChunk;
                  });

      addChunk(data);

      events.fire(new ChunkLoadedEvent(data));
    }

    return data;
  }

  public long getSeed() {
    return generator.getSeed();
  }

  @Override
  public void setWorldTime(long time) {
    super.setWorldTime(time);
    events.fire(new WorldTimeChangedEevent(time));
  }
}
