package client.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import common.game.block.BlockType;
import engine.rendering.Graphics;
import math.Vector3f;

public class ClientEntityManager {
  private static final Map<Long, ClientItemEntity> items = new ConcurrentHashMap<>();

  public static void spawnItem(
      long id, BlockType type, float x, float y, float z, float velX, float velY, float velZ) {
    ClientItemEntity item =
        new ClientItemEntity(id, type, new Vector3f(x, y, z), new Vector3f(velX, velY, velZ));
    items.put(id, item);
  }

  public static void removeItem(long id) {
    items.remove(id);
  }

  public static void renderAll(Graphics g) {
    for (ClientItemEntity item : items.values()) {
      item.update();
      item.render(g);
    }
  }
}
