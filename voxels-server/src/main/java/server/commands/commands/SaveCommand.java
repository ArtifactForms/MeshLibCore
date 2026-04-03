package server.commands.commands;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.WorldGateway;
import server.permissions.Permissions;

public class SaveCommand extends AbstractCommand {

  private WorldGateway world;

  public SaveCommand(WorldGateway world) {
    this.world = world;
  }

  @Override
  public void execute(CommandContext context) {
    world.saveDirtyChunks();
  }

  @Override
  public String getName() {
    return "save";
  }

  @Override
  public String getDescription() {
    return "Forces the server to save all dirty chunks.";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_WORLD_SAVE;
  }
}
