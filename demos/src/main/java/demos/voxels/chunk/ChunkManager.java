package demos.voxels.chunk;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import demos.voxels.GameSettings;
import demos.voxels.Player;
import demos.voxels.world.World;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.scene.camera.Camera;
import engine.scene.camera.Frustum;
import math.Color;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.modifier.transform.SnapToGroundModifier;

public class ChunkManager extends AbstractComponent implements RenderableComponent {

  private World world;

  private int renderDistance;

  private int bufferDistance;

  private int lastPlayerChunkX = Integer.MIN_VALUE;

  private int lastPlayerChunkZ = Integer.MIN_VALUE;

  private boolean debugVisualsEnabled = false;

  private Mesh3D debugBox;

  private final Player player;

  private final Vector3f playerPosition;

  private final ConcurrentLinkedDeque<Chunk> chunkPool;

  private volatile Map<Long, Chunk> activeChunks;

  private final AtomicInteger recycledChunks = new AtomicInteger();

  private int chunksRenderedLastFrame;

  // Queue system
  private final ConcurrentLinkedQueue<Chunk> dataQueue = new ConcurrentLinkedQueue<>();

  private final ConcurrentLinkedQueue<Chunk> meshQueue = new ConcurrentLinkedQueue<>();

  // Queue tracking
  private final ConcurrentHashMap<Chunk, Boolean> dataQueueSet = new ConcurrentHashMap<>();

  private final ConcurrentHashMap<Chunk, Boolean> meshQueueSet = new ConcurrentHashMap<>();

  private static final int MAX_DATA_PER_FRAME = 30;

  private static final int MAX_MESH_PER_FRAME = 10;

  private int playerChunkX;

  private int playerChunkZ;

  private Camera camera;

  private Frustum frustum = new Frustum();

  public ChunkManager(Player player, Camera camera) {
    this.player = player;
    this.playerPosition = new Vector3f(0, 0, 0);

    this.camera = camera;

    this.debugBox = new BoxCreator(16, 16, 16).create();
    new SnapToGroundModifier().modify(debugBox);

    this.activeChunks = new ConcurrentHashMap<>();
    this.chunkPool = new ConcurrentLinkedDeque<>();

    setRenderDistance(GameSettings.renderDistance);

    loadInitialChunks();
  }

  // ============================
  // INITIAL LOAD
  // ============================
  private void loadInitialChunks() {
    playerChunkX = player.getPlayerChunkX();
    playerChunkZ = player.getPlayerChunkZ();
    loadChunksInRadius(playerChunkX, playerChunkZ);
    lastPlayerChunkX = playerChunkX;
    lastPlayerChunkZ = playerChunkZ;
  }

  @Override
  public void render(Graphics g) {
    debugRenderActiveChunks(g);
    renderChunks(g);
  }

  private void renderChunks(Graphics g) {
    chunksRenderedLastFrame = 0;
    g.enableFaceCulling();

    for (Chunk chunk : activeChunks.values()) {
      if (isWithinRenderDistance(chunk)) {
        chunk.render(g);
        chunksRenderedLastFrame++;
      }
    }

    g.disableFaceCulling();
  }

  @Override
  public void onUpdate(float tpf) {
    playerChunkX = player.getPlayerChunkX();
    playerChunkZ = player.getPlayerChunkZ();
    playerPosition.set(player.getPosition());

    updateChunksAroundPlayer();
    enqueueChunks();
    processQueues();

    // Frustum update
    //    frustum.update(camera.getViewProjectionMatrix());
  }

  // ============================
  // QUEUE SYSTEM
  // ============================
  private void enqueueChunks() {
    for (Chunk chunk : activeChunks.values()) {
      if (!chunk.isDataReady() && dataQueueSet.putIfAbsent(chunk, true) == null) {
        dataQueue.offer(chunk);
      }
      if (chunk.isDataReady()
          && isWithinRenderDistance(chunk)
          && meshQueueSet.putIfAbsent(chunk, true) == null) {
        meshQueue.offer(chunk);
      }
    }
  }

  //  private void processQueues() {
  //    // DATA
  //    for (int i = 0; i < MAX_DATA_PER_FRAME && !dataQueue.isEmpty(); i++) {
  //      Chunk chunk = dataQueue.poll();
  //      if (chunk == null) continue;
  //      dataQueueSet.remove(chunk);
  //
  //      long key = toChunkKey(chunk.getChunkX(), chunk.getChunkZ());
  //      if (!activeChunks.containsKey(key)) continue;
  //
  //      if (!chunk.isDataReady()) chunk.scheduleDataGeneration(world);
  //      chunk.updateData();
  //    }
  //
  //    // MESH
  //    for (int i = 0; i < MAX_MESH_PER_FRAME && !meshQueue.isEmpty(); i++) {
  //      Chunk chunk = meshQueue.poll();
  //      if (chunk == null) continue;
  //      meshQueueSet.remove(chunk);
  //
  //      long key = toChunkKey(chunk.getChunkX(), chunk.getChunkZ());
  //      if (!activeChunks.containsKey(key)) continue;
  //
  //      if (chunk.isDataReady()) chunk.scheduleMeshGeneration(this);
  //      chunk.updateMesh();
  //    }
  //  }

