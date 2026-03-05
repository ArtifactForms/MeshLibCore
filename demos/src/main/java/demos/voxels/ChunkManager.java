package demos.voxels;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import demos.voxels.structure.ChunkCoordinate;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import math.Color;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.modifier.transform.SnapToGroundModifier;

public class ChunkManager extends AbstractComponent implements RenderableComponent {

  private int renderDistance;
  private int bufferDistance;

  private int lastPlayerChunkX = Integer.MIN_VALUE;
  private int lastPlayerChunkZ = Integer.MIN_VALUE;

  private boolean debugVisualsEnabled = false;
  private Mesh3D debugBox;

  private Player player;
  private Vector3f playerPosition;

  private ArrayDeque<Chunk> chunkPool;
  private Map<Long, Chunk> activeChunks;

  private int recycledChunks;
  private int chunksRenderedLastFrame;

  private World world;

  // Controlled chunk processing
  private final ConcurrentLinkedQueue<Chunk> dataQueue = new ConcurrentLinkedQueue<>();
  private final ConcurrentLinkedQueue<Chunk> meshQueue = new ConcurrentLinkedQueue<>();

  private static final int MAX_DATA_PER_FRAME = 30;
  private static final int MAX_MESH_PER_FRAME = 10;

  private int playerChunkX;
  private int playerChunkZ;

  public ChunkManager(Player player) {

    this.debugBox = new BoxCreator(16, 16, 16).create();
    new SnapToGroundModifier().modify(debugBox);

    this.player = player;
    this.playerPosition = new Vector3f(0, 0, 0);

    this.activeChunks = new ConcurrentHashMap<>();
    this.chunkPool = new ArrayDeque<>();

    setRenderDistance(GameSettings.renderDistance);

    loadChunksAroundPlayer();
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

    loadChunksAroundPlayer();

    enqueueChunks();

    processQueues();
  }

  // =====================================================
  // QUEUE SYSTEM
  // =====================================================

  private void enqueueChunks() {

    for (Chunk chunk : activeChunks.values()) {

      // DATA GENERATION

      if (!chunk.isDataReady() && !dataQueue.contains(chunk)) {
        dataQueue.offer(chunk);
      }

      // MESH GENERATION

      if (chunk.isDataReady() && isWithinRenderDistance(chunk) && !meshQueue.contains(chunk)) {

        meshQueue.offer(chunk);
      }
    }
  }

  private void processQueues() {

    // ===============================
    // DATA GENERATION
    // ===============================

    for (int i = 0; i < MAX_DATA_PER_FRAME && !dataQueue.isEmpty(); i++) {

      Chunk chunk = dataQueue.poll();

      if (chunk == null) continue;

      long key = toChunkKey(chunk.getChunkX(), chunk.getChunkZ());

      if (!activeChunks.containsKey(key)) continue;

      if (!chunk.isDataReady()) {
        chunk.scheduleDataGeneration(world);
      }

      chunk.updateData();
    }

    // ===============================
    // MESH GENERATION
    // ===============================

    for (int i = 0; i < MAX_MESH_PER_FRAME && !meshQueue.isEmpty(); i++) {

      Chunk chunk = meshQueue.poll();

      if (chunk == null) continue;

      long key = toChunkKey(chunk.getChunkX(), chunk.getChunkZ());

      if (!activeChunks.containsKey(key)) continue;

      if (chunk.isDataReady()) {
        chunk.scheduleMeshGeneration(this);
      }

      chunk.updateMesh();
    }
  }

  // =====================================================
  // DEBUG
  // =====================================================

  private void debugRenderActiveChunks(Graphics g) {

    if (!debugVisualsEnabled) return;

    for (Chunk chunk : activeChunks.values()) {
      debugRenderChunk(g, chunk);
    }
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

  // =====================================================
  // CHUNK LOADING (SPIRAL)
  // =====================================================

  private void loadChunksAroundPlayer() {

    if (playerChunkX == lastPlayerChunkX && playerChunkZ == lastPlayerChunkZ) {
      return;
    }

    Map<Long, Chunk> newChunks = new HashMap<>();

    int r2 = bufferDistance * bufferDistance;

    int x = 0;
    int z = 0;

    int dx = 0;
    int dz = -1;

    int max = (bufferDistance * 2 + 1) * (bufferDistance * 2 + 1);

    for (int i = 0; i < max; i++) {

      int dist2 = x * x + z * z;

      if (dist2 <= r2) {

        int chunkX = playerChunkX + x;
        int chunkZ = playerChunkZ + z;

        long key = toChunkKey(chunkX, chunkZ);

        if (!activeChunks.containsKey(key)) {

          Vector3f chunkPos = new Vector3f(chunkX * Chunk.WIDTH, 0, chunkZ * Chunk.DEPTH);

          Chunk chunk;

          if (chunkPool.isEmpty()) {

            chunk = new Chunk();
            chunk.setPosition(chunkPos);

          } else {

            chunk = chunkPool.pop();

            chunk.setPosition(chunkPos);

            recycledChunks++;
          }

          newChunks.put(key, chunk);

        } else {

          newChunks.put(key, activeChunks.get(key));
        }
      }

      if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {

        int temp = dx;

        dx = -dz;

        dz = temp;
      }

      x += dx;
      z += dz;
    }

    recycleUnusedChunks(newChunks);

    activeChunks.clear();

    activeChunks.putAll(newChunks);

    lastPlayerChunkX = playerChunkX;
    lastPlayerChunkZ = playerChunkZ;
  }

  private void recycleUnusedChunks(Map<Long, Chunk> newChunks) {

    for (Chunk chunk : activeChunks.values()) {

      long key = toChunkKey(chunk.getChunkX(), chunk.getChunkZ());

      if (!newChunks.containsKey(key)) {

        dataQueue.remove(chunk);
        meshQueue.remove(chunk);

        chunk.setupForPooling();

        chunkPool.push(chunk);
      }
    }
  }

  // =====================================================
  // HELPERS
  // =====================================================

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

  public int getRecycledChunksCount() {
    return recycledChunks;
  }

  public int getChunksRenderedLastFrame() {
    return chunksRenderedLastFrame;
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
