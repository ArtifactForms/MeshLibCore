package server.commands.commands;

import common.logging.Log;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.PlayerGateway;
import server.gateways.ServerGateway;
import server.permissions.Permissions;

public class StopCommand extends AbstractCommand {

  private final ServerGateway server;
  private final PlayerGateway players;

  public StopCommand(ServerGateway server, PlayerGateway players) {
    this.server = server;
    this.players = players;
  }

  @Override
  public void execute(CommandContext ctx) {

    // -------------------------------------
    // RESOLVE EXECUTOR (for logging)
    // -------------------------------------
    String executor;

    if (ctx.isConsole()) {
      executor = "Console";
    } else {
      String playerName = players.getName(ctx.getPlayer());
      executor = (playerName != null) ? playerName : ctx.getPlayer().toString();
    }

    // -------------------------------------
    // BROADCAST FIRST (important!)
    // -------------------------------------
    ctx.broadcast("Server is shutting down...");

    // -------------------------------------
    // LOG
    // -------------------------------------
    Log.info(executor + " issued server shutdown.");

    // -------------------------------------
    // SHUTDOWN
    // -------------------------------------
    server.shutdown();
  }

  @Override
  public String getName() {
    return "stop";
  }

  @Override
  public String getDescription() {
    return "Safely shuts down the server.";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_SERVER_STOP;
  }
}
