package parse;

import java.util.ArrayList;
import java.util.List;

import server.commands.Command;

public abstract class AbstractCommandFlagParser implements FlagParseListener {

  private boolean invalid;

  private Command command;

  private FlagParser parser;

  private List<String> invalidArguments;

  public AbstractCommandFlagParser(Command command) {
    this.command = command;
    invalidArguments = new ArrayList<String>();
    parser = new FlagParser();
    parser.addListener(this);
    addFlagsToParser();
  }

  private void addFlagsToParser() {
    for (Flag flag : command.getFlags()) {
      parser.addFlag(flag);
    }
  }

  @Override
  public void onInvalidArguments(List<String> invalidArguments) {
    this.invalidArguments.addAll(invalidArguments);
    invalid = true;
  }

  public void parse(List<String> arguments) {
    int from = command.getArgumentLabels().length;
    int to = arguments.size();
    parser.parseArguments(arguments.subList(from, to).toArray(new String[0]));
  }

  public boolean isInvalid() {
    return invalid;
  }

  public String[] getInvalidArguments() {
    return invalidArguments.toArray(new String[0]);
  }
}
