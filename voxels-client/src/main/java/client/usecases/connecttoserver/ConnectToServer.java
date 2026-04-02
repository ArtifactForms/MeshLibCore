package client.usecases.connecttoserver;

import java.util.UUID;

public interface ConnectToServer {

  void execute(ConnectToServerRequest request, ConnectToServerResponse response);

  interface ConnectToServerRequest {

    String getHost();

    int getPort();

    String getPlayerName();

    UUID getPlayerUuid();
  }

  interface ConnectToServerResponse {

    void onConnecting();

    void onConnected();

    void onError(String message);
  }
}
