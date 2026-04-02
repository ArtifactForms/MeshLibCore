package client.usecases.connecttoserver;

import java.util.UUID;

import client.usecases.connecttoserver.ConnectToServer.ConnectToServerRequest;

public class ConnectToServerRequestModel implements ConnectToServerRequest {

  private String host;
  private int port;
  private String playerName;
  private UUID playerUuid;

  public ConnectToServerRequestModel(String host, int port, String playerName, UUID playerUuid) {
    this.host = host;
    this.port = port;
    this.playerName = playerName;
    this.playerUuid = playerUuid;
  }

  @Override
  public String getHost() {
    return host;
  }

  @Override
  public int getPort() {
    return port;
  }

  @Override
  public String getPlayerName() {
    return playerName;
  }

  @Override
  public UUID getPlayerUuid() {
    return playerUuid;
  }
}
