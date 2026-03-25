package server.commands.commands;

import common.logging.Log;
import common.world.ChunkData;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.permissions.Permissions;
import server.player.ServerPlayer;

public class TeleportCommand extends AbstractCommand {

  @Override
  public String getName() {
    return "tp";
  }

  @Override
  public String[] getAliases() {
    return new String[] {"teleport"};
  }

  @Override
  public String getDescription() {
    return "Teleports to coordinates or another player.";
  }

  @Override
  public void execute(CommandContext ctx) {

    if (ctx.isConsole()) {
      Log.warn("Only players can use this command.");
      return;
    }

    ServerPlayer player = ctx.getServer().getPlayerManager().getPlayer(ctx.getPlayer());

    if (player == null) return;

    var args = ctx.getArgs();

    // -------------------------------------
    // /tp x y z
    // -------------------------------------
    if (args.size() == 3) {

      if (!ctx.hasPermission(Permissions.COMMAND_TELEPORT_SELF)) {
        Log.warn("No permission.");
        return;
      }

      handleCoordinateTeleport(player, args);
      return;
    }

    // -------------------------------------
    // /tp <player>
    // -------------------------------------
    if (args.size() == 1) {

      if (!ctx.hasPermission(Permissions.COMMAND_TELEPORT_OTHERS)) {
        Log.warn("No permission.");
        return;
      }

      handlePlayerTeleport(ctx, player, args.get(0));
      return;
    }

    // -------------------------------------
    // INVALID USAGE
    // -------------------------------------
    Log.warn("Usage: /tp <x> <y> <z> OR /tp <player>");
  }

  // =========================================================
  // COORDINATE TELEPORT
  // =========================================================
  private void handleCoordinateTeleport(ServerPlayer player, java.util.List<String> args) {
    try {
      float x = Float.parseFloat(args.get(0));
      float y = Float.parseFloat(args.get(1));
      float z = Float.parseFloat(args.get(2));

      // -------------------------------------
      // VALIDATION
      // -------------------------------------

      if (!isFinite(x) || !isFinite(y) || !isFinite(z)) {
        Log.warn("Invalid coordinates (NaN or Infinity).");
        return;
      }

      if (y < 0 || y > ChunkData.HEIGHT - 1) {
        Log.warn("Y must be between 0 and " + (ChunkData.HEIGHT - 1));
        return;
      }

      // -------------------------------------
      // TELEPORT
      // -------------------------------------
      player.teleport(x, y, z, player.getYaw(), player.getPitch());

      Log.info(player.getName() + " teleported to " + x + ", " + y + ", " + z);

    } catch (NumberFormatException e) {
      Log.warn("Invalid coordinates.");
    }
  }

  // =========================================================
  // PLAYER TELEPORT
  // =========================================================
  private void handlePlayerTeleport(CommandContext ctx, ServerPlayer player, String targetName) {

    ServerPlayer target = ctx.getServer().getPlayerManager().getPlayerByName(targetName);

    if (target == null) {
      Log.warn("Player not found: " + targetName);
      return;
    }

    player.teleport(
        target.getX(), target.getY(), target.getZ(), target.getYaw(), target.getPitch());

    Log.info(player.getName() + " teleported to " + target.getName());
  }

  // =========================================================
  // UTILS
  // =========================================================
  private boolean isFinite(float v) {
    return !Float.isNaN(v) && !Float.isInfinite(v);
  }

  @Override
  public String[] getArgumentLabels() {
    return new String[] {"x", "y", "z", "player"};
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_TELEPORT;
  }
}
