package server.commands.commands;

import common.world.WorldTime;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.WorldGateway;
import server.permissions.Permissions;

public class DayCommand extends AbstractCommand {

  private final WorldGateway world;

  public DayCommand(WorldGateway world) {
    this.world = world;
  }

  @Override
  public void execute(CommandContext ctx) {
    long time = WorldTime.getTicksFromKeyword("day");
//    world.setWorldTime(time);
    world.setTimeOfDay(time);

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
