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

public class UnmuteCommand extends AbstractCommand {

  private final ModerationService moderation;

  private final PlayerGateway players;

  public UnmuteCommand(ModerationService moderation, PlayerGateway players) {
    this.moderation = moderation;
    this.players = players;
  }

  @Override
  public void execute(CommandContext ctx) {

    List<String> args = ctx.getArgs();

    if (args == null || args.isEmpty()) {
      ctx.reply("Missing argument.");
      return;
    }

    String playerName = args.get(0);
    UUID playerId = players.getPlayerIdByName(playerName);

    if (playerId == null) {
      ctx.reply(ModerationMessages.NO_SUCH_PLAYER_FOUND);
      return;
    }

    moderation.unmute(playerId);
    ctx.reply("Unmuted " + playerName + ".");
    ctx.broadcast(playerName + " was unmuted by " + players.getName(ctx.getPlayer()) + ".");
  }

  @Override
  public String getName() {
    return "unmute";
  }

  @Override
  public String getPermission() {
    return ModerationPermissions.MODERATION_COMMAND_UNMUTE;
  }

  @Override
  public String getDescription() {
    return "Unmutes a player, allowing them to send chat messages again.";
  }

  @Override
  public CommandArgument[] getArgumentLabels() {
    return new CommandArgument[] {
      new CommandArgument("player", true),
    };
  }
}
