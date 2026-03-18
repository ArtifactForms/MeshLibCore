package server.gateways;

public class ConfigAdapter implements ConfigGateway {

  @Override
  public int getMaxChatMessageLength() {
	  return 256;
  }
}
