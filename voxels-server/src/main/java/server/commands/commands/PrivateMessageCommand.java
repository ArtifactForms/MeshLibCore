package server.commands.commands;

import java.util.List;
import java.util.UUID;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.network.GameServer;
import server.player.ServerPlayer;

public class PrivateMessageCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext context) {
    List<String> args = context.getArgs();

    UUID uuid = context.getPlayer();
    GameServer server = context.getServer();
    ServerPlayer player = server.getPlayerManager().getPlayer(uuid);

    player.sendMessage("Das ist ein Test!");
  }

  @Override
  public String[] getAliases() {
    return new String[] {"say"};
  }

  @Override
  public String getName() {
    return "msg";
  }

  @Override
  public String[] getArgumentLabels() {
    return new String[] {""};
  }
}
