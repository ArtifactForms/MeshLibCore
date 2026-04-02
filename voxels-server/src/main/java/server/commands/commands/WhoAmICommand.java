package server.commands.commands;

import java.util.UUID;

import common.logging.Log;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.PlayerGateway;
import server.permissions.Permissions;

public class WhoAmICommand extends AbstractCommand {

  private final PlayerGateway players;

  public WhoAmICommand(PlayerGateway players) {
    this.players = players;
  }

  @Override
  public void execute(CommandContext ctx) {
    if (ctx.isConsole()) {
      Log.warn("[Command] Only players can use this command.");
      return;
    }

    UUID playerId = ctx.getPlayer();
    String name = players.getName(playerId);
    ctx.reply("UUID: " + playerId + " | Name: " + (name != null ? name : "Unknown"));
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_UUID;
  }

  @Override
  public String getName() {
    return "whoami";
  }

  @Override
  public String[] getAliases() {
    return new String[] {"uuid"};
  }

  @Override
  public String getDescription() {
    return "Prints your player information.";
  }
}
