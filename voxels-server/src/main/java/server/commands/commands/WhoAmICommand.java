package server.commands.commands;

import java.util.UUID;

import common.logging.Log;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.commands.CommandMessages;
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
      Log.warn("[Command] " + CommandMessages.PLAYERS_ONLY);
    }
    ctx.reply(createReplyMessage(ctx));
  }

  private String createReplyMessage(CommandContext ctx) {
    if (ctx.isConsole()) {
      return CommandMessages.PLAYERS_ONLY;
    } else {
      UUID playerId = ctx.getPlayer();
      String name = players.getName(playerId);
      return "UUID: " + playerId + " | Name: " + (name != null ? name : "Unknown");
    }
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
