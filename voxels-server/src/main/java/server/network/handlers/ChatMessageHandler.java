package server.network.handlers;

import common.network.packets.ChatMessagePacket;
import server.network.PlayerManager;
import server.network.ServerConnection;
import server.player.ServerPlayer;

public class ChatMessageHandler {

  private final ServerConnection connection;

  public ChatMessageHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(ChatMessagePacket packet) {
    ServerPlayer p = connection.getPlayer();
    if (p == null) return; // Only logged in player are allowed to chat

    String formattedMessage = "[" + p.getName() + "]: " + packet.getMessage();
    System.out.println("[CHAT] " + formattedMessage);

    // Broadcast all
    PlayerManager.broadcast(new ChatMessagePacket(formattedMessage));
  }
}
