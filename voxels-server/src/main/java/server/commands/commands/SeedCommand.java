package server.commands.commands;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.WorldGateway;
import server.permissions.Permissions;

public class SeedCommand extends AbstractCommand {

  private final WorldGateway world;

  public SeedCommand(WorldGateway world) {
    this.world = world;
  }

  @Override
  public void execute(CommandContext ctx) {
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
