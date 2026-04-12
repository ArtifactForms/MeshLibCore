package server.commands;

public interface Command {

  void execute(CommandContext ctx);

  String getName();

  String getUsage();

  String getDescription();

  String getPermission();

  CommandArgument[] getArgumentLabels();

  String getSyntax();

  String[] getAliases();

  boolean hasAliases();
}
