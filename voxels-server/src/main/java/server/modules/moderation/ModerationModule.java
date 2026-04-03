package server.modules.moderation;

import server.commands.CommandRegistry;
import server.events.EventBus;
import server.events.events.PlayerChatEvent;
import server.events.events.PlayerPreJoinEvent;
import server.gateways.GatewayContext;
import server.gateways.MessageGateway;
import server.modules.Module;
import server.modules.moderation.commands.BanCommand;
import server.modules.moderation.commands.MuteCommand;
import server.modules.moderation.commands.MuteListCommand;
import server.modules.moderation.commands.UnmuteCommand;

public class ModerationModule implements Module {

  private final ModerationService moderation;

  private boolean enabled;

  public ModerationModule(ModerationService moderation) {
    this.moderation = moderation;
  }

  @Override
  public void registerCommands(CommandRegistry registry, GatewayContext context) {
    registry.register(new MuteCommand(moderation, context.players()));
    registry.register(new UnmuteCommand(moderation, context.players()));
    registry.register(new MuteListCommand(moderation, context.players()));
    registry.register(new BanCommand(moderation, context.players()));
  }

  @Override
  public void registerEvents(EventBus events, GatewayContext context) {
    events.register(PlayerChatEvent.class, e -> onPlayerChatEvent(e, context.messages()));
    events.register(PlayerPreJoinEvent.class, e -> onPlayerPreJoinEvent(e));
  }

  private void onPlayerPreJoinEvent(PlayerPreJoinEvent e) {
    if (e.isCancelled()) return;
    if (!isEnabled()) return;

    Ban ban = moderation.getBan(e.getPlayerId());

    if (ban == null) {
      return;
    }

    String reason = ban.getReason();

    if (reason != null && !reason.isBlank()) {
      e.setCancelReason(ModerationMessages.YOU_ARE_BANNED_REASON + reason);
    } else {
      e.setCancelReason(ModerationMessages.YOU_ARE_BANNED);
    }
  }

  private void onPlayerChatEvent(PlayerChatEvent e, MessageGateway messages) {
    if (e.isCancelled()) return;
    if (!isEnabled()) return;

    if (moderation.isMuted(e.getPlayerId())) {
      e.setCancelled(true);
      messages.sendMessage(e.getPlayerId(), ModerationMessages.MUTED_MESSAGE);
    }
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
