package server.modules.moderation.commands;

import java.util.List;
import java.util.UUID;

import server.commands.AbstractCommand;
import server.commands.CommandArgument;
import server.commands.CommandContext;
import server.gateways.PlayerGateway;
import server.modules.moderation.ModerationPermissions;
import server.modules.moderation.ModerationService;
import server.util.DurationParser;

public class BanCommand extends AbstractCommand {

  private final ModerationService moderation;

  private final PlayerGateway players;

  public BanCommand(ModerationService moderation, PlayerGateway players) {
    this.moderation = moderation;
    this.players = players;
  }

  @Override
  public void execute(CommandContext ctx) {

    List<String> args = ctx.getArgs();

    if (args.isEmpty()) {
      ctx.reply("§cUsage: /ban <player> [duration] [reason]");
      return;
    }

    String targetName = args.get(0);
    UUID targetId = players.getPlayerIdByName(targetName);

    if (targetId == null) {
      ctx.reply("§cPlayer '" + targetName + "' not found.");
      return;
    }

    if (moderation.isBanned(targetId)) {
      ctx.reply("§cPlayer is already banned.");
      return;
    }

    String durationArg = null;
    String reason = null;

    if (args.size() >= 2 && DurationParser.isDuration(args.get(1))) {
      durationArg = args.get(1);

      if (args.size() > 2) {
        reason = String.join(" ", args.subList(2, args.size()));
      }
    } else if (args.size() >= 2) {
      reason = String.join(" ", args.subList(1, args.size()));
    }

    if (durationArg != null) {
      long duration = DurationParser.parseToMillis(durationArg);

      if (duration <= 0) {
        ctx.reply("§cInvalid duration.");
        return;
      }

      moderation.ban(targetId, System.currentTimeMillis() + duration, reason);
    } else {
      moderation.ban(targetId, -1L, reason);
    }

    //    // Optional: sofort kicken, falls online
    //    var online = players.getOnlinePlayer(targetName);
    //    if (online != null) {
    //      players.kick(
    //          targetId, "§cYou are banned.\n§7Reason: §f" + (reason != null ? reason : "No
    // reason"));
    //    }

    // Feedback
    String msg = "§aBanned §f" + targetName;

    if (durationArg != null) {
      msg += " §afor §f" + durationArg;
    }

    if (reason != null) {
      msg += " §7(" + reason + ")";
    }

    ctx.reply(msg + "§a.");
  }

  @Override
  public String getName() {
    return "ban";
  }

  @Override
  public String getDescription() {
    return "Bans a player from the server.";
  }

  @Override
  public CommandArgument[] getArgumentLabels() {
    return new CommandArgument[] {
      new CommandArgument("player", true),
      new CommandArgument("duration", false),
      new CommandArgument("reason", false),
    };
  }

  @Override
  public String getPermission() {
    return ModerationPermissions.MODERATION_COMMAND_BAN;
  }
}
