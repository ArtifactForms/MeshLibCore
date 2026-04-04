package client.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import client.app.GameClient;
import client.rendering.BasicChunkRenderer;
import client.rendering.ChunkRenderer;
import client.settings.GameSettings;
import common.world.ChunkData;
import common.world.ChunkStatus;
import common.world.World;
import common.world.WorldMath;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import math.Vector3f;

/**
 * Manages the lifecycle of Chunks on the client side. Optimized with time-budgeted queue processing
 * for stable performance.
 */
public class ChunkManager extends AbstractComponent implements RenderableComponent {

  private int renderDistance;

  private int bufferDistance;

  private int lastPlayerChunkX = Integer.MIN_VALUE;

  private int lastPlayerChunkZ = Integer.MIN_VALUE;

  private final ConcurrentLinkedDeque<Chunk> chunkPool = new ConcurrentLinkedDeque<>();

  private final Map<Long, Chunk> activeChunks = new ConcurrentHashMap<>();

  private final ConcurrentLinkedQueue<Chunk> dataQueue = new ConcurrentLinkedQueue<>();

  private final ConcurrentLinkedQueue<Chunk> meshQueue = new ConcurrentLinkedQueue<>();

  private final ConcurrentHashMap<Chunk, Boolean> dataQueueSet = new ConcurrentHashMap<>();

  private final ConcurrentHashMap<Chunk, Boolean> meshQueueSet = new ConcurrentHashMap<>();

  private int playerChunkX;

  private int playerChunkZ;

  private volatile Vector3f playerPosition = new Vector3f();

  private ChunkRenderer chunkRenderer;

  private ClientWorld world;

  // Füge eine Map für den Lösch-Timer hinzu
  private final Map<Long, Long> deletionQueue = new ConcurrentHashMap<>();
  private static final long DELETION_DELAY_MS = 2000; // 2 Sekunden Puffer

  public ChunkManager(GameClient client) {
    this.world = client.getWorld();
    this.chunkRenderer = new BasicChunkRenderer(client);
    setRenderDistance(GameSettings.renderDistance);
  }

  @Override
  public void onUpdate(float tpf) {
    world.processIncomingPackets(2_000_000);

    playerChunkX = WorldMath.worldToChunkX(playerPosition);
    playerChunkZ = WorldMath.worldToChunkZ(playerPosition);

    if (playerChunkX != lastPlayerChunkX || playerChunkZ != lastPlayerChunkZ) {

      lastPlayerChunkX = playerChunkX;
      lastPlayerChunkZ = playerChunkZ;

      updateChunksAroundPlayer();
    }

    enqueueChunks();
    processQueues();
  }

  private void enqueueChunks() {
    List<Chunk> toMesh = new ArrayList<>();

    for (Chunk chunk : activeChunks.values()) {
      if (!chunk.isDataReady() && dataQueueSet.putIfAbsent(chunk, true) == null) {
        dataQueue.offer(chunk);
      }
      if (chunk.isDataReady()
          && (chunk.getStatus() == ChunkStatus.DATA_READY || chunk.needsRebuild())) {
        if (!meshQueueSet.containsKey(chunk)) {
          toMesh.add(chunk);
        }
      }
    }

    toMesh.sort(
        (a, b) -> {
          float distA = a.getWorldPosition().distanceSquared(playerPosition);
          float distB = b.getWorldPosition().distanceSquared(playerPosition);
          return Float.compare(distA, distB);
        });

    for (Chunk c : toMesh) {
      if (meshQueueSet.putIfAbsent(c, true) == null) {
        meshQueue.offer(c);
      }
    }
  }

  private void processQueues() {
    long startTime = System.nanoTime();
    long budget = 2_000_000;

    while (!meshQueue.isEmpty() && (System.nanoTime() - startTime) < budget) {
      Chunk chunk = meshQueue.poll();
      if (chunk == null) continue;

      meshQueueSet.remove(chunk);

      // Sicherstellen, dass der Chunk noch Teil der Welt ist
      if (!activeChunks.containsKey(World.getChunkKey(chunk.getChunkX(), chunk.getChunkZ()))) {
        continue;
      }

      // Der Chunk braucht ein Update
      if (chunk.needsRebuild() || chunk.getStatus() == ChunkStatus.DATA_READY) {
        // Nur wenn Nachbarn da sind, sonst zurück in die Queue (mit Verzögerung)
        if (neighborsReady(chunk.getChunkX(), chunk.getChunkZ())) {
          chunk.scheduleMeshGeneration(this); // Startet Worker-Thread
        } else {
          // Erneut hinten anstellen, damit andere Chunks erst dran kommen
          meshQueue.offer(chunk);
          meshQueueSet.put(chunk, true);
          continue;
        }
      }

      // Mesh-Resultate vom Worker-Thread einsammeln
      chunk.updateMesh();
    }
  }

  public boolean neighborsReady(int cx, int cz) {
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    for (int[] dir : directions) {
      Chunk neighbor = getChunk(cx + dir[0], cz + dir[1]);
      if (neighbor == null || !neighbor.isDataReady()) {
        return false;
      }
    }
    return true;
  }

