package parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlagParser {

  private List<Flag> flags;
  private HashMap<String, Flag> flagMap;
  private List<FlagParseListener> listeners;

  public FlagParser() {
    flags = new ArrayList<Flag>();
    flagMap = new HashMap<String, Flag>();
    listeners = new ArrayList<FlagParseListener>();
  }

  public void parseArguments(String[] arguments) {
    List<String> argumentsList = new ArrayList<String>();

    for (String argument : arguments) {
      argumentsList.add(argument);
      for (Flag flag : flags) {
        if (argument.startsWith(flag.getName()) && !flag.matchesFlagPattern(argument)) {
          continue;
        }
        if (flag.matchesFlagPattern(argument)) {
          if (flag.parseArgument(argument)) {
            flagMap.put(flag.getName(), flag);
            fireFlagParsed(flag);
            argumentsList.remove(argument);
          }
        }
      }
    }

    if (!argumentsList.isEmpty()) fireInvalidArguments(argumentsList);
  }

  private void fireFlagParsed(Flag flag) {
    for (FlagParseListener listener : listeners) listener.onFlagParsed(flag);
  }

  private void fireInvalidArguments(List<String> invalidArguments) {
    for (FlagParseListener listener : listeners) {
      listener.onInvalidArguments(invalidArguments);
    }
  }

  public void addListener(FlagParseListener listener) {
    if (listener == null) return;
    listeners.add(listener);
  }

  public void addFlag(Flag flag) {
    flags.add(flag);
  }

  public boolean containsFlag(String flag) {
    return flagMap.containsKey(flag);
  }

  public Flag getFlag(String flag) {
    return flagMap.get(flag);
  }
}
