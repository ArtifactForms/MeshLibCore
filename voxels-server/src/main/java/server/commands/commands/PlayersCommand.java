package server.commands.commands;

import java.util.Collection;
import java.util.stream.Collectors;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.ConfigGateway;
import server.gateways.PlayerGateway;
import server.permissions.Permissions;

public class PlayersCommand extends AbstractCommand {

  private final PlayerGateway players;

  private final ConfigGateway config;

  public PlayersCommand(PlayerGateway players, ConfigGateway config) {
    this.players = players;
    this.config = config;
  }

  @Override
  public void execute(CommandContext ctx) {

    Collection<java.util.UUID> onlinePlayers = players.getOnlinePlayers();
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
                  id -> {
                    String name = players.getName(id);

                    String color = ctx.hasPermission(Permissions.ADMIN_DEBUG) ? "§c" : "§f";
                    return color + name;
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
