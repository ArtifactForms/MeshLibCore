package server.commands.commands;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.PlayerGateway;
import server.permissions.Permissions;
import server.player.ServerPlayer;

public class KickCommand extends AbstractCommand {

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

    // 1. Spieler suchen
    String targetName = args.get(0);
    ServerPlayer target = ctx.getServer().getPlayerManager().getPlayerByName(targetName);

    if (target == null) {
      ctx.reply("§cPlayer '" + targetName + "' is not online.");
      return;
    }

    // 2. Grund zusammenbauen (Default, falls kein Grund angegeben wurde)
    String reason = "Kicked by an operator.";
    if (args.size() > 1) {
      reason = String.join(" ", args.subList(1, args.size()));
    }

    // 3. Kick ausführen
    // Wir nutzen Farbcodes, damit der Spieler im Disconnect-Screen sieht, warum er fliegt
    //    target.kick("§cYou were kicked from the server.\n§7Reason: §f" + reason);
    ctx.getServer().getPlayerManager().kick(target);

    // Feedback für den Admin und den Server-Log
    ctx.reply("§aPlayer " + target.getName() + " has been kicked.");
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
