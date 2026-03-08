package server.world;

import common.world.ChunkData;
import common.world.World;
import server.network.GameServer;
import server.world.generation.BasicWorldGenerator;
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
    this.generator = new BasicWorldGenerator(seed);
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

  /**
   * Finds the highest non-air block at the given world coordinates. Useful for spawning entities or
   * determining terrain surface. * @param worldX Global X coordinate
   *
   * @param worldZ Global Z coordinate
   * @return The Y coordinate of the first empty space above the terrain
   */
  public int getHighestNonAirBlock(int worldX, int worldZ) {
    int cx = Math.floorDiv(worldX, 16);
    int cz = Math.floorDiv(worldZ, 16);

    ChunkData chunk = getOrCreateChunk(cx, cz);

    // Local coordinates within the chunk (0-15)
    int lx = Math.floorMod(worldX, 16);
    int lz = Math.floorMod(worldZ, 16);

    // Iterate downwards from the top of the world (assuming 256 height)
    for (int y = 255; y >= 0; y--) {
      if (chunk.getBlockId(lx, y, lz) != 0) { // 0 is AIR
        return y + 1; // Return the position ABOVE the solid block
      }
    }

    return 64; // Default fallback height if the chunk is empty
  }
}
