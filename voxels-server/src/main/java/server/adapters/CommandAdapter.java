package server.adapters;

import java.util.Collection;

import server.commands.Command;
import server.commands.CommandRegistry;
import server.gateways.CommandGateway;

public class CommandAdapter implements CommandGateway {

  private final CommandRegistry registry;

  public CommandAdapter(CommandRegistry registry) {
    this.registry = registry;
  }

  @Override
  public Command getCommand(String name) {
    return registry.get(name);
  }

  @Override
  public Collection<Command> getCommands() {
    return registry.getCommands();
  }
}
