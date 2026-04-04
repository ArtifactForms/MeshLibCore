package server.commands.commands;

import java.util.Locale;

import common.world.Location;
import common.world.WorldMath;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.PlayerGateway;
import server.permissions.Permissions;

public class PositionCommand extends AbstractCommand {

  private final PlayerGateway players;

  public PositionCommand(PlayerGateway players) {
    this.players = players;
  }

  @Override
  public void execute(CommandContext ctx) {
    if (ctx.isConsole()) {
      ctx.reply("This command can only be used by a player.");
      return;
    }

    Location loc = players.getLocation(ctx.getPlayer());
    if (loc == null) return; // should not happen

    int chunkX = WorldMath.worldToChunkX(loc);
    int chunkZ = WorldMath.worldToChunkZ(loc);

    ctx.reply(formatPosition(loc));
    ctx.reply(formatChunk(chunkX, chunkZ));
  }

  private String formatPosition(Location loc) {
    return String.format(
        Locale.ROOT,
        "Position: X=%.2f Y=%.2f Z=%.2f | Yaw=%.2f Pitch=%.2f",
        loc.getX(),
        loc.getY(),
        loc.getZ(),
        Math.toDegrees(loc.getYaw()),
        Math.toDegrees(loc.getPitch()));
  }

  private String formatChunk(int x, int z) {
    return "Chunk: X=" + x + " Z=" + z;
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
