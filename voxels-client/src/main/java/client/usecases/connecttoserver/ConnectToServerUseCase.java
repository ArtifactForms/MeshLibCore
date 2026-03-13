package client.usecases.connecttoserver;

import client.network.ClientNetwork;
import common.network.packets.PlayerJoinPacket;

public class ConnectToServerUseCase implements ConnectToServer {

  private ClientNetwork network;

  public ConnectToServerUseCase(ClientNetwork network) {
    this.network = network;
  }

  @Override
  public void execute(ConnectToServerRequest request, ConnectToServerResponse response) {
    try {
      network.connect(request.getHost());
      network.send(new PlayerJoinPacket(request.getPlayerUuid(), request.getPlayerName()));
    } catch (Exception e) {
      response.onError(e.getMessage());
    }
  }
}
