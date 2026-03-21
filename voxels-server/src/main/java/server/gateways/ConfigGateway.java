package server.gateways;

public interface ConfigGateway {

  int getMaxPlayers();
	
  int getMaxChatMessageLength();
  
  String getChatFormat();
  
  String getMessageOfTheDay();
}
