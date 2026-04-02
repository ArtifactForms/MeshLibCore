package server.commands;

import server.gateways.GatewayContext;

public interface CommandProvider {

  void registerCommands(CommandRegistry registry, GatewayContext ctx);
}
