package client.world;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import client.app.ApplicationContext;
import client.app.GameSettings;
import common.world.ChunkStatus;
import common.world.World;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import math.Vector3f;

/**
 * Manages the lifecycle of Chunks on the client side. Handles automatic loading/unloading based on
 * player position, asynchronous mesh generation, and object pooling to minimize Garbage Collection.
 */
public class ChunkManager extends AbstractComponent implements RenderableComponent {

  private int renderDistance;
  private int bufferDistance;

  private int lastPlayerChunkX = Integer.MIN_VALUE;
  private int lastPlayerChunkZ = Integer.MIN_VALUE;

  /** Object pool for reused Chunk instances to prevent memory fragmentation */
  private final ConcurrentLinkedDeque<Chunk> chunkPool = new ConcurrentLinkedDeque<>();
  /** Registry of currently active chunks mapped by their world coordinates */
  private final Map<Long, Chunk> activeChunks = new ConcurrentHashMap<>();

  /** Queues for asynchronous processing */
  private final ConcurrentLinkedQueue<Chunk> dataQueue = new ConcurrentLinkedQueue<>();

  private final ConcurrentLinkedQueue<Chunk> meshQueue = new ConcurrentLinkedQueue<>();

  /** Sets used to prevent duplicate entries in the queues */
  private final ConcurrentHashMap<Chunk, Boolean> dataQueueSet = new ConcurrentHashMap<>();

  private final ConcurrentHashMap<Chunk, Boolean> meshQueueSet = new ConcurrentHashMap<>();

  /** Performance caps per frame to maintain stable FPS */
  private static final int MAX_DATA_PER_FRAME = 30;

  private static final int MAX_MESH_PER_FRAME = 10;

  private int playerChunkX;
  private int playerChunkZ;

  public ChunkManager() {
    setRenderDistance(GameSettings.renderDistance);
  }

  @Override
  public void onUpdate(float tpf) {
    Vector3f pos = ApplicationContext.clientMovementConsumer.getPosition();

    // Calculate current player chunk coordinates
    playerChunkX = (int) Math.floor(pos.x / 16.0);
    playerChunkZ = (int) Math.floor(pos.z / 16.0);

    // Update world loading only if player crossed a chunk boundary
    if (playerChunkX != lastPlayerChunkX || playerChunkZ != lastPlayerChunkZ) {
      updateChunksAroundPlayer();
      lastPlayerChunkX = playerChunkX;
      lastPlayerChunkZ = playerChunkZ;
    }

    enqueueChunks();
    processQueues();
  }

  /** Identifies chunks that need data or a mesh rebuild and adds them to the work queues. */
  private void enqueueChunks() {
    for (Chunk chunk : activeChunks.values()) {
      // Stage 1: Queue for data if not ready
      if (!chunk.isDataReady() && dataQueueSet.putIfAbsent(chunk, true) == null) {
        dataQueue.offer(chunk);
      }

      // Stage 2: Queue for mesh rebuild if data is ready but mesh is dirty
      if (chunk.isDataReady()
          && (chunk.getStatus() == ChunkStatus.DATA_READY || chunk.needsRebuild())) {
        if (isWithinRenderDistance(chunk)) {
          if (meshQueueSet.putIfAbsent(chunk, true) == null) {
            meshQueue.offer(chunk);
          }
        }
      }
    }
  }

  /** Processes a limited number of chunks from the queues each frame. */
  private void processQueues() {
    // 1. DATA PROCESSING: Check if network data has arrived
    for (int i = 0; i < MAX_DATA_PER_FRAME && !dataQueue.isEmpty(); i++) {
      Chunk chunk = dataQueue.poll();
      if (chunk == null) continue;
      dataQueueSet.remove(chunk);
      // Logic for re-requesting missing data would go here
    }

    // 2. MESH GENERATION: Trigger asynchronous geometry calculation
    for (int i = 0; i < MAX_MESH_PER_FRAME && !meshQueue.isEmpty(); i++) {
      Chunk chunk = meshQueue.poll();
      if (chunk == null) continue;
      meshQueueSet.remove(chunk);

      if (chunk.isDataReady()) {
        chunk.scheduleMeshGeneration(this);
      }
    }

    // 3. GPU UPLOAD: Transfer completed meshes from RAM to VRAM
    for (Chunk chunk : activeChunks.values()) {
      if (chunk.getStatus() == ChunkStatus.MESH_GENERATING) {
        chunk.updateMesh();
      }
    }
  }

