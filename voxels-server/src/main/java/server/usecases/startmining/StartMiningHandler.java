package server.usecases.startmining;

import java.util.UUID;

import common.network.packets.StartMiningPacket;
import server.network.ServerConnection;
import server.player.ServerPlayer;
import server.usecases.startmining.StartMining.StartMiningRequest;

public class StartMiningHandler {

  private final ServerConnection connection;

  public StartMiningHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(StartMiningPacket packet) {
    ServerPlayer player = connection.getPlayer();
    if (player == null) {
      // TODO Log
      return;
    }
    UUID playerId = player.getUuid();
    StartMiningRequest request = createRequest(playerId, packet);
  }

  private StartMiningRequest createRequest(UUID playerId, StartMiningPacket packet) {
    return new StartMiningRequestModel(
        playerId,
        packet.getX(),
        packet.getY(),
        packet.getZ(),
        packet.getSelectedSlot(),
        packet.getFace(),
        packet.getPitch(),
        packet.getYaw());
  }
}
