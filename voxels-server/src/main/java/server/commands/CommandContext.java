package server.commands;

import java.util.List;
import java.util.UUID;

import server.network.GameServer;

public class CommandContext {

  private UUID player;
  private List<String> args;
  private GameServer server;

  public CommandContext(UUID player, List<String> args, GameServer server) {
    this.player = player;
    this.args = args;
    this.server = server;
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
}
