package server.gateways;

public interface WorldGateway {

  short getBlock(int x, int y, int z);

  void setBlock(int x, int y, int z, short id);
}
