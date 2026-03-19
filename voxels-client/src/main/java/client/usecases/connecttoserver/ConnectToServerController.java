package client.usecases.connecttoserver;

import java.util.UUID;

import client.app.GameClient;
import client.usecases.connecttoserver.ConnectToServer.ConnectToServerRequest;

public class ConnectToServerController {

  private GameClient client;

  public ConnectToServerController(GameClient client) {
    this.client = client;
  }

  public void connect() {

    String host = "localhost";
    String playerName = "Player";
    UUID uuid = UUID.randomUUID();

    ConnectToServerRequest request = new ConnectToServerRequestModel(host, playerName, uuid);
    ConnectToServerPresenter presenter = new ConnectToServerPresenter();
    ConnectToServerUseCase useCase = new ConnectToServerUseCase(client.getNetwork());

    useCase.execute(request, presenter);
  }
}
