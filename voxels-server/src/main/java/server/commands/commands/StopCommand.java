package server.commands.commands;

import common.logging.Log;
import server.commands.AbstractCommand;
import server.commands.CommandContext;

public class StopCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext context) {
    if (!context.isConsole()) {
      Log.info("Player " + context.getPlayer() + " issued server shutdown.");
    } else {
      Log.info("Console issued server shutdown.");
    }
    context.getServer().shutdown();
  }

  @Override
  public String getName() {
    return "stop";
  }

  @Override
  public String getDescription() {
    return "Safely shuts down the server.";
  }

  @Override
  public String[] getArgumentLabels() {
    return new String[] {};
  }
}
