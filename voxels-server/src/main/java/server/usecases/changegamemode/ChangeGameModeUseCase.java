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

  private EventGateway events;

  private PlayerGateway players;

  public ChangeGameModeUseCase(GatewayContext context) {
    events = context.events();
    players = context.players();
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

    boolean changed = false;

    if (gameMode == GameMode.CREATIVE) {
      GameModePresets.applyCreative(abilities);
      GameModePresets.applyCreative(attributes);
      players.setGameMode(playerId, gameMode);
      changed = true;
    }

    if (gameMode == GameMode.SURVIVAL) {
      GameModePresets.applySurvival(abilities);
      GameModePresets.applySurvival(attributes);
      players.setGameMode(playerId, gameMode);
      changed = true;
    }

    if (changed) {
      PostGameModeChangeEvent postEvent = new PostGameModeChangeEvent(playerId, gameMode);
      events.fire(postEvent);
      response.onGameModeChanged(playerId, gameMode);
      return;
    }

    response.onFailed(playerId);
  }
}