  private void processQueues() {
    // 1. DATA GENERATION (Bleibt limitiert, da CPU-lastig)
    for (int i = 0; i < MAX_DATA_PER_FRAME && !dataQueue.isEmpty(); i++) {
      Chunk chunk = dataQueue.poll();
      if (chunk == null) continue;
      dataQueueSet.remove(chunk);

      long key = toChunkKey(chunk.getChunkX(), chunk.getChunkZ());
      if (!activeChunks.containsKey(key)) continue;

      if (!chunk.isDataReady()) chunk.scheduleDataGeneration(world);
      chunk.updateData();
    }

    // 2. MESH GENERATION START (Hier fangen wir an zu arbeiten)
    for (int i = 0; i < MAX_MESH_PER_FRAME && !meshQueue.isEmpty(); i++) {
      Chunk chunk = meshQueue.poll();
      if (chunk == null) continue;
      meshQueueSet.remove(chunk);

      long key = toChunkKey(chunk.getChunkX(), chunk.getChunkZ());
      if (!activeChunks.containsKey(key)) continue;

      // Startet den Thread, falls noch nicht geschehen
      if (chunk.isDataReady()) chunk.scheduleMeshGeneration(this);
    }

    // 3. FAST TRACK: Alle Chunks, deren Mesh-Thread fertig ist, SOFORT auf die GPU schieben
    // Das ignoriert das "10 pro Frame" Limit, weil Future.isDone() fast kostenlos ist.
    for (Chunk chunk : activeChunks.values()) {
      if (chunk.getStatus() == ChunkStatus.MESH_GENERATING) {
        chunk.updateMesh();
      }
    }
  }

  public void forceImmediateMesh(Chunk chunk) {
    if (chunk == null) return;

    chunk.markDirty();
    // 1. Thread starten
    chunk.scheduleMeshGeneration(this);

    // 2. WICHTIG: Wir zwingen diesen spezifischen Chunk,
    // sofort im nächsten Frame (oder sogar jetzt) fertig zu werden,
    // ohne dass er in der Mesh-Queue warten muss.
    chunk.updateMesh();
  }

  // ============================
  // DEBUG
  // ============================
  private void debugRenderActiveChunks(Graphics g) {
    if (!debugVisualsEnabled) return;
    for (Chunk chunk : activeChunks.values()) debugRenderChunk(g, chunk);
  }

  private void debugRenderChunk(Graphics g, Chunk chunk) {
    Vector3f position = chunk.getPosition();
    Color color = isWithinRenderDistance(chunk) ? Color.YELLOW : Color.RED;

    g.pushMatrix();
    g.translate(position.x, position.y, position.z);
    g.setColor(color);
    g.drawFaces(debugBox);
    g.popMatrix();
  }

  // ============================
  // CHUNK LOADING
  // ============================
  private void updateChunksAroundPlayer() {
    int dx = playerChunkX - lastPlayerChunkX;
    int dz = playerChunkZ - lastPlayerChunkZ;

    if (dx == 0 && dz == 0) return;

    loadChunksInRadius(playerChunkX, playerChunkZ);

    recycleChunksOutsideRadius(playerChunkX, playerChunkZ);

    lastPlayerChunkX = playerChunkX;
    lastPlayerChunkZ = playerChunkZ;
  }

  private void loadChunksInRadius(int centerX, int centerZ) {
    int r = bufferDistance;
    int r2 = r * r;

    for (int x = -r; x <= r; x++) {
      for (int z = -r; z <= r; z++) {
        if (x * x + z * z > r2) continue;

        int chunkX = centerX + x;
        int chunkZ = centerZ + z;
        long key = toChunkKey(chunkX, chunkZ);

        if (activeChunks.containsKey(key)) continue;

        Vector3f chunkPos = new Vector3f(chunkX * Chunk.WIDTH, 0, chunkZ * Chunk.DEPTH);
        Chunk chunk = chunkPool.isEmpty() ? new Chunk() : chunkPool.pop();
        chunk.setPosition(chunkPos);
        activeChunks.put(key, chunk);
        recycledChunks.getAndIncrement();
      }
    }
  }

  private void recycleChunksOutsideRadius(int centerX, int centerZ) {
    int r2 = bufferDistance * bufferDistance;

    for (Chunk chunk : activeChunks.values().toArray(new Chunk[0])) {
      int dx = chunk.getChunkX() - centerX;
      int dz = chunk.getChunkZ() - centerZ;
      if (dx * dx + dz * dz > r2) recycleChunk(chunk);
    }
  }

  private void recycleChunk(Chunk chunk) {
    long key = toChunkKey(chunk.getChunkX(), chunk.getChunkZ());
    activeChunks.remove(key);
    dataQueue.remove(chunk);
    meshQueue.remove(chunk);
    dataQueueSet.remove(chunk);
    meshQueueSet.remove(chunk);
    chunk.setupForPooling();
    chunkPool.push(chunk);
    recycledChunks.getAndIncrement();
  }

  // ============================
  // HELPERS
  // ============================
  public void setWorld(World world) {
    this.world = world;
  }

  private boolean isWithinRenderDistance(Chunk chunk) {
    int dx = Math.abs(chunk.getChunkX() - playerChunkX);
    int dz = Math.abs(chunk.getChunkZ() - playerChunkZ);
    return dx * dx + dz * dz <= renderDistance * renderDistance;
  }

  public boolean isChunkActive(int x, int z) {
    return activeChunks.containsKey(toChunkKey(x, z));
  }

  public Chunk getChunk(int x, int z) {
    return activeChunks.get(toChunkKey(x, z));
  }

  private long toChunkKey(int x, int z) {
    return ChunkCoordinate.toLong(x, z);
  }

  public void setRenderDistance(int renderDistance) {
    this.renderDistance = renderDistance;
    this.bufferDistance = renderDistance + 1;
  }

  public int getChunkPoolSize() {
    return chunkPool.size();
  }

  public int getActiveChunkCount() {
    return activeChunks.size();
  }

  public int getRenderDistance() {
    return renderDistance;
  }

  public AtomicInteger getRecycledChunksCount() {
    return recycledChunks;
  }

  public int getChunksRenderedLastFrame() {
    return chunksRenderedLastFrame;
  }
}
