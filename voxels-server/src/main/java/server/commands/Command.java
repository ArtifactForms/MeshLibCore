package server.commands;

import parse.Flag;

public interface Command {

  void execute(CommandContext context);

  String getName();

  String getDescription();

  String getPermission();

  String[] getArgumentLabels();

  String getSyntax();

  String[] getAliases();

  Flag[] getFlags();

  boolean allowsFlags();

  boolean hasAliases();
}
