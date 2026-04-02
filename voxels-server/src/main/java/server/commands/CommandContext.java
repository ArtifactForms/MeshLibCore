package server.commands;

import java.util.List;
import java.util.UUID;

import common.network.packets.ChatMessagePacket;
import server.gateways.GatewayContext;
import server.network.GameServer;
import server.usecases.UseCaseRegistry;

public class CommandContext {

  private final UUID player;
  private final String prefix;
  private final List<String> args;
  private final GameServer server;
  private final GatewayContext gatewayContext;

  public CommandContext(
      UUID player, List<String> args, GameServer server, GatewayContext gatewayContext) {
    this.player = player;
    this.prefix = gatewayContext.config().getCommandMessagePrefix();
    this.args = args;
    this.server = server;
    this.gatewayContext = gatewayContext;
  }

  public void reply(String message) {
    if (isConsole()) {
      System.out.println("[Command] " + message);
      return;
    }
    server.getPlayerManager().send(player, new ChatMessagePacket(prefix + " " + message));
  }

  public void broadcast(String message) {
    server.getPlayerManager().broadcast(new ChatMessagePacket(prefix + " " + message));
  }

  public UUID getPlayer() {
    return player;
  }

  public List<String> getArgs() {
    return args;
  }

  public GameServer getServer() {
    return server;
  }

  public boolean isConsole() {
    return this.player == null;
  }

  public boolean hasPermission(String permission) {
    return gatewayContext.permissions().hasPermission(player, permission);
  }

  public UseCaseRegistry getUseCases() {
    return server.getUseCases();
  }
}
