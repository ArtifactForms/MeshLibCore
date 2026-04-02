package server.commands.commands;

import common.logging.Log;
import common.world.ChunkData;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.WorldGateway;
import server.permissions.Permissions;
import server.player.ServerPlayer;

public class TopCommand extends AbstractCommand {

  private final WorldGateway world;

  public TopCommand(WorldGateway world) {
    this.world = world;
  }

  @Override
  public void execute(CommandContext ctx) {
    if (ctx.isConsole()) {
      Log.warn("[Command] Only players can use this command.");
      return;
    }

    ServerPlayer player = ctx.getServer().getPlayerManager().getPlayer(ctx.getPlayer());

    if (player == null) return;

    int x = (int) player.getX();
    int z = (int) player.getZ();

    int topY = world.getHeightAt(x, z);

    if (topY < 0 || topY >= ChunkData.HEIGHT) {
      Log.warn("No valid top position found.");
      return;
    }

    player.teleport(x, topY + 1.5f, z, player.getYaw(), player.getPitch());

    ctx.reply("Teleported to top.");

    Log.info(player.getName() + " teleported to top.");
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
