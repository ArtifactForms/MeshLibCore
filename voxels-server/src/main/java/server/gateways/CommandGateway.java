package server.gateways;

import java.util.Collection;

import server.commands.Command;

public interface CommandGateway {

  Command getCommand(String name);
  
  Collection<Command> getCommands();
}
