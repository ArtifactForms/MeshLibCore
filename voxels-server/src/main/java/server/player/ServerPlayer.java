package server.player;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import common.game.Inventory;
import common.network.packets.ChunkDataPacket;
import common.network.packets.PlayerPositionPacket;
import common.world.ChunkData;
import common.world.World;
import math.Vector3f;
import server.network.GameServer;
import server.network.ServerConnection;

/**
 * Represents a player on the server side. Manages player state, inventory, and chunk streaming
 * (loading/unloading).
 */
public class ServerPlayer {

  private int lastChunkX = Integer.MAX_VALUE;
  private int lastChunkZ = Integer.MAX_VALUE;

  private final ServerConnection connection;
  private final UUID uuid;
  private final String name;

  private Vector3f position = new Vector3f();
  private float yaw, pitch;

  /** Thread-safe set of chunks currently loaded on the client side */
  private Set<Long> loadedChunks = Collections.newSetFromMap(new ConcurrentHashMap<>());

  private int viewDistance = 12;

  private Inventory inventory;

  public ServerPlayer(UUID uuid, String name, ServerConnection connection) {
    this.uuid = uuid;
    this.name = name;
    this.connection = connection;
    this.inventory = new Inventory(4 * 9); // Standard 36 slot inventory
  }

  /**
   * Forced movement (Hard Correction). Updates the server-side position and ALWAYS sends a packet
   * back to the client to sync their position (useful for anti-cheat or teleportation).
   */
  public void move(float x, float y, float z, float yaw, float pitch) {
    setSilentPosition(x, y, z, yaw, pitch);

    // Send correction packet to the client
    connection.send(new PlayerPositionPacket(uuid, position.x, position.y, position.z, yaw, pitch));

    // Inform other players of this movement
    broadcastUpdate();
  }

  /**
   * Updates the internal position without sending a confirmation packet. Used when the client's
   * movement is accepted by the server logic.
   */
  public void setSilentPosition(float x, float y, float z, float yaw, float pitch) {
    this.position.set(x, y, z);
    this.yaw = yaw;
    this.pitch = pitch;

    // Check if the player crossed a chunk boundary
    int currentChunkX = Math.floorDiv((int) x, 16);
    int currentChunkZ = Math.floorDiv((int) z, 16);

    if (currentChunkX != lastChunkX || currentChunkZ != lastChunkZ) {
      updateStreaming();
      lastChunkX = currentChunkX;
      lastChunkZ = currentChunkZ;
    }
  }

  /**
   * Manages which chunks are sent to the client based on their view distance. Loads new chunks in
   * range and unloads those that are too far away.
   */
  public void updateStreaming() {
    int pChunkX = Math.floorDiv((int) position.x, 16);
    int pChunkZ = Math.floorDiv((int) position.z, 16);

    java.util.List<long[]> chunksToLoad = new java.util.ArrayList<>();

    // 1. Identify chunks that need to be loaded
    for (int dx = -viewDistance; dx <= viewDistance; dx++) {
      for (int dz = -viewDistance; dz <= viewDistance; dz++) {
        // Circular view distance check
        if (dx * dx + dz * dz > viewDistance * viewDistance) continue;

        int cx = pChunkX + dx;
        int cz = pChunkZ + dz;
        long key = World.getChunkKey(cx, cz);

        if (!loadedChunks.contains(key)) {
          // Store coordinates and squared distance for sorting
          chunksToLoad.add(new long[] {cx, cz, (long) (dx * dx + dz * dz)});
        }
      }
    }

    // 2. Sort by distance (load closest chunks first)
    chunksToLoad.sort(java.util.Comparator.comparingLong(a -> a[2]));

    // 3. Send chunk data to client
    for (long[] chunkInfo : chunksToLoad) {
      int cx = (int) chunkInfo[0];
      int cz = (int) chunkInfo[1];
      long key = World.getChunkKey(cx, cz);

      ChunkData data = GameServer.getWorld().getOrCreateChunk(cx, cz);
      connection.send(new ChunkDataPacket(data));
      loadedChunks.add(key);
    }

    // 4. Unload chunks that are out of range (using a small buffer to prevent flickering)
    int unloadDistanceSq = (viewDistance + 2) * (viewDistance + 2);
    loadedChunks.removeIf(
        key -> {
          int cx = World.unpackChunkX(key);
          int cz = World.unpackChunkZ(key);
          int dx = cx - pChunkX;
          int dz = cz - pChunkZ;
          return (dx * dx + dz * dz > unloadDistanceSq);
        });
  }

  /** Broadcasts the player's current state to other nearby players. */
  private void broadcastUpdate() {
    // TODO: Logic to inform OTHER players about your movement
  }

  // --- Getters ---

  public Vector3f getPosition() {
    return new Vector3f(position);
  }

  public Inventory getInventory() {
    return inventory;
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public float getX() {
    return position.x;
  }

  public float getY() {
    return position.y;
  }

  public float getZ() {
    return position.z;
  }
}
