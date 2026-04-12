package server.commands.commands;

import server.commands.AbstractCommand;
import server.commands.CommandArgument;
import server.commands.CommandContext;
import server.permissions.Permissions;

public class EchoCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext ctx) {
    if (ctx.getArgs().isEmpty()) {
      ctx.reply("§cUsage: /echo <message>");
      getUsage();
      return;
    }
    String message = String.join(" ", ctx.getArgs());

    ctx.reply(message);
  }

  @Override
  public String getName() {
    return "echo";
  }

  @Override
  public CommandArgument[] getArgumentLabels() {
    return new CommandArgument[] {new CommandArgument("message", true)};
  }

  @Override
  public String getDescription() {
    return "Echos your input back to you.";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_ECHO;
  }
}
