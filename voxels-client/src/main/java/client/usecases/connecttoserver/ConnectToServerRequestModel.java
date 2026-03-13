package client.usecases.connecttoserver;

import java.util.UUID;

import client.usecases.connecttoserver.ConnectToServer.ConnectToServerRequest;

public class ConnectToServerRequestModel implements ConnectToServerRequest {

  private String host;
  private String playerName;
  private UUID playerUuid;

  public ConnectToServerRequestModel(String host, String playerName, UUID playerUuid) {
    this.host = host;
    this.playerName = playerName;
    this.playerUuid = playerUuid;
  }

  @Override
  public String getHost() {
    return host;
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
