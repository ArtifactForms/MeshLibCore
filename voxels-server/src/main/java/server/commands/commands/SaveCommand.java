package server.commands.commands;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.permissions.Permissions;

public class SaveCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext context) {
    context.getServer().getWorld().saveDirtyChunks();
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
