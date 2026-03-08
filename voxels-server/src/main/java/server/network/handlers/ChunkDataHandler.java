package server.network.handlers;

import common.network.packets.ChunkDataPacket;
import server.network.ServerConnection;

public class ChunkDataHandler {

  private ServerConnection connection;

  public ChunkDataHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(ChunkDataPacket packet) {
    // TODO
  }
}
