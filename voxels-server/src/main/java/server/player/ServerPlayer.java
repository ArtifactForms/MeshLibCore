package server.player;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import common.game.ItemStack;
import common.network.packets.ChatMessagePacket;
import common.network.packets.ChunkDataPacket;
import common.network.packets.PlayerPositionPacket;
import common.network.packets.TitlePacket;
import common.player.PlayerData;
import common.player.PlayerProperties;
import common.player.ability.GameModePresets;
import common.world.ChunkData;
import common.world.World;
import server.network.PlayerManager;
import server.network.ServerConnection;

/**
 * Represents a player on the server side. Manages player state, inventory, and chunk streaming
 * (loading/unloading).
 */
public class ServerPlayer extends PlayerData {

  private int lastChunkX = Integer.MAX_VALUE;
  private int lastChunkZ = Integer.MAX_VALUE;

  private final ServerConnection connection;

  private final PlayerProperties properties = new PlayerProperties();

  /** Thread-safe set of chunks currently loaded on the client side */
  private Set<Long> loadedChunks = Collections.newSetFromMap(new ConcurrentHashMap<>());

  private Set<Long> enqueuedChunks = ConcurrentHashMap.newKeySet();

  private final java.util.Queue<long[]> chunkLoadQueue =
      new java.util.concurrent.LinkedBlockingQueue<>();

  private int viewDistance = 12;
  private boolean ignoreNextMovement = false;

  private int inventoryVersion = 0;
  private ItemStack cursorStack;

  public ServerPlayer(UUID uuid, String name, ServerConnection connection) {
    super(uuid, name);
    this.connection = connection;

    GameModePresets.applyCreative(getAbilities());
    GameModePresets.applyCreative(getAttributes());
  }

  public void teleport(float x, float y, float z, float yaw, float pitch) {
    //    ignoreNextMovement = true;
    setSilentPosition(x, y, z, yaw, pitch);

    // Send correction packet to the client
    connection.send(
        new PlayerPositionPacket(uuid, position.x, position.y, position.z, yaw, pitch, true));

    // Inform other players of this movement
    broadcastUpdate();
  }

  /**
   * Forced movement (Hard Correction). Updates the server-side position and ALWAYS sends a packet
   * back to the client to sync their position (useful for anti-cheat or teleportation).
   */
  public void move(float x, float y, float z, float yaw, float pitch) {
    //    if (ignoreNextMovement) {
    //      ignoreNextMovement = false;
    //      return;
    //    }

    setSilentPosition(x, y, z, yaw, pitch);

    // Send correction packet to the client
    connection.send(
        new PlayerPositionPacket(getUuid(), position.x, position.y, position.z, yaw, pitch));

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
    int currentChunkX = getChunkX();
    int currentChunkZ = getChunkZ();

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
    int pChunkX = getChunkX();
    int pChunkZ = getChunkZ();

    java.util.List<long[]> chunksToLoad = new java.util.ArrayList<>();

    // 1. Identify chunks that need to be loaded
    for (int dx = -viewDistance; dx <= viewDistance; dx++) {
      for (int dz = -viewDistance; dz <= viewDistance; dz++) {
        // Circular view distance check
        if (dx * dx + dz * dz > viewDistance * viewDistance) continue;

        int cx = pChunkX + dx;
        int cz = pChunkZ + dz;
        long key = World.getChunkKey(cx, cz);

        if (!loadedChunks.contains(key) && !enqueuedChunks.contains(key)) {
          chunksToLoad.add(new long[] {cx, cz, (long) (dx * dx + dz * dz)});
          enqueuedChunks.add(key);
        }
      }
    }

    // 2. Sort by distance (load closest chunks first)
    chunksToLoad.sort(java.util.Comparator.comparingLong(a -> a[2]));

    // 3. NEU: In die Warteschlange einreihen statt sofort senden
    for (long[] chunkInfo : chunksToLoad) {
      chunkLoadQueue.add(chunkInfo);
    }

    //  // 3. Send chunk data to client
    //  for (long[] chunkInfo : chunksToLoad) {
    //    int cx = (int) chunkInfo[0];
    //    int cz = (int) chunkInfo[1];
    //    long key = World.getChunkKey(cx, cz);
    //
    //    ChunkData data = connection.getServer().getWorld().getOrCreateChunk(cx, cz);
    //    connection.send(new ChunkDataPacket(data));
    //    loadedChunks.add(key);
    //  }

    // 4. Unload chunks that are out of range (using a small buffer to prevent flickering)
    int unloadDistanceSq = (viewDistance + 2) * (viewDistance + 2);
    //    loadedChunks.removeIf(
    //        key -> {
    //          int cx = World.unpackChunkX(key);
    //          int cz = World.unpackChunkZ(key);
    //          int dx = cx - pChunkX;
    //          int dz = cz - pChunkZ;
    //          return (dx * dx + dz * dz > unloadDistanceSq);
    //        });
    loadedChunks.removeIf(
        key -> { // TODO Send packet
          int cx = World.unpackChunkX(key);
          int cz = World.unpackChunkZ(key);

          int dx = cx - pChunkX;
          int dz = cz - pChunkZ;

          boolean shouldUnload = (dx * dx + dz * dz > unloadDistanceSq);

          if (shouldUnload) {
            //            connection.send(new ChunkUnloadPacket(cx, cz));
          }

          return shouldUnload;
        });
  }

