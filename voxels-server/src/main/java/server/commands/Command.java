package server.commands;

public interface Command {

  void execute(CommandContext ctx);

  String getName();

  String getUsage();

  String getDescription();

  String getPermission();

  String[] getArgumentLabels();

  String getSyntax();

  String[] getAliases();

  boolean hasAliases();
}
