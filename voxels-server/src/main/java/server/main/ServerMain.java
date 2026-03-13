package server.main;

import common.game.ItemRegistry;
import common.logging.ConsoleLogger;
import common.logging.Log;
import common.network.NetworkPackets;
import server.network.GameServer;

public class ServerMain {

  public static void main(String[] args) throws Exception {
    Log.setImplementation(new ConsoleLogger("SERVER"));
    int port = 25565;

    NetworkPackets.register();

    GameServer server = new GameServer(port);
    ItemRegistry.init();
    server.start();
  }
}
