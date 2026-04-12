package server.commands.commands;

import java.util.List;
import java.util.UUID;

import server.commands.AbstractCommand;
import server.commands.CommandArgument;
import server.commands.CommandContext;
import server.gateways.MessageGateway;
import server.gateways.PermissionGateway;
import server.gateways.PlayerGateway;
import server.permissions.Permissions;

public class OpCommand extends AbstractCommand {

  private final PermissionGateway permissions;

  private final PlayerGateway players;

  private final MessageGateway messages;

  public OpCommand(PermissionGateway permissions, PlayerGateway players, MessageGateway messages) {
    this.permissions = permissions;
    this.players = players;
    this.messages = messages;
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_OP;
  }

  @Override
  public void execute(CommandContext ctx) {
    List<String> args = ctx.getArgs();

    if (args.isEmpty()) {
      ctx.reply("§cUsage: /op <player>");
      return;
    }

    String playerName = args.get(0);
    UUID playerId = players.getPlayerIdByName(playerName);

    if (playerId == null) {
      ctx.reply("§cPlayer not found.");
      return;
    }

    if (permissions.isOp(playerId)) {
      ctx.reply("§e" + playerName + " is already an operator.");
      return;
    }

    permissions.setOp(playerId, true);

    ctx.reply("§a" + playerName + " is now an operator.");
    messages.sendMessage(playerId, "§aYou are now an operator.");
  }

  @Override
  public CommandArgument[] getArgumentLabels() {
    return new CommandArgument[] {new CommandArgument("player", true)};
  }

  @Override
  public String getName() {
    return "op";
  }

  @Override
  public String getDescription() {
    return "Grants operator privileges to a player.";
  }
}
