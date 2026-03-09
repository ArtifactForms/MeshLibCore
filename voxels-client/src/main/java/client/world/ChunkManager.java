package client.world;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import client.app.ApplicationContext;
import client.app.GameSettings;
import common.world.ChunkData;
import common.world.ChunkStatus;
import common.world.World;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import math.Vector3f;

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

  private static final int MAX_DATA_PER_FRAME = 40;
  private static final int MAX_MESH_PER_FRAME = 12;

  private int playerChunkX;
  private int playerChunkZ;

  public ChunkManager() {
    setRenderDistance(GameSettings.renderDistance);
  }

  @Override
  public void onUpdate(float tpf) {
    Vector3f pos = ApplicationContext.clientMovementConsumer.getPosition();

    playerChunkX = (int) Math.floor(pos.x / 16.0);
    playerChunkZ = (int) Math.floor(pos.z / 16.0);

    if (playerChunkX != lastPlayerChunkX || playerChunkZ != lastPlayerChunkZ) {
      updateChunksAroundPlayer();
      lastPlayerChunkX = playerChunkX;
      lastPlayerChunkZ = playerChunkZ;
    }

    enqueueChunks();
    processQueues();
  }

  private void enqueueChunks() {
    for (Chunk chunk : activeChunks.values()) {
      if (!chunk.isDataReady() && dataQueueSet.putIfAbsent(chunk, true) == null) {
        dataQueue.offer(chunk);
      }
      if (chunk.isDataReady()
          && (chunk.getStatus() == ChunkStatus.DATA_READY || chunk.needsRebuild())) {
        if (meshQueueSet.putIfAbsent(chunk, true) == null) {
          meshQueue.offer(chunk);
        }
      }
    }
  }

  private void processQueues() {
    for (int i = 0; i < MAX_DATA_PER_FRAME && !dataQueue.isEmpty(); i++) {
      Chunk chunk = dataQueue.poll();
      if (chunk == null) continue;
      dataQueueSet.remove(chunk);
    }

    for (int i = 0; i < MAX_MESH_PER_FRAME && !meshQueue.isEmpty(); i++) {
      Chunk chunk = meshQueue.poll();
      if (chunk == null) continue;
      meshQueueSet.remove(chunk);

      if (chunk.isDataReady()) {
        chunk.scheduleMeshGeneration(this);
      }
    }

    for (Chunk chunk : activeChunks.values()) {
      if (chunk.getStatus() == ChunkStatus.MESH_GENERATING) {
        chunk.updateMesh();
      }
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

    for (int y = ChunkData.HEIGHT; y >= 0; y--) {
      if (c.getBlockId(lx, y, lz) != 0) return y + 1.0f;
    }
    return 0;
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
