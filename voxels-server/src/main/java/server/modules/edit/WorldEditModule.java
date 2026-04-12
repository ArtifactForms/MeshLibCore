package server.modules.edit;

import server.commands.CommandRegistry;
import server.events.EventBus;
import server.gateways.GatewayContext;
import server.modules.Module;

public class WorldEditModule implements Module {

  private boolean enabled;

  @Override
  public void registerCommands(CommandRegistry registry, GatewayContext ctx) {
    WorldEditConfig config = new BaseWorldEditConfiig();

    registry.register(new CircleCommand(ctx.players(), config));
    registry.register(new SphereCommand(ctx.players(), config));
    registry.register(new DiscCommand(ctx.players(), config));
  }

  @Override
  public void registerEvents(EventBus events, GatewayContext context) {
    // Currently no registration needed
  }

  @Override
  public void onEnable() {
    this.enabled = true;
  }

  @Override
  public void onDisable() {
    this.enabled = false;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
