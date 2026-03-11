package server.world.structures;

import common.game.block.BlockType;

public interface WorldAccess {

  void setBlock(int wx, int wy, int wz, BlockType type);

  BlockType getBlock(int wx, int wy, int wz);

  int getHeight(int wx, int wz);
}
