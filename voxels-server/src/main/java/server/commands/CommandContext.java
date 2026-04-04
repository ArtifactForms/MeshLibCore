package server.commands;

import java.util.List;
import java.util.UUID;

import server.gateways.MessageGateway;
import server.gateways.PermissionGateway;
import server.network.GameServer;
import server.usecases.UseCaseRegistry;

public class CommandContext {

  private final UUID player;

  private final List<String> args;

  private final GameServer server;

  private final PermissionGateway permissions;

  private final MessageGateway messages;

  public CommandContext(
      UUID player,
      List<String> args,
      GameServer server,
      PermissionGateway permissions,
      MessageGateway messages) {
    this.player = player;
    this.args = args;
    this.server = server;
    this.permissions = permissions;
    this.messages = messages;
  }

  public void reply(String message) {
    if (isConsole()) {
      messages.sendCosoleMessage("[Command] " + message);
      return;
    }
    messages.sendMessage(player, message);
  }

  public void broadcast(String message) {
    messages.broadcastMessage(message);
  }

  public UUID getPlayer() {
    return player;
  }

  public List<String> getArgs() {
    return args;
  }

  @Deprecated
  public GameServer getServer() {
    return server;
  }

  public boolean isConsole() {
    return this.player == null;
  }

  public boolean hasPermission(String permission) {
    return permissions.hasPermission(player, permission);
  }

  public UseCaseRegistry getUseCases() {
    return server.getUseCases();
  }
}
