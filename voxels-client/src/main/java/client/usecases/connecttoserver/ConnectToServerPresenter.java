package client.usecases.connecttoserver;

import client.usecases.connecttoserver.ConnectToServer.ConnectToServerResponse;

public class ConnectToServerPresenter implements ConnectToServerResponse {

  private ConnectToServerView view;

  public ConnectToServerPresenter(ConnectToServerView view) {
    this.view = view;
  }

  public void onMissingPlayerName() {
    view.displayMessage("Please enter a player name to continue.");
  }

  public void onMissingServerAddress() {
    view.displayMessage("Please enter a server address to continue.");
  }

  @Override
  public void onConnecting() {
    view.displayMessage("Connecting...");
  }

  @Override
  public void onConnected() {
    view.displayMessage("Connected!");
    view.displayGameScene();
  }

  @Override
  public void onError(String message) {
    view.displayMessage(message);
  }
}
