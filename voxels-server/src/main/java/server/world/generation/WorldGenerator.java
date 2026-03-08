package server.world.generation;

import common.world.ChunkData;

public interface WorldGenerator {

  public void generate(ChunkData chunk);
}
