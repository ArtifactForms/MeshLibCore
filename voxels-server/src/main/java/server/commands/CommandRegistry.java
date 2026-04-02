package server.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

  private Map<String, Command> uniqueCommands = new HashMap<>();

  private Map<String, Command> lookupMap = new HashMap<>();

  public void register(Command command) {
    uniqueCommands.put(command.getName(), command);

    lookupMap.put(command.getName(), command);
    for (String alias : command.getAliases()) {
      lookupMap.put(alias, command);
    }
  }

  public Command get(String name) {
    return lookupMap.get(name);
  }

  public Collection<Command> getCommands() {
    return uniqueCommands.values();
  }
}
