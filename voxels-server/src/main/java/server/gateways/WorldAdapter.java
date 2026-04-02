package server.gateways;

import common.game.block.BlockType;
import server.world.ServerWorld;

public class WorldAdapter implements WorldGateway {

  private ServerWorld world;

  public WorldAdapter(ServerWorld world) {
    this.world = world;
  }

  @Override
  public short getBlock(int x, int y, int z) {
    return world.getBlock(x, y, z).getId();
  }

  @Override
  public void setBlock(int x, int y, int z, short id) {
    world.setBlock(x, y, z, id);
  }

  @Override
  public BlockType getBlockType(int x, int y, int z) {
    return world.getBlock(x, y, z);
  }

  @Override
  public long getWorldTime() {
    return world.getWorldTime();
  }

  @Override
  public void setWorldTime(long time) {
    world.setWorldTime(time);
  }

  @Override
  public long getSeed() {
    return world.getSeed();
  }

  @Override
  public int getHeightAt(int x, int z) {
    return world.getHeightAt(x, z);
  }

  @Override
  public void setTimeOfDay(long timeOfDay) {
    world.setTimeOfDay(timeOfDay);
  }

  @Override
  public long getDay() {
    return world.getDay();
  }
}
