package server.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import common.entity.ItemEntity;
import common.network.packets.EntityDestroyPacket;
import common.network.packets.ItemPickupPacket;
import server.network.GameServer;
import server.player.ServerPlayer;

/**
 * Manages the lifecycle, physics, and interaction logic for all server-side entities. This class
 * handles spawning, updating, and player-item collisions.
 */
public class EntityManager {

  private final AtomicLong nextEntityId = new AtomicLong(1);

  /** Internal registry of all active ItemEntities, mapped by their unique entity ID. */
  private final Map<Long, ItemEntity> itemEntities = new ConcurrentHashMap<>();

  private final GameServer server;

  public EntityManager(GameServer server) {
    this.server = server;
  }

  /**
   * Registers a new entity into the world tracking system. * @param entity The ItemEntity to be
   * added.
   */
  public void addEntity(ItemEntity entity) {
    itemEntities.put(entity.getEntityId(), entity);
  }

  /**
   * Main simulation loop for all entities. Updates physics and checks for interactions with the
   * provided list of players. * @param players An iterable collection of currently active players.
   */
  public void update(Iterable<ServerPlayer> players) {
    for (ItemEntity item : itemEntities.values()) {
      // 1. Update Physics (Apply gravity, velocity, and collision)
      item.update();

      // 2. Check for player proximity (Pickup detection)
      for (ServerPlayer player : players) {
        if (item.isNear(player.getPosition())) {
          handlePickup(player, item);
          // Break inner loop: once an item is picked up, it cannot be checked for others
          break;
        }
      }
    }
  }

  /**
   * Processes the logic when a player is close enough to an item. Verifies inventory space before
   * removing the entity from the world. * @param player The player attempting the pickup.
   *
   * @param item The item entity being picked up.
   */
  private void handlePickup(ServerPlayer player, ItemEntity item) {
    // Attempt to add the item to the player's inventory
    // Casting the block ID to short as required by the inventory system
    boolean success = player.getInventory().addItem(item.getBlockType().getId(), 1);

    if (success) {
      // Notify clients to play pickup effects (sound/animation)
      server
          .getPlayerManager()
          .broadcast(new ItemPickupPacket(item.getEntityId(), player.getUuid()));

      // Remove the entity from server tracking and notify clients to stop rendering it
      removeEntity(item.getEntityId());

      // System log for debugging (Optional: remove for production performance)
      // System.out.println("[EntityManager] Player " + player.getName() + " collected " +
      // item.getBlockType());
    }
  }

  /**
   * Removes an entity from the server's tracking and broadcasts a destruction packet to all clients
   * to clean up their local state. * @param entityId The unique ID of the entity to remove.
   */
  public void removeEntity(long entityId) {
    if (itemEntities.remove(entityId) != null) {
      // Only broadcast if the entity was actually present to avoid redundant packets
      server.getPlayerManager().broadcast(new EntityDestroyPacket(entityId));
    }
  }

  /**
   * Provides access to the current map of item entities. * @return A map containing all active item
   * entities.
   */
  public Map<Long, ItemEntity> getItemEntities() {
    return itemEntities;
  }

  public long createEntityId() {
    return nextEntityId.getAndIncrement();
  }
}
