package server.gateways;

public class ConfigAdapter implements ConfigGateway {

  @Override
  public int getMaxChatMessageLength() {
    return 256;
  }

  @Override
  public String getChatFormat() {
    return "{prefix}{name}: {message}";
  }
}
