package server.network.handlers;

import java.util.ArrayList;
import java.util.List;

import common.logging.Log;
import common.network.packets.ChatMessagePacket;
import server.commands.Command;
import server.commands.CommandContext;
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
    String rawInput = message.substring(1).trim();
    if (rawInput.isEmpty()) return;

    String[] parts = rawInput.split("\\s+");
    String commandName = parts[0];

    List<String> args = new ArrayList<>();
    for (int i = 1; i < parts.length; i++) {
      args.add(parts[i]);
    }

    Command command = connection.getServer().getCommandRegistry().get(commandName);

    if (command != null) {
      CommandContext context = new CommandContext(player.getUuid(), args, connection.getServer());
      command.execute(context);
    } else {
      player.getConnection().send(new ChatMessagePacket("Unknown command: " + commandName));
    }
  }
}
