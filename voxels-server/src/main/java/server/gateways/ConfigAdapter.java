package server.gateways;

import server.config.ServerConfig;

public class ConfigAdapter implements ConfigGateway {

  private ServerConfig config;

  public ConfigAdapter(ServerConfig config) {
    this.config = config;
  }

  @Override
  public int getMaxPlayers() {
    return config.getMaxPlayers();
  }

  @Override
  public int getMaxChatMessageLength() {
    return config.getMaxChatMessageLength();
  }

  @Override
  public String getChatFormat() {
    return config.getChatFormat();
  }
}
