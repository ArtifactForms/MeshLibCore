package server.commands.commands;

import java.util.Locale;

import common.game.Inventory;
import common.network.packets.PlayerInventoryFullUpdatePacket;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.permissions.Permissions;
import server.player.ServerPlayer;

public class InventoryCommand extends AbstractCommand {

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
    if (player == null) {
      // TODO Log
      return;
    }

    String sub = ctx.getArgs().get(0).toLowerCase(Locale.ROOT);

    if (!sub.equals("clear")) {
      return;
    }

    Inventory inventory = player.getInventory();
    inventory.clear();
    player.incrementInventoryVersion();

    player
        .getConnection()
        .send(
            new PlayerInventoryFullUpdatePacket(
                inventory.getItems(), null, player.getInventoryVersion()));
  }

  @Override
  public String[] getArgumentLabels() {
    return new String[] {"clear"};
  }

  @Override
  public String getName() {
    return "inventory";
  }

  @Override
  public String getDescription() {
    return "Clears the inventory.";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_INVENTORY;
  }
}
