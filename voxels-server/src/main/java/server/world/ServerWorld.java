package server.world;

import common.world.ChunkData;
import common.world.World;
import server.network.GameServer;
import server.world.generation.BasicWorldGenerator2;
import server.world.generation.WorldGenerator;

/**
 * Server-side representation of the game world. Manages chunk generation, block updates, and world
 * queries.
 */
public class ServerWorld extends World {

  private final GameServer gameServer;
  private WorldGenerator generator;

  public ServerWorld(GameServer gameServer) {
    this.gameServer = gameServer;
    // Default seed set to 0; consider passing a dynamic seed in the future
    long seed = 0;
    //    this.generator = new BasicWorldGenerator(seed);
    this.generator = new BasicWorldGenerator2(seed);
    //    this.generator = new FlatWorldGenerator("1*bedrock,4*dirt,1*grass_block");
  }

  /**
   * Updates a block in the world and should eventually broadcast this change to all connected
   * clients.
   */
  @Override
  public void setBlock(int x, int y, int z, short blockId) {
    // 1. Update the underlying data in the server's world storage
    super.setBlock(x, y, z, blockId);

    // TODO: Broadcast the change to all clients via the network
    // gameServer.broadcast(new BlockUpdatePacket(x, y, z, blockId));
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
      data = new ChunkData(cx, cz);
      generator.generate(data);
      addChunk(data);
    }
    return data;
  }
}
