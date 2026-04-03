package server.commands.commands;

import java.util.List;

import server.commands.AbstractCommand;
import server.commands.Command;
import server.commands.CommandContext;
import server.gateways.CommandGateway;
import server.permissions.Permissions;

public class HelpCommand extends AbstractCommand {

  private static final int PAGE_SIZE = 8;

  private final CommandGateway commands;

  public HelpCommand(CommandGateway commands) {
    this.commands = commands;
  }

  @Override
  public void execute(CommandContext ctx) {
    if (ctx.isConsole()) {
      ctx.reply("This command can only be used by a player.");
      return;
    }

    var args = ctx.getArgs();

    if (!args.isEmpty() && !isInteger(args.get(0))) {
      String commandName = args.get(0).toLowerCase(java.util.Locale.ROOT);
      Command target = commands.getCommand(commandName);

      if (target == null || !ctx.hasPermission(target.getPermission())) {
        ctx.reply("§cCommand '" + commandName + "' not found.");
        return;
      }

      renderDetailedHelp(ctx, target);
      return;
    }

    renderPageHelp(ctx, args);
  }

  private void renderDetailedHelp(CommandContext ctx, Command cmd) {
    StringBuilder sb = new StringBuilder();
    sb.append("§e--- Command Info: §6/").append(cmd.getName()).append(" §e---\n");
    sb.append("§7Description: §f").append(cmd.getDescription()).append("\n");

    sb.append("§7Usage: §e/").append(cmd.getName());
    for (String label : cmd.getArgumentLabels()) {
      sb.append(" <").append(label).append(">");
    }
    sb.append("\n");

    if (cmd.getAliases().length > 0) {
      sb.append("§7Aliases: §8").append(String.join(", ", cmd.getAliases())).append("\n");
    }

    if (ctx.hasPermission(Permissions.ADMIN_DEBUG)) {
      sb.append("§7Permission: §d").append(cmd.getPermission()).append("\n");
    }

    ctx.reply(sb.toString());
  }

  private void renderPageHelp(CommandContext ctx, java.util.List<String> args) {
    List<Command> available =
        commands
            .getCommands()
            .stream()
            .filter(cmd -> ctx.hasPermission(cmd.getPermission()))
            .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
            .toList();

    int totalPages = (int) Math.ceil((double) available.size() / PAGE_SIZE);
    int page = 1;

    if (totalPages == 0) {
      ctx.reply("§cYou don't have permission to see any commands.");
      return;
    }

    if (!args.isEmpty()) {
      try {
        page = Math.max(1, Math.min(totalPages, Integer.parseInt(args.get(0))));
      } catch (NumberFormatException e) {
        page = 1;
      }
    }

    int start = (page - 1) * PAGE_SIZE;
    int end = Math.min(start + PAGE_SIZE, available.size());

    StringBuilder sb = new StringBuilder();
    sb.append("§e--- Help (").append(page).append("/").append(totalPages).append(") ---\n");

    for (int i = start; i < end; i++) {
      Command cmd = available.get(i);
      sb.append("§6/")
          .append(cmd.getName())
          .append(" §7")
          .append(cmd.getDescription())
          .append("\n");
    }

    if (page < totalPages) {
      sb.append("§eType §6/help ").append(page + 1).append(" §efor the next page.");
    } else if (totalPages > 1) {
      sb.append("§eEnd of help list.");
    }

    ctx.reply(sb.toString());
  }

  private boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  @Override
  public String[] getAliases() {
    return new String[] {"?"};
  }

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public String getDescription() {
    return "Lists all available commands and their usage.";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_HELP;
  }
}
