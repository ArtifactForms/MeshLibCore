package server.usecases.changegamemode;

import java.util.UUID;

import common.game.GameMode;
import common.player.ability.AbilityContainer;
import common.player.ability.GameModePresets;
import common.player.attribute.AttributeContainer;
import server.events.events.ChangeGameModeEvent;
import server.events.events.PostGameModeChangeEvent;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.gateways.PlayerGateway;

public class ChangeGameModeUseCase implements ChangeGamemode {

  private final EventGateway events;
  private final PlayerGateway players;

  public ChangeGameModeUseCase(GatewayContext context) {
    this.events = context.events();
    this.players = context.players();
  }

  @Override
  public void execute(ChangeGameModeRequest request, ChangeGameModeResponse response) {
    UUID playerId = request.getPlayerId();
    GameMode gameMode = request.getGameMode();

    ChangeGameModeEvent event = new ChangeGameModeEvent(playerId, gameMode);
    events.fire(event);

    if (event.isCancelled()) {
      response.onFailed(playerId);
      return;
    }

    AbilityContainer abilities = players.getAbilities(playerId);
    AttributeContainer attributes = players.getAttributes(playerId);

    if (!applyGameMode(gameMode, abilities, attributes)) {
      response.onFailed(playerId);
      return;
    }

    players.setGameMode(playerId, gameMode);

    events.fire(new PostGameModeChangeEvent(playerId, gameMode));
    response.onGameModeChanged(playerId, gameMode);
  }

  private boolean applyGameMode(
      GameMode gameMode, AbilityContainer abilities, AttributeContainer attributes) {

    switch (gameMode) {
      case CREATIVE:
        GameModePresets.applyCreative(abilities);
        GameModePresets.applyCreative(attributes);
        return true;

      case SURVIVAL:
        GameModePresets.applySurvival(abilities);
        GameModePresets.applySurvival(attributes);
        return true;

      default:
        return false;
    }
  }
}
