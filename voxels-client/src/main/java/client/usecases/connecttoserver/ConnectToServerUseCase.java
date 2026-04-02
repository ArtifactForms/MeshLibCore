package client.usecases.connecttoserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.network.ClientNetwork;
import common.network.packets.PlayerJoinPacket;

public class ConnectToServerUseCase implements ConnectToServer {

  private ClientNetwork network;
  private ExecutorService executor = Executors.newSingleThreadExecutor();

  public ConnectToServerUseCase(ClientNetwork network) {
    this.network = network;
  }

  @Override
  public void execute(ConnectToServerRequest request, ConnectToServerResponse response) {
    response.onConnecting();

    executor.submit(
        () -> {
          try {
            network.connect(request.getHost(), request.getPort());
            network.send(new PlayerJoinPacket(request.getPlayerUuid(), request.getPlayerName()));

            response.onConnected();

          } catch (Exception e) {
            response.onError("Could not connect to the server.");
          }
        });
  }
}
