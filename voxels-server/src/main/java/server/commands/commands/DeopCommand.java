package server.commands.commands;

import java.util.List;
import java.util.UUID;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.MessageGateway;
import server.gateways.PermissionGateway;
import server.gateways.PlayerGateway;
import server.permissions.Permissions;

public class DeopCommand extends AbstractCommand {

  private final PermissionGateway permissions;

  private final PlayerGateway players;

  private final MessageGateway messages;

  public DeopCommand(
      PermissionGateway permissions, PlayerGateway players, MessageGateway messages) {
    this.permissions = permissions;
    this.players = players;
    this.messages = messages;
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_DEOP;
  }

  @Override
  public void execute(CommandContext ctx) {
    List<String> args = ctx.getArgs();

    if (args.isEmpty()) {
      ctx.reply("§cUsage: /deop <player>");
      return;
    }

    String playerName = args.get(0);
    UUID playerId = players.getPlayerIdByName(playerName);

    if (playerId == null) {
      ctx.reply("§cPlayer not found.");
      return;
    }

    if (!permissions.isOp(playerId)) {
      ctx.reply("§e" + playerName + " is not an operator.");
      return;
    }

    permissions.setOp(playerId, false);

    ctx.reply("§a" + playerName + " is no longer an operator.");
    messages.sendMessage(playerId, "§cYou are no longer an operator.");
  }

  @Override
  public String[] getArgumentLabels() {
    return new String[] {"player"};
  }

  @Override
  public String getName() {
    return "deop";
  }

  @Override
  public String getDescription() {
    return "Removes operator privileges from a player.";
  }
}
