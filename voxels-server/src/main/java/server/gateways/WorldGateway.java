package server.gateways;

import common.game.block.BlockType;

public interface WorldGateway {

  short getBlock(int x, int y, int z);

  void setBlock(int x, int y, int z, short id);
  
  BlockType getBlockType(int x, int y, int z);
}
