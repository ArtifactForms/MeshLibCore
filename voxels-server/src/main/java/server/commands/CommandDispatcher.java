package server.commands;

import java.util.List;
import java.util.UUID;

import common.logging.Log;
import common.network.packets.ChatMessagePacket;
import server.gateways.CommandGateway;
import server.gateways.GatewayContext;
import server.gateways.PermissionGateway;
import server.network.GameServer;
import server.permissions.Permissions;

public class CommandDispatcher {

  private final GameServer server;
  private final GatewayContext context;
  private final PermissionGateway permissions;
  private final CommandGateway commands;

  public CommandDispatcher(GameServer server, GatewayContext context) {
    this.server = server;
    this.context = context;
    this.permissions = context.permissions();
    this.commands = context.commands();
  }

  public void dispatchCommand(UUID playerId, String input) {
    input = translateColors(input);

    if (input.startsWith("/")) {
      input = input.substring(1);
    }

    input = input.trim();

    if (input.isEmpty()) {
      Log.debug("[COMMAND] rejected=empty_command");
      return;
    }

    // -------------------------------------
    // SPLIT
    // -------------------------------------
    String[] parts = input.split("\\s+");
    String commandName = parts[0].toLowerCase();

    List<String> args = java.util.Arrays.asList(parts).subList(1, parts.length);

    Command command = commands.getCommand(commandName);

    if (command == null) {
      if (playerId == null) {
        Log.warn("Unknown command: " + commandName);
      } else {
        sendMessage(playerId, CommandMessages.UNKNOWN_COMMAND);
      }
      return;
    }

    // -------------------------------------
    // PERMISSION CHECK
    // -------------------------------------
    if (playerId != null && !hasPermission(playerId, Permissions.COMMANDS)) {
      sendMessage(playerId, CommandMessages.NO_COMMAND_PERMISSION);
      return;
    }

    if (playerId != null && !hasPermission(playerId, command.getPermission())) {
      sendMessage(playerId, CommandMessages.NO_PERMISSION);
      return;
    }

    // -------------------------------------
    // HELP
    // -------------------------------------
    if (!args.isEmpty() && args.get(0).equalsIgnoreCase("help")) {
      if (playerId != null) {
        sendMessage(playerId, command.getUsage());
      } else {
        Log.info(command.getUsage());
      }
      return;
    }

    // -------------------------------------
    // EXECUTE
    // -------------------------------------
    Log.info("[COMMAND] " + (playerId == null ? "console" : playerId) + " executed=" + commandName);
    CommandContext ctx = new CommandContext(playerId, args, server, context);
    command.execute(ctx);
  }

  private boolean hasPermission(UUID playerId, String permission) {
    return permissions.hasPermission(playerId, permission);
  }

  private void sendMessage(UUID playerId, String message) {
    server.getPlayerManager().send(playerId, new ChatMessagePacket(message));
  }

  private String translateColors(String input) {
    return input.replace("&", "§");
  }
}
