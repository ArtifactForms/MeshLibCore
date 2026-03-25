package server.commands.commands;

import java.util.Locale;

import math.Vector3f;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.permissions.Permissions;
import server.player.ServerPlayer;

public class PositionCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext ctx) {

    // -------------------------------------
    // CONSOLE CHECK
    // -------------------------------------
    if (ctx.isConsole()) {
      ctx.reply("This command can only be used by a player.");
      return;
    }

    // -------------------------------------
    // GET PLAYER
    // -------------------------------------
    ServerPlayer player = ctx.getServer().getPlayerManager().getPlayer(ctx.getPlayer());

    if (player == null) {
      //      ctx.reply("Player not found.");
      // TODO Log instead cause we can not reply
      return;
    }

    // -------------------------------------
    // DATA
    // -------------------------------------
    Vector3f pos = player.getPosition();

    int chunkX = player.getChunkX();
    int chunkZ = player.getChunkZ();

    // -------------------------------------
    // OUTPUT
    // -------------------------------------
    String positionStr =
        String.format(Locale.ROOT, "Position: X=%.2f Y=%.2f Z=%.2f", pos.x, pos.y, pos.z);

    String chunkStr = "Chunk: X=" + chunkX + " Z=" + chunkZ;

    ctx.reply(positionStr);
    ctx.reply(chunkStr);
  }

  @Override
  public String getName() {
    return "pos";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_WORLD_POSITION;
  }

  @Override
  public String getDescription() {
    return "Displays your current world coordinates and chunk indices.";
  }

  @Override
  public String[] getAliases() {
    return new String[] {"position"};
  }
}
