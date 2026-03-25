package server.gateways;

import java.util.UUID;

import common.player.ability.AbilityContainer;
import common.player.attribute.AttributeContainer;

public interface PlayerGateway {

  AttributeContainer getAttributes(UUID playerId);

  AbilityContainer getAbilities(UUID playerId);
}
