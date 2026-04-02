package server.gateways;

import java.util.UUID;

import common.game.GameMode;
import common.player.ability.AbilityContainer;
import common.player.attribute.AttributeContainer;

public interface PlayerGateway {

  AttributeContainer getAttributes(UUID playerId);

  AbilityContainer getAbilities(UUID playerId);

  void setGameMode(UUID playerId, GameMode gameMode);
  
  void kick(String name);
  
  String getName(UUID playerId);
}
