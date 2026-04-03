package server.gateways;

import java.util.Collection;
import java.util.UUID;

import common.game.GameMode;
import common.player.ability.AbilityContainer;
import common.player.attribute.AttributeContainer;
import common.world.Location;

public interface PlayerGateway {

  AttributeContainer getAttributes(UUID playerId);

  AbilityContainer getAbilities(UUID playerId);

  void setGameMode(UUID playerId, GameMode gameMode);

  void kick(UUID playerId, String reason);

  String getName(UUID playerId);

  UUID getPlayerIdByName(String name);

  Collection<UUID> getOnlinePlayers();

  Location getLocation(UUID playerId);

  void teleport(UUID playerId, Location location);
}
