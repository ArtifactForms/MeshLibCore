package server.commands.commands;

import common.world.World;
import common.world.WorldTime;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.permissions.Permissions;

public class DayCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext ctx) {
    World world = ctx.getServer().getWorld();

    long time = WorldTime.getTicksFromKeyword("day");
    world.setWorldTime(time);

    ctx.reply("Time set to day (" + time + ")");
  }

  @Override
  public String getName() {
    return "day";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_WORLD_TIME;
  }

  @Override
  public String getDescription() {
    return "Sets the current world time to day.";
  }
}
