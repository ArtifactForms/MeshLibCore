package server.adapters;

import java.util.Collection;
import java.util.UUID;

import common.game.GameMode;
import common.player.ability.AbilityContainer;
import common.player.attribute.AttributeContainer;
import common.world.Location;
import server.gateways.PlayerGateway;
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

  @Override
  public void kick(UUID playerId, String reason) {
    ServerPlayer player = playerManager.getPlayer(playerId);
    if (player != null) {
      playerManager.kick(player, reason);
    }
  }

  @Override
  public String getName(UUID playerId) {
    ServerPlayer player = playerManager.getPlayer(playerId);
    if (player != null) {
      return player.getName();
    }
    return null;
  }

  @Override
  public UUID getPlayerIdByName(String name) {
    ServerPlayer player = playerManager.getPlayerByName(name);
    if (player != null) {
      return player.getUuid();
    }
    return null;
  }

  @Override
  public Collection<UUID> getOnlinePlayers() {
    return playerManager.getAllPlayers().stream().map(ServerPlayer::getUuid).toList();
  }

  @Override
  public Location getLocation(UUID playerId) {
    ServerPlayer player = playerManager.getPlayer(playerId);
    if (player == null) {
      return null;
    }
    Location location =
        new Location(
            player.getX(), player.getY(), player.getZ(), player.getPitch(), player.getYaw());
    return location;
  }

  @Override
  public void teleport(UUID playerId, Location location) {
    ServerPlayer player = playerManager.getPlayer(playerId);
    player.teleport(
        (float) location.getX(),
        (float) location.getY(),
        (float) location.getZ(),
        location.getYaw(),
        location.getPitch());
  }
}
