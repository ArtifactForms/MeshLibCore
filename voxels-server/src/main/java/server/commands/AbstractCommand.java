package server.commands;

public abstract class AbstractCommand implements Command {

  private static final CommandArgument[] EMPTY_ARGUMENT_LABELS = new CommandArgument[] {};

  private static final String[] EMPTY_ALIASES = new String[] {};

  @Override
  public abstract void execute(CommandContext ctx);

  @Override
  public abstract String getName();

  @Override
  public abstract String getDescription();

  @Override
  public String getSyntax() {
    //    StringBuilder builder = new StringBuilder();
    //    builder.append(getName());
    //    for (CommandArgument arg : getArgumentLabels()) {
    //      builder.append(" <");
    //      builder.append(arg.getName());
    //      builder.append(">");
    //    }
    //    return builder.toString();
    return "";
  }

  @Override
  public String getUsage() {
    StringBuilder builder = new StringBuilder();

    // Syntax
    builder.append("/");
    builder.append(getSyntax());

    // Description (optional)
    String desc = getDescription();
    if (desc != null && !desc.isEmpty()) {
      builder.append(" - ");
      builder.append(desc);
    }

    return builder.toString();
  }

  @Override
  public boolean hasAliases() {
    return getAliases().length > 0;
  }

  @Override
  public String[] getAliases() {
    return EMPTY_ALIASES;
  }

  @Override
  public CommandArgument[] getArgumentLabels() {
    return EMPTY_ARGUMENT_LABELS;
  }
}
