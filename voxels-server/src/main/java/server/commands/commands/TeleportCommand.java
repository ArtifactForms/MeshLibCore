package server.commands.commands;

import java.util.UUID;

import common.logging.Log;
import common.world.ChunkData;
import common.world.Location;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.PlayerGateway;
import server.permissions.Permissions;

public class TeleportCommand extends AbstractCommand {

  private final PlayerGateway players;

  public TeleportCommand(PlayerGateway players) {
    this.players = players;
  }

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

    var args = ctx.getArgs();

    // -------------------------------------
    // /tp x y z
    // -------------------------------------
    if (args.size() == 3) {

      if (!ctx.hasPermission(Permissions.COMMAND_TELEPORT_SELF)) {
        Log.warn("No permission.");
        return;
      }

      handleCoordinateTeleport(ctx, args);
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

      handlePlayerTeleport(ctx, args.get(0));
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
  private void handleCoordinateTeleport(CommandContext ctx, java.util.List<String> args) {
    try {
      double x = Float.parseFloat(args.get(0));
      double y = Float.parseFloat(args.get(1));
      double z = Float.parseFloat(args.get(2));

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

      Location location = players.getLocation(ctx.getPlayer());
      Location dest = new Location(x, y, z, location.getPitch(), location.getYaw());
      players.teleport(ctx.getPlayer(), dest);

      Log.info(players.getName(ctx.getPlayer()) + " teleported to " + x + ", " + y + ", " + z);

    } catch (NumberFormatException e) {
      Log.warn("Invalid coordinates.");
    }
  }

  // =========================================================
  // PLAYER TELEPORT
  // =========================================================
  private void handlePlayerTeleport(CommandContext ctx, String targetName) {
    String playerName = players.getName(ctx.getPlayer());
    UUID targetId = players.getPlayerIdByName(targetName);

    if (targetId == null) {
      Log.warn("Player not found: " + targetName);
      return;
    }

    Location dest = players.getLocation(targetId);
    players.teleport(ctx.getPlayer(), dest);

    Log.info(playerName + " teleported to " + targetName);
  }

  // =========================================================
  // UTILS
  // =========================================================
  private boolean isFinite(double v) {
    return !Double.isNaN(v) && !Double.isInfinite(v);
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
