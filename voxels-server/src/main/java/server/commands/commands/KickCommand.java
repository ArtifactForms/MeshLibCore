package server.commands.commands;

import java.util.UUID;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.events.events.PlayerKickEvent;
import server.gateways.EventGateway;
import server.gateways.PlayerGateway;
import server.permissions.Permissions;

public class KickCommand extends AbstractCommand {

  private static final String DEFAULT_REASON = "Kicked by an operator.";

  private final PlayerGateway players;
  private final EventGateway events;

  public KickCommand(PlayerGateway players, EventGateway events) {
    this.players = players;
    this.events = events;
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

    PlayerKickEvent event = new PlayerKickEvent(playerId, playerName, reason);
    events.fire(event);

    if (event.isCancelled()) {
      ctx.reply("Kick event was cancelled by a subsystem.");
      return;
    }

    String message = "You were kicked from the server.\nReason: " + event.getReason();

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
