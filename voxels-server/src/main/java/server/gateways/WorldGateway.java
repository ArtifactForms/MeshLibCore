package server.gateways;

import common.game.block.BlockType;

public interface WorldGateway {

  short getBlock(int x, int y, int z);

  void setBlock(int x, int y, int z, short id);

  BlockType getBlockType(int x, int y, int z);

  public long getWorldTime();

  void setWorldTime(long time);

  void setTimeOfDay(long timeOfDay);

  long getSeed();

  int getHeightAt(int x, int z);
  
  long getDay();
}
