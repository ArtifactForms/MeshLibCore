package server.commands.commands;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.permissions.Permissions;

public class BroadcastCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext ctx) {
    if (ctx.getArgs().isEmpty()) {
      ctx.reply("Missing argument <message>.");
      return;
    }

    String message = String.join(" ", ctx.getArgs());
    ctx.broadcast(message);
  }

  @Override
  public String getName() {
    return "broadcast";
  }

  @Override
  public String getDescription() {
    return "Broadcasts a server-wide message to all players.";
  }

  @Override
  public String[] getArgumentLabels() {
    return new String[] {"message"};
  }

  @Override
  public String[] getAliases() {
    return new String[] {"bc"};
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_BROADCAST;
  }
}
