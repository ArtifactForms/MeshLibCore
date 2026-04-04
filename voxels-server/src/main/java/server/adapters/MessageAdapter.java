package server.adapters;

import java.util.UUID;

import common.network.packets.ChatMessagePacket;
import server.gateways.MessageGateway;
import server.network.PlayerManager;

public class MessageAdapter implements MessageGateway {

  private final PlayerManager playerManager;
  private final String prefix;

  public MessageAdapter(PlayerManager playerManager, String prefix) {
    this.playerManager = playerManager;
    this.prefix = prefix;
  }

  private ChatMessagePacket createPacket(String message) {
    return new ChatMessagePacket(prefix + " " + message);
  }

  @Override
  public void sendMessage(UUID playerId, String message) {
    playerManager.send(playerId, createPacket(message));
  }

  @Override
  public void broadcastMessage(String message) {
    playerManager.broadcast(createPacket(message));
  }

  @Override
  public void sendCosoleMessage(String message) {
    System.out.println(message);
  }
}
