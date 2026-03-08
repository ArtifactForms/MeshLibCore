package server.network.handlers;

import common.network.packets.PlayerSpawnPacket;
import server.network.ServerConnection;

public class PlayerSpawnHandler {

  private final ServerConnection connection;

  public PlayerSpawnHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(PlayerSpawnPacket packet) {
    // TODO
  }
}
