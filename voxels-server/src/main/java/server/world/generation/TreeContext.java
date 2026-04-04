package server.world.generation;

import java.util.Random;

import common.world.ChunkData;

public class TreeContext {

  public final ChunkData chunk;

  public final int x;

  public final int y;

  public final int z;

  public final Random rng;

  public TreeContext(ChunkData chunk, int x, int y, int z, Random rng) {
    this.chunk = chunk;
    this.x = x;
    this.y = y;
    this.z = z;
    this.rng = rng;
  }
}
