package server.modules.moderation.commands;

import java.util.Map;
import java.util.UUID;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.PlayerGateway;
import server.modules.moderation.ModerationMessages;
import server.modules.moderation.ModerationPermissions;
import server.modules.moderation.ModerationService;
import server.modules.moderation.Mute;
import server.util.TimeFormatter;

public class MuteListCommand extends AbstractCommand {

  private final ModerationService moderation;

  private final PlayerGateway players;

  public MuteListCommand(ModerationService moderation, PlayerGateway players) {
    this.moderation = moderation;
    this.players = players;
  }

  @Override
  public String getPermission() {
    return ModerationPermissions.MODERATION_COMMAND_MUTE_LIST;
  }

  @Override
  public void execute(CommandContext ctx) {

    Map<UUID, Mute> muted = moderation.getMutedEntries();

    if (muted.isEmpty()) {
      ctx.reply(ModerationMessages.NO_PLAYERS_MUTED);
      return;
    }

    long now = System.currentTimeMillis();

    StringBuilder sb = new StringBuilder();
    sb.append("Muted players (").append(muted.size()).append("):\n");

    for (Map.Entry<UUID, Mute> entry : muted.entrySet()) {
      UUID playerId = entry.getKey();
      Mute mute = entry.getValue();

      String name = players.getName(playerId);
      if (name == null) name = "Unknown";

      sb.append("- ").append(name);

      if (mute.isPermanent()) {
        sb.append(" [permanent]");
      } else {
        long remaining = mute.getExpiresAt() - now;

        if (remaining <= 0) continue;

        sb.append(" [").append(TimeFormatter.formatRemaining(remaining)).append("]");
      }

      String reason = mute.getReason();
      if (reason != null && !reason.isBlank()) {
        sb.append(" - ").append(reason);
      }

      sb.append("\n");
    }

    ctx.reply(sb.toString().trim());
  }

  @Override
  public String getName() {
    return "mutelist";
  }

  @Override
  public String getDescription() {
    return "Displays all muted players including remaining time and reason.";
  }
}
