package server.usecases.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import common.logging.Log;
import common.network.packets.ChatMessagePacket;
import server.commands.Command;
import server.commands.CommandContext;
import server.commands.CommandMessages;
import server.events.events.PlayerChatEvent;
import server.gateways.CommandGateway;
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
  private final CommandGateway commands;

  public ChatMessageHandler(ServerConnection connection, GatewayContext context) {
    this.connection = connection;
    this.permissions = context.permissions();
    this.events = context.events();
    this.config = context.config();
    this.commands = context.commands();
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
      handleCommand(playerId, message);
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

  private void handleCommand(UUID playerId, String message) {
    // -------------------------------------
    // GENERAL COMMAND PERMISSION CHECK
    // -------------------------------------
    if (!permissions.hasPermission(playerId, Permissions.COMMANDS)) {
      connection.send(new ChatMessagePacket(ChatMessageMessages.NO_COMMAND_PERMISSION));
      return;
    }

    // -------------------------------------
    // PROCESS COMMAND
    // -------------------------------------
    String rawInput = message.substring(1).trim();

    if (rawInput.isEmpty()) {
      Log.debug("[COMMAND] player=" + playerId + " rejected=empty_command");
      return;
    }

    String[] parts = rawInput.split("\\s+");
    String commandName = parts[0].toLowerCase();

    List<String> args = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));

    Command command = commands.getCommand(commandName);

    if (command == null) {
      connection.send(new ChatMessagePacket(CommandMessages.UNKNOWN_COMMAND));
      Log.debug("[COMMAND] player=" + playerId + " unknown=\"" + commandName + "\"");
      return;
    }

    // -------------------------------------
    // SPECIFIC COMMAND PERMISSION CHECK
    // -------------------------------------
    if (!hasPermission(playerId, command.getPermission())) {
      connection.send(new ChatMessagePacket(CommandMessages.NO_PERMISSION));
      return;
    }

    // -------------------------------------
    // COMMAND EXECUTION
    // -------------------------------------
    CommandContext context = new CommandContext(playerId, args, connection.getServer());
    command.execute(context);
    Log.info("[COMMAND] player=" + playerId + " executed=" + commandName);
  }
}
