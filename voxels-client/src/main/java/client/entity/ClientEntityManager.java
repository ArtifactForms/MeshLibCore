package client.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import client.player.RemotePlayer;
import client.rendering.RemotePlayerRenderer;
import client.world.ClientWorld;
import common.game.block.BlockType;
import engine.rendering.Graphics;
import math.Vector3f;

public class ClientEntityManager {

  private static final Map<Long, ClientItemEntity> items = new ConcurrentHashMap<>();

  private final Map<UUID, RemotePlayer> remotePlayers = new HashMap<>();
  private final ClientWorld world;

  public ClientEntityManager(ClientWorld world) {
    this.world = world;
  }

  public void updateRemotePlayer(UUID uuid, float x, float y, float z, float yaw, float pitch) {
    RemotePlayer player = remotePlayers.get(uuid);

    if (player == null) {
      return;
    }

    player.setX(x);
    player.setY(y);
    player.setZ(z);
    player.setYaw(yaw);
    player.setPitch(pitch);
  }

  public void addRemotePlayer(UUID uuid, String name, float x, float y, float z) {
    if (!remotePlayers.containsKey(uuid)) {
      RemotePlayer newPlayer = new RemotePlayer(uuid);
      newPlayer.setX(x);
      newPlayer.setY(y);
      newPlayer.setZ(z);
      remotePlayers.put(uuid, newPlayer);
      System.out.println("[Client] Player spawned correctly: " + name);
    }
  }

  public void removeRemotePlayer(UUID uuid) {
    remotePlayers.remove(uuid);
    System.out.println("[Client] Player removed: " + uuid);
  }

  public void spawnItem(
      long id, BlockType type, float x, float y, float z, float velX, float velY, float velZ) {
    ClientItemEntity item =
        new ClientItemEntity(
            id, type, new Vector3f(x, y, z), new Vector3f(velX, velY, velZ), world);
    items.put(id, item);
  }

  public void removeItem(long id) {
    items.remove(id);
  }

  public void renderAll(Graphics g) {
    renderItems(g);
    renderPlayers(g);
  }

  private void renderPlayers(Graphics g) {

    for (RemotePlayer player : remotePlayers.values()) {
      renderRemotePlayer(g, player);
    }
  }

  private void renderItems(Graphics g) {
    for (ClientItemEntity item : items.values()) {
      item.update();
      item.render(g);
    }
  }

  private void renderRemotePlayer(Graphics g, RemotePlayer player) {
    RemotePlayerRenderer.render(g, player);
  }
}
