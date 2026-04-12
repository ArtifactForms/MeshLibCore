package server.modules.moderation.commands;

import java.util.List;
import java.util.UUID;

import server.commands.AbstractCommand;
import server.commands.CommandArgument;
import server.commands.CommandContext;
import server.gateways.PlayerGateway;
import server.modules.moderation.ModerationMessages;
import server.modules.moderation.ModerationPermissions;
import server.modules.moderation.ModerationService;
import server.util.DurationParser;

public class MuteCommand extends AbstractCommand {

  private final ModerationService moderation;

  private final PlayerGateway players;

  public MuteCommand(ModerationService moderation, PlayerGateway players) {
    this.moderation = moderation;
    this.players = players;
  }

  @Override
  public void execute(CommandContext ctx) {

    List<String> args = ctx.getArgs();

    if (args == null || args.isEmpty()) {
      ctx.reply("Missing argument: player");
      return;
    }

    String playerName = args.get(0);
    UUID playerId = players.getPlayerIdByName(playerName);

    if (playerId == null) {
      ctx.reply(ModerationMessages.NO_SUCH_PLAYER_FOUND);
      return;
    }

    if (moderation.isMuted(playerId)) {
      ctx.reply(playerName + " is already muted.");
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

    // APPLY MUTE
    if (durationArg != null) {
      long duration = DurationParser.parseToMillis(durationArg);

      if (duration <= 0) {
        ctx.reply("Invalid duration.");
        return;
      }

      moderation.mute(playerId, duration);
    } else {
      moderation.mute(playerId);
    }

    // FEEDBACK
    String message = "Muted " + playerName;

    if (durationArg != null) {
      message += " for " + durationArg;
    }

    if (reason != null) {
      message += " (" + reason + ")";
    }

    ctx.reply(message + ".");
  }

  @Override
  public String getName() {
    return "mute";
  }

  @Override
  public String getPermission() {
    return ModerationPermissions.MODERATION_COMMAND_MUTE;
  }

  @Override
  public String getDescription() {
    return "Mutes a player, preventing them from sending chat messages.";
  }

  @Override
  public CommandArgument[] getArgumentLabels() {
    return new CommandArgument[] {
      new CommandArgument("player", true),
      new CommandArgument("duration", false),
      new CommandArgument("reason", false),
    };
  }
}
