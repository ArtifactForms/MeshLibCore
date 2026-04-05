package server.commands.commands;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

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

    UUID target = resolveTargetPlayer(ctx);
    if (target == null) {
      return;
    }

    Location loc = players.getLocation(target);
    if (loc == null) {
      // should not happen, but fail silently or log if needed
      return;
    }

    sendPosition(ctx, loc);
  }

  private UUID resolveTargetPlayer(CommandContext ctx) {
    List<String> args = ctx.getArgs();

    // Self
    if (args.isEmpty()) {
      return ctx.getPlayer();
    }

    // Other player → permission check
    if (!ctx.hasPermission(Permissions.COMMAND_WORLD_POSITION_OTHERS)) {
      ctx.reply("You don't have permission to view other players' positions.");
      return null;
    }

    String name = args.get(0);
    UUID playerId = players.getPlayerIdByName(name);

    if (playerId == null) {
      ctx.reply("No such player.");
      return null;
    }

    return playerId;
  }

  private void sendPosition(CommandContext ctx, Location loc) {
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
  public String[] getArgumentLabels() {
    return new String[] {"player"};
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
    return "Displays your or another player's coordinates and chunk location.";
  }

  @Override
  public String[] getAliases() {
    return new String[] {"position", "coords"};
  }
}
