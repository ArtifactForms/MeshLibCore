package server.commands.commands;

import java.util.Collection;
import java.util.stream.Collectors;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.ConfigGateway;
import server.permissions.Permissions;
import server.player.ServerPlayer;

public class PlayersCommand extends AbstractCommand {

  private final ConfigGateway config;

  public PlayersCommand(ConfigGateway config) {
    this.config = config;
  }

  @Override
  public void execute(CommandContext ctx) {

    Collection<ServerPlayer> onlinePlayers = ctx.getServer().getPlayerManager().getAllPlayers();
    int count = onlinePlayers.size();
    int max = config.getMaxPlayers();

    StringBuilder sb = new StringBuilder();
    sb.append("§e--- Online Players (§6")
        .append(count)
        .append("§e/§6")
        .append(max)
        .append("§e) ---\n");

    if (count == 0) {
      sb.append("§7The server is lonely... only ghosts here.");
    } else {
      String playerList =
          onlinePlayers
              .stream()
              .map(
                  p -> {
                    String color = ctx.hasPermission(Permissions.ADMIN_DEBUG) ? "§c" : "§f";
                    return color + p.getName();
                  })
              .collect(Collectors.joining("§7, "));

      sb.append(playerList);
    }

    ctx.reply(sb.toString());
  }

  @Override
  public String getName() {
    return "players";
  }

  @Override
  public String[] getAliases() {
    return new String[] {"list", "who", "online"};
  }

  @Override
  public String getDescription() {
    return "Shows a list of all online players.";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_PLAYERS;
  }
}
