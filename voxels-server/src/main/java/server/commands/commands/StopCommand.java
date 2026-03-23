package server.commands.commands;

import common.logging.Log;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.permissions.Permissions;
import server.player.ServerPlayer;

public class StopCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext ctx) {

    // -------------------------------------
    // RESOLVE EXECUTOR (for logging)
    // -------------------------------------
    String executor;

    if (ctx.isConsole()) {
      executor = "Console";
    } else {
      ServerPlayer player = ctx.getServer().getPlayerManager().getPlayer(ctx.getPlayer());
      executor = (player != null) ? player.getName() : ctx.getPlayer().toString();
    }

    // -------------------------------------
    // BROADCAST FIRST (important!)
    // -------------------------------------
    ctx.getServer().broadcastMessage("Server is shutting down...");

    // -------------------------------------
    // LOG
    // -------------------------------------
    Log.info(executor + " issued server shutdown.");

    // -------------------------------------
    // SHUTDOWN
    // -------------------------------------
    ctx.getServer().shutdown();
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
