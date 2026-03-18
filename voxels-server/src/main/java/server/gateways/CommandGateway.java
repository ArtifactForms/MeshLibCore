package server.gateways;

import server.commands.Command;

public interface CommandGateway {

  Command getCommand(String name);
}
