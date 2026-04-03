package server.modules.whitelist;

import server.commands.CommandRegistry;
import server.events.EventBus;
import server.events.events.PlayerPreJoinEvent;
import server.gateways.GatewayContext;
import server.modules.Module;

public class WhitelistModule implements Module {

  private boolean enabled;

  private final WhitelistService whitelist;

  public WhitelistModule(WhitelistService whitelist) {
    this.whitelist = whitelist;
  }

  @Override
  public void registerCommands(CommandRegistry registry, GatewayContext context) {
    registry.register(new WhitelistCommand());
  }

  @Override
  public void registerEvents(EventBus events, GatewayContext context) {
    events.register(PlayerPreJoinEvent.class, e -> onPlayerPreJoinEvent(e));
  }

  private void onPlayerPreJoinEvent(PlayerPreJoinEvent e) {
    if (e.isCancelled()) return;
    if (!isEnabled()) return;
  }

  @Override
  public void onEnable() {
    enabled = true;
  }

  @Override
  public void onDisable() {
    enabled = false;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
