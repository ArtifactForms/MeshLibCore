package server.commands.commands;

import java.util.UUID;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.PlayerGateway;
import server.permissions.Permissions;

public class KickCommand extends AbstractCommand {

  private static final String DEFAULT_REASON = "Kicked by an operator.";

  private final PlayerGateway players;

  public KickCommand(PlayerGateway players) {
    this.players = players;
  }

  @Override
  public void execute(CommandContext ctx) {
    var args = ctx.getArgs();

    if (args.isEmpty()) {
      ctx.reply("§cUsage: /kick <player> [reason]");
      return;
    }

    String playerName = args.get(0);
    UUID playerId = players.getPlayerIdByName(playerName);

    if (playerId == null) {
      ctx.reply("§cPlayer '" + playerName + "' is not online or unknown.");
      return;
    }

    String reason = DEFAULT_REASON;
    if (args.size() > 1) {
      reason = String.join(" ", args.subList(1, args.size()));
    }

    String message = "You were kicked from the server.\nReason: " + reason;

    players.kick(playerId, message);

    ctx.reply("§aPlayer " + playerName + " has been kicked.");
  }

  @Override
  public String getName() {
    return "kick";
  }

  @Override
  public String getDescription() {
    return "Disconnects a player from the server.";
  }

  @Override
  public String[] getArgumentLabels() {
    return new String[] {"player", "reason"};
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_KICK;
  }
}
