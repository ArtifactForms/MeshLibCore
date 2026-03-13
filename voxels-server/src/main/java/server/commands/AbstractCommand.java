package server.commands;

import parse.Flag;

public abstract class AbstractCommand implements Command {

  @Override
  public String getSyntax() {
    StringBuilder builder = new StringBuilder();
    builder.append(getName());
    for (String arg : getArgumentLabels()) {
      builder.append(" <");
      builder.append(arg);
      builder.append(">");
    }
    builder.append(getFlagsAsString());
    return builder.toString();
  }

  private String getFlagsAsString() {
    if (!allowsFlags()) return "";
    StringBuilder builder = new StringBuilder();
    for (Flag flag : getFlags()) {
      builder.append(" [");
      builder.append(flag.getName());
      builder.append("]");
    }
    return builder.toString();
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public boolean allowsFlags() {
    return getFlags().length > 0;
  }

  @Override
  public String getPermission() {
    return "";
  }

  @Override
  public Flag[] getFlags() {
    return new Flag[] {};
  }

  @Override
  public boolean hasAliases() {
    return getAliases().length > 0;
  }

  @Override
  public String[] getAliases() {
    return new String[] {};
  }
}
