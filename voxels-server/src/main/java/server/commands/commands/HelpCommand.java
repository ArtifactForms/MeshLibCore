package server.commands.commands;

import java.util.Collection;

import server.commands.AbstractCommand;
import server.commands.Command;
import server.commands.CommandContext;
import server.permissions.Permissions;

public class HelpCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext ctx) {
    // -------------------------------------
    // CONSOLE CHECK
    // -------------------------------------
    if (ctx.isConsole()) {
      ctx.reply("This command can only be used by a player.");
      return;
    }

    Collection<Command> commands = ctx.getServer().getCommands();

    StringBuilder builder = new StringBuilder();
    for (Command command : commands) {
      builder.append(command.getName());
      builder.append("\n");
    }

    ctx.reply(builder.toString());
  }

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_HELP;
  }
}
