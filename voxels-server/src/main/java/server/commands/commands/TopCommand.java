package server.commands.commands;

import common.logging.Log;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.permissions.Permissions;
import server.player.ServerPlayer;

public class TopCommand extends AbstractCommand {

  @Override
  public String getName() {
    return "top";
  }

  @Override
  public String getDescription() {
    return "Teleports you to the highest solid block.";
  }

  @Override
  public void execute(CommandContext ctx) {

    // -------------------------------------
    // CONSOLE CHECK
    // -------------------------------------
    if (ctx.isConsole()) {
      ctx.reply("This command can only be used by a player.");
      return;
    }

    ServerPlayer player = ctx.getServer().getPlayerManager().getPlayer(ctx.getPlayer());

    if (player == null) return;

    int x = (int) player.getX();
    int z = (int) player.getZ();

    int topY = findTopY(ctx, x, z);

    if (topY == -1) {
      Log.warn("No valid top position found.");
      return;
    }

    player.teleport(x, topY + 1.5f, z, player.getYaw(), player.getPitch());

    ctx.reply("Teleported to top.");

    Log.info(player.getName() + " teleported to top.");
  }

  private int findTopY(CommandContext ctx, int x, int z) {
    return ctx.getServer().getWorld().getHeightAt(x, z) + 1;
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_TOP;
  }
}
