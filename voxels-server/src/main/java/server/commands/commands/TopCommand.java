package server.commands.commands;

import common.logging.Log;
import common.world.ChunkData;
import common.world.Location;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.PlayerGateway;
import server.gateways.WorldGateway;
import server.permissions.Permissions;

public class TopCommand extends AbstractCommand {

  private final WorldGateway world;

  private final PlayerGateway players;

  public TopCommand(WorldGateway world, PlayerGateway players) {
    this.world = world;
    this.players = players;
  }

  @Override
  public void execute(CommandContext ctx) {
    if (ctx.isConsole()) {
      Log.warn("[Command] Only players can use this command.");
      return;
    }

    Location location = players.getLocation(ctx.getPlayer());
    if (location == null) {
      ctx.reply("An error occured.");
      return;
    }

    int x = (int) location.getX();
    int z = (int) location.getZ();

    int topY = world.getHeightAt(x, z);

    if (topY < 0 || topY >= ChunkData.HEIGHT) {
      Log.warn("No valid top position found.");
      return;
    }

    location.setPosition(x, topY + 1.5f, z);

    players.teleport(ctx.getPlayer(), location);

    ctx.reply("Teleported to top.");

    Log.info(players.getName(ctx.getPlayer()) + " teleported to top.");
  }

  @Override
  public String getName() {
    return "top";
  }

  @Override
  public String getDescription() {
    return "Teleports you to the highest solid block.";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_TOP;
  }
}
