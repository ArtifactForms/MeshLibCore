package server.modules;

import server.commands.CommandRegistry;
import server.events.EventBus;
import server.gateways.GatewayContext;

public interface Module {

  void registerCommands(CommandRegistry registry, GatewayContext context);

  void registerEvents(EventBus events, GatewayContext context);

  void onEnable();

  void onDisable();
  
  boolean isEnabled();
}
