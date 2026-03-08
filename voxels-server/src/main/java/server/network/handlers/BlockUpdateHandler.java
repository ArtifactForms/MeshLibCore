package server.network.handlers;

import common.network.packets.BlockUpdatePacket;
import server.network.ServerConnection;

public class BlockUpdateHandler {

  private final ServerConnection connection;

  public BlockUpdateHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(BlockUpdatePacket packet) {
    // TODO
  }
}
