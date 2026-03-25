package server.usecases.changegamemode;

import java.util.UUID;

import common.game.GameMode;
import common.player.ability.AbilityContainer;
import common.player.ability.GameModePresets;
import common.player.attribute.AttributeContainer;
import server.events.events.ChangeGameModeEvent;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.gateways.PlayerGateway;

public class ChangeGameModeUseCase implements ChangeGamemode {

  private EventGateway eventGateway;
  private PlayerGateway playerGateway;

  public ChangeGameModeUseCase(GatewayContext context) {
    eventGateway = context.events();
    playerGateway = context.players();
  }

  @Override
  public boolean execute(UUID playerId, GameMode gameMode) {

    ChangeGameModeEvent event = new ChangeGameModeEvent(playerId, gameMode);
    eventGateway.fire(event);

    if (event.isCancelled()) {
      return false;
    }

    AbilityContainer abilities = playerGateway.getAbilities(playerId);
    AttributeContainer attributes = playerGateway.getAttributes(playerId);

    if (gameMode == GameMode.CREATIVE) {
      GameModePresets.applyCreative(abilities);
      GameModePresets.applyCreative(attributes);
      return true;
    }

    if (gameMode == GameMode.SURVIVAL) {
      GameModePresets.applySurvival(abilities);
      GameModePresets.applySurvival(attributes);
      return true;
    }

    return false;
  }
}
