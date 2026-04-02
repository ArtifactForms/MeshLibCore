package server.usecases.chat;

import java.util.UUID;

import common.logging.Log;
import common.network.packets.ChatMessagePacket;
import server.events.events.PlayerChatEvent;
import server.gateways.ConfigGateway;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.gateways.PermissionGateway;
import server.network.ServerConnection;
import server.permissions.Permissions;
import server.player.ServerPlayer;

public class ChatMessageHandler {

  private final ServerConnection connection;

  private final PermissionGateway permissions;
  private final EventGateway events;
  private final ConfigGateway config;

  public ChatMessageHandler(ServerConnection connection, GatewayContext context) {
    this.connection = connection;
    this.permissions = context.permissions();
    this.events = context.events();
    this.config = context.config();
  }

  private boolean hasPermission(UUID uuid, String permission) {
    return permissions.hasPermission(uuid, permission);
  }

  private boolean isValid(String message) {
    if (message == null || message.isBlank()) return false;

    return true;
  }

  public void handle(ChatMessagePacket packet) {
    ServerPlayer player = connection.getPlayer();

    if (player == null) {
      //      Log.warn("[CHAT] null_player connection=" + connection.getId());
      return;
    }

    UUID playerId = player.getUuid();

    // -------------------------------------
    // PERMISSION CHECK
    // -------------------------------------
    if (!hasPermission(playerId, Permissions.CHAT)) {
      connection.send(new ChatMessagePacket(ChatMessageMessages.NO_CHAT_PERMISSION));
      Log.warn("[CHAT] player=" + player.getName() + " blocked=no_permission");
      return;
    }

    // -------------------------------------
    // VALIDATE MESSAGE
    // -------------------------------------
    String message = packet.getMessage();
    if (!isValid(message)) {
      Log.debug("[CHAT] player=" + player.getName() + " rejected=invalid_message");
      return;
    }

    // -------------------------------------
    // VALIDATE MESSAGE LENGTH
    // -------------------------------------
    int maxMessageLength = config.getMaxChatMessageLength();
    if (message.length() > maxMessageLength) {
      connection.send(new ChatMessagePacket(ChatMessageMessages.MAX_MESSAGE_LENGTH));
      Log.debug(
          "[CHAT] player=" + player.getName() + " rejected=too_long length=" + message.length());
      return;
    }

    // -------------------------------------
    // COMMAND CHECK
    // -------------------------------------
    if (message.startsWith("/")) {
      connection.getServer().dispatchCommand(playerId, message);
      return;
    }

    Log.debug(
        "[CHAT] player=" + player.getName() + " uuid=" + playerId + " raw=\"" + message + "\"");

    // -------------------------------------
    // EVENT
    // -------------------------------------
    PlayerChatEvent event = new PlayerChatEvent(playerId, message);
    events.fire(event);

    if (event.isCancelled()) {
      Log.info("[CHAT] player=" + player.getName() + " cancelled=true message=\"" + message + "\"");
      return;
    }

    String finalMessage = event.getMessage();
    if (!finalMessage.equals(message)) {
      Log.debug(
          "[CHAT] player="
              + player.getName()
              + " modified=\""
              + message
              + "\" -> \""
              + finalMessage
              + "\"");
    }

    // -------------------------------------
    // RESPONSE BROADCAST
    // -------------------------------------
    String formattedMessage = ChatFormatter.format(config, player, finalMessage);
    connection.getServer().getPlayerManager().broadcast(new ChatMessagePacket(formattedMessage));

    Log.info("[CHAT] broadcast player=" + player.getName() + " message=\"" + finalMessage + "\"");
  }
}