  public void notifyNeighborsOfDataReady(int cx, int cz) {
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    for (int[] dir : directions) {
      Chunk neighbor = getChunk(cx + dir[0], cz + dir[1]);
      if (neighbor != null && neighbor.isDataReady()) {
        neighbor.markDirty();
      }
    }
  }

  public Chunk getOrCreateChunk(int cx, int cz) {
    long key = World.getChunkKey(cx, cz);
    Chunk c = activeChunks.get(key);
    if (c != null) return c;

    c = chunkPool.isEmpty() ? new Chunk(cx, cz) : chunkPool.pop();
    c.setupForPooling(cx, cz);
    activeChunks.put(key, c);
    return c;
  }

  private void updateChunksAroundPlayer() {
    int r = bufferDistance;
    for (int x = -r; x <= r; x++) {
      for (int z = -r; z <= r; z++) {
        if (x * x + z * z > r * r) continue;
        getOrCreateChunk(playerChunkX + x, playerChunkZ + z);
      }
    }
    recycleChunksOutsideRadius(playerChunkX, playerChunkZ);
  }

  private void recycleChunksOutsideRadius(int centerX, int centerZ) {
    int r2 = (bufferDistance + 1) * (bufferDistance + 1);
    long currentTime = System.currentTimeMillis();

    activeChunks
        .entrySet()
        .removeIf(
            entry -> {
              Chunk chunk = entry.getValue();
              int dx = chunk.getChunkX() - centerX;
              int dz = chunk.getChunkZ() - centerZ;

              if (dx * dx + dz * dz > r2) {
                long key = entry.getKey();
                // Wenn noch nicht in der DeletionQueue, jetzt hinzufügen
                if (!deletionQueue.containsKey(key)) {
                  deletionQueue.put(key, currentTime);
                } else if (currentTime - deletionQueue.get(key) > DELETION_DELAY_MS) {
                  // Erst nach Ablauf des Timers wirklich recyceln
                  recycleChunk(chunk);
                  deletionQueue.remove(key);
                  return true;
                }
              } else {
                // Falls der Spieler zurückläuft, aus der DeletionQueue entfernen
                deletionQueue.remove(World.getChunkKey(chunk.getChunkX(), chunk.getChunkZ()));
              }
              return false;
            });
  }

  private void recycleChunk(Chunk chunk) {
    dataQueue.remove(chunk);
    meshQueue.remove(chunk);
    dataQueueSet.remove(chunk);
    meshQueueSet.remove(chunk);
    chunkPool.push(chunk);
  }

  @Override
  public void render(Graphics g) {
    chunkRenderer.renderChunks(g, activeChunks.values());
  }

  public void setBlockAt(int x, int y, int z, short id) {
    int cx = Math.floorDiv(x, 16);
    int cz = Math.floorDiv(z, 16);
    Chunk c = getChunk(cx, cz);

    if (c != null) {
      c.setBlockId(id, Math.floorMod(x, 16), y, Math.floorMod(z, 16));
      c.markDirty();

      int lx = Math.floorMod(x, 16);
      int lz = Math.floorMod(z, 16);
      if (lx == 0) markChunkDirty(cx - 1, cz);
      if (lx == 15) markChunkDirty(cx + 1, cz);
      if (lz == 0) markChunkDirty(cx, cz - 1);
      if (lz == 15) markChunkDirty(cx, cz + 1);
    }
  }

  public void markChunkDirty(int cx, int cz) {
    Chunk c = getChunk(cx, cz);
    if (c != null) c.markDirty();
  }

  public Chunk getChunk(int x, int z) {
    return activeChunks.get(World.getChunkKey(x, z));
  }

  public float getTerrainHeight(float x, float z) {
    int cx = (int) Math.floor(x / 16.0);
    int cz = (int) Math.floor(z / 16.0);

    Chunk c = getChunk(cx, cz);
    if (c == null || !c.isDataReady()) return 0;

    int lx = Math.floorMod((int) Math.floor(x), 16);
    int lz = Math.floorMod((int) Math.floor(z), 16);

    for (int y = ChunkData.HEIGHT - 1; y >= 0; y--) {
      if (c.getBlockId(lx, y, lz) != 0) return y + 1.0f;
    }
    return 0;
  }

  public boolean isWithinRenderDistance(Chunk chunk) {
    int dx = chunk.getChunkX() - playerChunkX;
    int dz = chunk.getChunkZ() - playerChunkZ;
    return dx * dx + dz * dz <= renderDistance * renderDistance;
  }

  public void setRenderDistance(int renderDistance) {
    this.renderDistance = renderDistance;
    this.bufferDistance = renderDistance + 1;
  }

  public void forceRebuild() {
    for (Chunk chunk : activeChunks.values()) {
      chunk.markDirty(); // Sets needsRebuild = true
    }
  }

  public void updatePlayerPosition(float x, float y, float z) {
    playerPosition.set(x, y, z);
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
