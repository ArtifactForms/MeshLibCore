package common.world;

import common.game.block.Blocks;

class ReferenceChunk {

  private final short[][][] blocks = new short[ChunkData.WIDTH][ChunkData.HEIGHT][ChunkData.DEPTH];

  void set(short id, int x, int y, int z) {
    blocks[x][y][z] = id;
  }

  short get(int x, int y, int z) {
    return blocks[x][y][z];
  }

  int height(int x, int z) {

    for (int y = ChunkData.HEIGHT - 1; y >= 0; y--) {
      if (blocks[x][y][z] != Blocks.AIR.getId()) {
        return y;
      }
    }

    return 0;
  }
}
