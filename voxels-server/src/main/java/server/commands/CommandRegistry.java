package server.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

  private Map<String, Command> commands = new HashMap<>();

  public void register(Command command) {

    commands.put(command.getName(), command);

    for (String alias : command.getAliases()) {
      commands.put(alias, command);
    }
  }

  public Command get(String name) {
    return commands.get(name);
  }
}
