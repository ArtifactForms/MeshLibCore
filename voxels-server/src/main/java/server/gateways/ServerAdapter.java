package server.gateways;

import server.network.GameServer;

public class ServerAdapter implements ServerGateway {

  private final GameServer server;

  public ServerAdapter(GameServer server) {
    this.server = server;
  }

  @Override
  public void shutdown() {
    server.shutdown();
  }
}
