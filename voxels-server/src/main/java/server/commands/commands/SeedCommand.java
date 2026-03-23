package server.commands.commands;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.permissions.Permissions;
import server.world.ServerWorld;

public class SeedCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext ctx) {
    ServerWorld world = ctx.getServer().getWorld();

    long seed = world.getSeed();

    ctx.reply("World Seed: " + seed);
  }

  @Override
  public String getName() {
    return "seed";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_WORLD_SEED;
  }

  @Override
  public String getDescription() {
    return "Displays the generation seed for the current world.";
  }
}
