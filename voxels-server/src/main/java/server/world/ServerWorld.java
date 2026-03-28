package server.world;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import common.logging.Log;
import common.network.packets.ChatMessagePacket;
import common.network.packets.TimeUpdatePacket;
import common.world.ChunkData;
import common.world.World;
import server.network.GameServer;
import server.persistance.ChunkRepository;
import server.world.generation.BasicWorldGenerator2;
import server.world.generation.WorldGenerator;

/**
 * Server-side representation of the game world. Manages chunk generation, block updates, and world
 * queries.
 */
public class ServerWorld extends World {

  private final long seed;
  private final GameServer gameServer;
  private final ChunkRepository repository;
  private WorldGenerator generator;

  public ServerWorld(GameServer gameServer, ChunkRepository repository) {
    // TODO Default seed set to 0; consider passing a dynamic seed in the future
    seed = 0;
    this.gameServer = gameServer;
    this.repository = repository;
    this.generator = new BasicWorldGenerator2(seed);
//    this.generator = new BasicWorldGenerator4(seed);

    //        this.generator = new BasicWorldGenerator(seed);
    //        this.generator = new FlatWorldGenerator("1*bedrock,4*dirt,1*grass_block");
  }

  @Override
  public void tick() {
    super.tick();

    if (gameServer.getTick() % 40 == 0) {
      //      gameServer.getPlayerManager().broadcast(new ChatMessagePacket("[Server-World]
      // chunks-size: " + chunks.size()));
    }

    //    if (gameServer.getTick() % 3000 == 0) {
    //      saveDirtyChunks();
    //    }
  }

  public void saveDirtyChunks() {
    for (ChunkData chunk : chunks.values()) {
      if (chunk.isDirty()) {
        repository.save(chunk);
        chunk.setDirty(false); // Reset
        Log.info("Saved changed chunk: " + chunk.getChunkX() + "," + chunk.getChunkZ());
      }
    }
    gameServer.getPlayerManager().broadcast(new ChatMessagePacket("World saved."));
  }

  @Override
  public void setWorldTime(long time) {
    super.setWorldTime(time);
    gameServer.getPlayerManager().broadcast(new TimeUpdatePacket(time));
  }

  //  public void unloadUnusedChunks(java.util.Set<Long> requiredByPlayers) {
  //    Log.info("Loaded chunks: " + chunks.size());
  //    chunks
  //        .entrySet()
  //        .removeIf(
  //            entry -> {
  //              long key = entry.getKey();
  //              ChunkData data = entry.getValue();
  //              if (!requiredByPlayers.contains(key)) {
  //                if (data.isDirty()) {
  //                  repository.save(data);
  //                  data.setDirty(false);
  //                }
  //                return true;
  //              }
  //              return false;
  //            });
  //    Log.info("Chunks after Unload: " + chunks.size());
  //  }

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

  @Override
  public void setBlock(int x, int y, int z, short blockId) {

    if (y < 0 || y >= ChunkData.HEIGHT) return;

    int cx = Math.floorDiv(x, ChunkData.WIDTH);
    int cz = Math.floorDiv(z, ChunkData.DEPTH);

    ChunkData chunk = getOrCreateChunk(cx, cz);

    int lx = Math.floorMod(x, ChunkData.WIDTH);
    int lz = Math.floorMod(z, ChunkData.DEPTH);

    chunk.setBlockId(blockId, lx, y, lz);
    // Later
    // TODO gameServer.broadcast(new BlockUpdatePacket(x, y, z, blockId));
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
                    // Initial save
                    //                    repository.save(newChunk); // TODO Async!!!
                    return newChunk;
                  });

      addChunk(data);
    }
    return data;
  }

  public long getSeed() {
    return seed;
  }
}
