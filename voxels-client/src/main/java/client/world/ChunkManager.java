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

  private static final int DEFAULT_ORIGIN_SHIFT_THRESHOLD_CHUNKS = 4;

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

  private static final int MAX_DATA_PER_FRAME = 30;
  private static final int MAX_MESH_PER_FRAME = 15;

  private int playerChunkX;
  private int playerChunkZ;
  private volatile Vector3f playerPosition = new Vector3f();
  private float worldTime;

  private ChunkRenderer chunkRenderer;

  private ClientWorld world;
  private final ClientWorldOrigin origin;
  private int originShiftThresholdChunks = DEFAULT_ORIGIN_SHIFT_THRESHOLD_CHUNKS;

  // Füge eine Map für den Lösch-Timer hinzu
  private final Map<Long, Long> deletionQueue = new ConcurrentHashMap<>();
  private static final long DELETION_DELAY_MS = 2000; // 2 Sekunden Puffer

  public ChunkManager(GameClient client) {
    this.world = client.getWorld();
    this.origin = world.getOrigin();
    this.chunkRenderer = new BasicChunkRenderer(client);
    setRenderDistance(GameSettings.renderDistance);
  }

  @Override
  public void onUpdate(float tpf) {
    worldTime += tpf * 0.1f;

    world.processIncomingPackets(2_000_000);

    playerChunkX = WorldMath.worldToChunkX(playerPosition);
    playerChunkZ = WorldMath.worldToChunkZ(playerPosition);

    if (origin.isOutsideShiftThreshold(
        playerChunkX, playerChunkZ, originShiftThresholdChunks)) {
      origin.setOriginChunk(playerChunkX, playerChunkZ);
    }

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

  //  private void processQueues() {
  //    long startTime = System.nanoTime();
  //    long budgetInNanos = 2_000_000; // 2ms budget
  //
  //    while (!meshQueue.isEmpty() && (System.nanoTime() - startTime) < budgetInNanos) {
  //      Chunk chunk = meshQueue.poll();
  //      if (chunk == null) continue;
  //
  //      meshQueueSet.remove(chunk);
  //
  //      if (!activeChunks.containsValue(chunk)) continue;
  //
  //      if ((chunk.getStatus() == ChunkStatus.DATA_READY || chunk.needsRebuild())
  //          && neighborsReady(chunk.getChunkX(), chunk.getChunkZ())) {
  //        chunk.scheduleMeshGeneration(this);
  //      }
  //
  //      chunk.updateMesh();
  //    }
  //  }

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

  //  private void recycleChunksOutsideRadius(int centerX, int centerZ) {
  //    int r2 = (bufferDistance + 1) * (bufferDistance + 1);
  //    activeChunks
  //        .values()
  //        .removeIf(
  //            chunk -> {
  //              int dx = chunk.getChunkX() - centerX;
  //              int dz = chunk.getChunkZ() - centerZ;
  //              if (dx * dx + dz * dz > r2) {
  //                recycleChunk(chunk);
  //                return true;
  //              }
  //              return false;
  //            });
  //  }

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

  //  @Override
  //  public void render(Graphics g) {
  //
  ////	  / 1. Sonnenhöhe bestimmen (geht von -1 bis 1)
  //	  float sunY = (float) Math.sin(worldTime);
  //
  //	  // 2. Einen Faktor berechnen, der weich zwischen 0 (Nacht) und 1 (Tag) gleitet
  //	  // Wir mappen sunY von [-1, 1] auf [0, 1]
  //	  float dayFactor = sunY * 0.5f + 0.5f;
  //
  //	  // 3. Farben definieren
  //	  Vector3f nightSky = new Vector3f(0.05f, 0.07f, 0.15f); // Dunkelblau
  //	  Vector3f daySky = new Vector3f(0.6f, 0.75f, 0.9f);    // Hellblau
  //
  //	  Vector3f nightSun = new Vector3f(0.2f, 0.3f, 0.5f);   // Schwaches Mondlicht
  //	  Vector3f daySun = new Vector3f(1.0f, 0.95f, 0.8f);    // Gelbe Sonne
  //
  //	  // 4. Weich mischen (Linear Interpolation / Lerp)
  //	  float r = nightSky.x + dayFactor * (daySky.x - nightSky.x);
  //	  float gr = nightSky.y + dayFactor * (daySky.y - nightSky.y);
  //	  float b = nightSky.z + dayFactor * (daySky.z - nightSky.z);
  //	  Vector3f currentSky = new Vector3f(r, gr, b);
  //
  //	  float sr = nightSun.x + dayFactor * (daySun.x - nightSun.x);
  //	  float sg = nightSun.y + dayFactor * (daySun.y - nightSun.y);
  //	  float sb = nightSun.z + dayFactor * (daySun.z - nightSun.z);
  //	  Vector3f currentSun = new Vector3f(sr, sg, sb);
  //
  //	  // 5. Umgebungslicht anpassen
  //	  float currentAmbient = 0.2f + (dayFactor * 0.4f);
  //
  //	  // 6. An Shader senden
  //	  g.setUniform("u_fogColor", currentSky);
  //	  g.setUniform("u_lightColor", currentSun);
  //	  g.setUniform("u_ambient", currentAmbient);
  //
  //    getOwner().getScene().setBackground(new Color(currentSky.x, currentSky.y, currentSky.z));
  //
  //    //    Color skyColor = Color.getColorFromInt(180, 210, 255);
  //
  //    g.setShader("voxel.vert", "voxel.frag");
  //    g.setUniform("u_fogColor", new Color(currentSky.x, currentSky.y, currentSky.z));
  //
  //    g.setUniform("u_lightDir", new Vector3f(0.2f, 1.0f, 0.4f));
  //    g.setUniform("u_lightColor", new Vector3f(0.8f, 0.8f, 0.7f)); // Etwas schwächeres Weiß
  //    g.setUniform("u_ambient", 0.5f); // Mehr Grundhelligkeit
  //
  //    float blocks = 8.0f * 16.0f;
  //
  //    float density = 1.5f / blocks;
  //    g.setUniform("u_fogDensity", density);
  //
  //    g.enableFaceCulling();
  //
  //    for (Chunk chunk : activeChunks.values()) {
  //      if (isWithinRenderDistance(chunk)) {
  //        chunk.render(g);
  //      }
  //    }
  //
  //    g.disableFaceCulling();
  //
  //    g.resetShader();
  //  }

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

  public ClientWorldOrigin getOrigin() {
    return origin;
  }

  public int getOriginShiftThresholdChunks() {
    return originShiftThresholdChunks;
  }

  public void setOriginShiftThresholdChunks(int originShiftThresholdChunks) {
    if (originShiftThresholdChunks < 0) {
      throw new IllegalArgumentException("Origin shift threshold must be >= 0.");
    }
    this.originShiftThresholdChunks = originShiftThresholdChunks;
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
