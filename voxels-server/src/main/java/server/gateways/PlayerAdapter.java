package server.gateways;

import java.util.UUID;

import common.game.GameMode;
import common.player.ability.AbilityContainer;
import common.player.attribute.AttributeContainer;
import server.network.PlayerManager;
import server.player.ServerPlayer;

public class PlayerAdapter implements PlayerGateway {

  private PlayerManager playerManager;

  public PlayerAdapter(PlayerManager playerManager) {
    this.playerManager = playerManager;
  }

  @Override
  public AttributeContainer getAttributes(UUID playerId) {
    ServerPlayer player = playerManager.getPlayer(playerId);
    return player.getAttributes();
  }

  @Override
  public AbilityContainer getAbilities(UUID playerId) {
    ServerPlayer player = playerManager.getPlayer(playerId);
    return player.getAbilities();
  }

  @Override
  public void setGameMode(UUID playerId, GameMode gameMode) {
    ServerPlayer player = playerManager.getPlayer(playerId);
    player.setGameMode(gameMode);
  }
}
