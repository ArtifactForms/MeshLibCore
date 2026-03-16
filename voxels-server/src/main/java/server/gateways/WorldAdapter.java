package server.gateways;

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
}
