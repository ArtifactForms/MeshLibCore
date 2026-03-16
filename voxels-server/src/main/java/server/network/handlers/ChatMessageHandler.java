package server.network.handlers;

import java.util.List;

import common.logging.Log;
import common.network.packets.ChatMessagePacket;
import server.commands.Command;
import server.commands.CommandContext;
import server.commands.CommandMessages;
import server.network.GameServer;
import server.network.ServerConnection;
import server.player.ServerPlayer;

public class ChatMessageHandler {

  private final ServerConnection connection;

  public ChatMessageHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(ChatMessagePacket packet) {
    ServerPlayer player = connection.getPlayer();
    if (player == null) return;

    String message = packet.getMessage();

    if (message.startsWith("/")) {
      handleCommand(player, message);
      return;
    }

    String formattedMessage = "[" + player.getName() + "]: " + message;
    Log.info("[CHAT] " + formattedMessage);

    connection.getServer().getPlayerManager().broadcast(new ChatMessagePacket(formattedMessage));
  }

  private void handleCommand(ServerPlayer player, String message) {

    GameServer server = connection.getServer();

    String rawInput = message.substring(1).trim();
    if (rawInput.isEmpty()) return;

    String[] parts = rawInput.split("\\s+");
    String commandName = parts[0].toLowerCase();

    List<String> args = List.of(parts).subList(1, parts.length);

    Command command = server.getCommandRegistry().get(commandName);

    if (command == null) {
      player.sendMessage(CommandMessages.unknownCommand);
      return;
    }

    if (!server.hasPermission(player.getUuid(), command.getPermission())) {
      player.sendMessage(CommandMessages.noPermsission);
      return;
    }

    CommandContext context = new CommandContext(player.getUuid(), args, server);
    command.execute(context);
  }
}