  private void updateChunksAroundPlayer() {
    loadChunksInRadius(playerChunkX, playerChunkZ);
    recycleChunksOutsideRadius(playerChunkX, playerChunkZ);
  }

  private void loadChunksInRadius(int centerX, int centerZ) {
    int r = bufferDistance;
    for (int x = -r; x <= r; x++) {
      for (int z = -r; z <= r; z++) {
        if (x * x + z * z > r * r) continue;

        int cx = centerX + x;
        int cz = centerZ + z;
        getOrCreateChunk(cx, cz);
      }
    }
  }

  /** Retrieves a chunk from the active map or reuses one from the pool. */
  public Chunk getOrCreateChunk(int cx, int cz) {
    long key = World.getChunkKey(cx, cz);
    Chunk c = activeChunks.get(key);
    if (c != null) return c;

    c = chunkPool.isEmpty() ? new Chunk(cx, cz) : chunkPool.pop();
    c.setupForPooling(cx, cz);
    activeChunks.put(key, c);
    return c;
  }

  private void recycleChunksOutsideRadius(int centerX, int centerZ) {
    int r2 = bufferDistance * bufferDistance;
    activeChunks
        .values()
        .removeIf(
            chunk -> {
              int dx = chunk.getChunkX() - centerX;
              int dz = chunk.getChunkZ() - centerZ;
              if (dx * dx + dz * dz > r2) {
                recycleChunk(chunk);
                return true;
              }
              return false;
            });
  }

  /** Removes chunk from all active queues and returns it to the pool for later reuse. */
  private void recycleChunk(Chunk chunk) {
    dataQueue.remove(chunk);
    meshQueue.remove(chunk);
    dataQueueSet.remove(chunk);
    meshQueueSet.remove(chunk);
    chunkPool.push(chunk);
  }

  @Override
  public void render(Graphics g) {
    g.enableFaceCulling();
    for (Chunk chunk : activeChunks.values()) {
      if (isWithinRenderDistance(chunk)) {
        chunk.render(g);
      }
    }
    g.disableFaceCulling();
  }

  /** Modifies a block in the world and marks the affected chunk as dirty for a mesh rebuild. */
  public void setBlockAt(int x, int y, int z, short id) {
    int cx = (int) Math.floor(x / 16.0);
    int cz = (int) Math.floor(z / 16.0);
    Chunk c = getChunk(cx, cz);
    if (c != null) {
      c.setBlockId(id, Math.floorMod(x, 16), y, Math.floorMod(z, 16));
      c.markDirty();
    }
  }

  /**
   * Calculates the highest solid block at specific world coordinates. Used for client-side player
   * snapping.
   */
  public float getTerrainHeight(float x, float z) {
    int cx = (int) Math.floor(x / 16.0);
    int cz = (int) Math.floor(z / 16.0);
    Chunk c = getChunk(cx, cz);
    if (c == null || !c.isDataReady()) return 0;

    int lx = Math.floorMod((int) Math.floor(x), 16);
    int lz = Math.floorMod((int) Math.floor(z), 16);

    for (int y = Chunk.HEIGHT - 1; y >= 0; y--) {
      if (c.getBlockId(lx, y, lz) != 0) return y + 1.0f;
    }
    return 0;
  }

  public void markChunkDirty(int cx, int cz) {
    Chunk c = getChunk(cx, cz);
    if (c != null) c.markDirty();
  }

  public Chunk getChunk(int x, int z) {
    return activeChunks.get(World.getChunkKey(x, z));
  }

  private boolean isWithinRenderDistance(Chunk chunk) {
    int dx = chunk.getChunkX() - playerChunkX;
    int dz = chunk.getChunkZ() - playerChunkZ;
    return dx * dx + dz * dz <= renderDistance * renderDistance;
  }

  public void setRenderDistance(int renderDistance) {
    this.renderDistance = renderDistance;
    this.bufferDistance = renderDistance + 1;
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
