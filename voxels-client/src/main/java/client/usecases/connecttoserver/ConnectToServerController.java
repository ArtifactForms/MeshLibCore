package client.usecases.connecttoserver;

import java.util.UUID;

import client.app.GameClient;
import client.usecases.connecttoserver.ConnectToServer.ConnectToServerRequest;

public class ConnectToServerController {

  private GameClient client;
  private ConnectToServerView view;

  public ConnectToServerController(GameClient client, ConnectToServerView view) {
    this.client = client;
    this.view = view;
  }

  public void connect(String rawInput, String playerName) {
    ConnectToServerPresenter presenter = new ConnectToServerPresenter(view);

    if (playerName == null || playerName.isEmpty()) {
      presenter.onMissingPlayerName();
      return;
    }

    if (rawInput == null || rawInput.isEmpty()) {
      presenter.onMissingServerAddress();
      return;
    }

    int port;
    String host;

    try {
      String[] parts = rawInput.split(":");
      host = parts[0];
      port = (parts.length > 1) ? Integer.parseInt(parts[1]) : 25565;
    } catch (Exception e) {
      view.displayMessage(e.getMessage());
      return;
    }

    UUID uuid = client.getPlayer().getUuid();

    ConnectToServerRequest request = new ConnectToServerRequestModel(host, port, playerName, uuid);

    ConnectToServerUseCase useCase = new ConnectToServerUseCase(client.getNetwork());

    useCase.execute(request, presenter);
  }
}