  public void processStreaming() {
    int chunksThisTick = 4; // FIXME Max 3-4 Chunks pro Tick pro Spieler

    int pChunkX = getChunkX();
    int pChunkZ = getChunkZ();
    int maxDistSq = viewDistance * viewDistance;

    while (chunksThisTick > 0 && !chunkLoadQueue.isEmpty()) {

      long[] chunkInfo = chunkLoadQueue.poll();
      int cx = (int) chunkInfo[0];
      int cz = (int) chunkInfo[1];

      int dx = cx - pChunkX;
      int dz = cz - pChunkZ;

      // ❗ Skip wenn nicht mehr relevant
      if (dx * dx + dz * dz > maxDistSq) {
        enqueuedChunks.remove(World.getChunkKey(cx, cz));
        continue;
      }

      long key = World.getChunkKey(cx, cz);
      enqueuedChunks.remove(key);

      ChunkData data = connection.getServer().getWorld().getOrCreateChunk(cx, cz);
      connection.send(new ChunkDataPacket(data));
      loadedChunks.add(key);

      chunksThisTick--;
    }
  }

  /** Broadcasts the player's current state to other nearby players. */
  private void broadcastUpdate() {
    // Wir erstellen das Paket mit der aktuellen Position und Rotation
    // Wichtig: Hier wieder dein Y-Fix, falls der Client -Y erwartet!
    PlayerPositionPacket packet =
        new PlayerPositionPacket(uuid, position.x, position.y, position.z, yaw, pitch);

    // Wir senden es an alle, außer an uns selbst
    PlayerManager playerManager = connection.getServer().getPlayerManager();
    for (ServerPlayer other : playerManager.getAllPlayers()) {
      if (!other.getUuid().equals(this.uuid)) {
        other.connection.send(packet);
      }
    }
  }

  public void sendTitle(
      String title, String subtitle, int faceInTicks, int stayTicks, int fadeOutTicks) {
    TitlePacket packet = new TitlePacket(title, subtitle, faceInTicks, stayTicks, fadeOutTicks);
    connection.send(packet);
  }

  public void sendMessage(String message) {
    ChatMessagePacket packet = new ChatMessagePacket(message);
    connection.send(packet);
  }

  // --- Getters / Setters ---

  public Set<Long> getLoadedChunks() {
    return loadedChunks;
  }

  public Set<Long> getEnqueuedChunks() {
    return enqueuedChunks;
  }

  public ServerConnection getConnection() {
    return connection;
  }

  public PlayerProperties getProperties() {
    return properties;
  }

  public ItemStack getCursorStack() {
    return cursorStack;
  }

  public void setCursorStack(ItemStack stack) {
    this.cursorStack = stack;
  }

  public int getInventoryVersion() {
    return inventoryVersion;
  }

  public void incrementInventoryVersion() {
    inventoryVersion++;
  }
}
