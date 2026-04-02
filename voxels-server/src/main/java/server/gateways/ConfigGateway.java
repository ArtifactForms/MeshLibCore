package server.gateways;

public interface ConfigGateway {

  int getPort();

  int getMaxPlayers();

  int getMaxChatMessageLength();

  String getMotd();

  String getChatFormat();

  int getViewDistance();
  
  String getCommandMessagePrefix();
}
