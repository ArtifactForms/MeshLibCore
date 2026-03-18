package server.main;

import common.game.ItemRegistry;
import common.game.block.Blocks;
import common.logging.ConsoleLogger;
import common.logging.Log;
import common.network.NetworkPackets;
import server.config.ServerConfig;
import server.network.GameServer;

public class ServerMain {

  public static void main(String[] args) throws Exception {

    Log.setImplementation(new ConsoleLogger("SERVER"));
    ServerConfig config = new ServerConfig();
    config.load();

    int port = config.getPort();

    NetworkPackets.register();

    Blocks.initialize(); // Important: Initialize before item registration -> ItemRegistry.init();
    ItemRegistry.init();

    GameServer server = new GameServer(port);
    server.start();
  }
}
