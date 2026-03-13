package server.commands.commands;

import common.logging.Log;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.player.ServerPlayer;

public class TeleportCommand extends AbstractCommand {

  @Override
  public String getName() {
    return "tp";
  }

  @Override
  public String getDescription() {
    return "Teleports the player to coordinates or another player.";
  }

  @Override
  public void execute(CommandContext ctx) {
    if (ctx.isConsole()) {
      Log.warn("Only players can use the teleport command.");
      return;
    }

    ServerPlayer player = ctx.getServer().getPlayerManager().getPlayer(ctx.getPlayer());
    if (player == null) return;

    var args = ctx.getArgs();

    if (args.size() == 3) {
      try {
        float x = Float.parseFloat(args.get(0));
        float y = Float.parseFloat(args.get(1));
        float z = Float.parseFloat(args.get(2));

        player.teleport(x, y, z, 0, 0);

        Log.info(player.getName() + " teleported to " + x + ", " + y + ", " + z);
      } catch (NumberFormatException e) {
        Log.warn("Invalid coordinates entered by " + player.getName());
      }
    } else {
      Log.warn("Usage: /tp <player> OR /tp <x> <y> <z>");
    }
  }

  @Override
  public String[] getArgumentLabels() {
    return new String[] {"x", "y", "z"};
  }
}
