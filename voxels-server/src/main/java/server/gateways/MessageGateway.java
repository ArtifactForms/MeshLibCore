package server.gateways;

import java.util.UUID;

public interface MessageGateway {

  void sendMessage(UUID playerId, String message);

  void broadcastMessage(String message);
  
  void sendCosoleMessage(String message);
}
