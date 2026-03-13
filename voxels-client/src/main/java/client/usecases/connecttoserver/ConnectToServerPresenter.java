package client.usecases.connecttoserver;

import client.usecases.connecttoserver.ConnectToServer.ConnectToServerResponse;

public class ConnectToServerPresenter implements ConnectToServerResponse {

  @Override
  public void onConnecting() {
    System.out.println("Connecting...");
  }

  @Override
  public void onConnected() {
    System.out.println("Connected!");
  }

  @Override
  public void onError(String message) {
    System.out.println("Connection failed: " + message);
  }
}
