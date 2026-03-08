package server.main;

import common.game.ItemRegistry;
import common.network.NetworkPackets;
import server.network.GameServer;
import server.world.ServerWorld;

public class ServerMain {

  public static void main(String[] args) throws Exception {

    int port = 25565;

    NetworkPackets.register();

    GameServer server = new GameServer(port);
    ItemRegistry.init();
    ServerWorld world = new ServerWorld(server);
    GameServer.setWorld(world);
    server.start();
  }
}
