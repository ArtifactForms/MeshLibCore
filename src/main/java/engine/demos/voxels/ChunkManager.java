package engine.demos.voxels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.scene.Scene;
import math.Color;
import math.Vector2f;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.modifier.SnapToGroundModifier;
import workspace.ui.Graphics;

public class ChunkManager extends AbstractComponent implements RenderableComponent {

  private int renderDistance; // Visible range of chunks
  private int bufferDistance; // Preemptive loading range beyond visible chunks

  private int lastPlayerChunkX = Integer.MIN_VALUE;
  private int lastPlayerChunkZ = Integer.MIN_VALUE;

  private boolean debugVisualsEnabled = false;
  private Mesh3D debugBox;

  private Scene scene;
  private Player player;
  private Vector3f playerPosition;

  private Stack<Chunk> chunkPool;
  private Map<Long, Chunk> activeChunks = new ConcurrentHashMap<>();

  private int recycledChunks;

  private ChunkGenerator chunkGenerator = new ChunkGenerator3(0);
  //  private ChunkGenerator chunkGenerator = new CaveChunkGenerator();
  //  private ChunkGenerator chunkGenerator = new ChunkGenerator2();

  public ChunkManager(Scene scene, Player player) {
    this.debugBox = new BoxCreator(16, 16, 16).create();
    this.debugBox.apply(new SnapToGroundModifier());
    this.scene = scene;
    this.player = player;
    this.playerPosition = new Vector3f(0, 0, 0);
    this.activeChunks = new HashMap<>();
    this.chunkPool = new Stack<Chunk>();
    setRenderDistance(GameSettings.renderDistance);
    loadChunksAroundPlayer();
  }

  @Override
  public void render(Graphics g) {
    debugRenderActiveChunks(g);
    renderChunks(g);
  }

  private void renderChunks(Graphics g) {
    g.enableFaceCulling();
    for (Chunk chunk : activeChunks.values()) {
      if (isWithinRenderDistance(chunk)) {
        chunk.render(g);
      }
    }
    g.disableFaceCulling();
  }

  //  private void renderChunks(Graphics g) {
  //      g.enableFaceCulling();
  //
  //      // Collect and sort chunks by their XZ order
  //      List<Chunk> sortedChunks = activeChunks.values().stream()
  //          .sorted((chunk1, chunk2) -> {
  //              int dx1 = chunk1.getChunkX() - getPlayerChunkX();
  //              int dz1 = chunk1.getChunkZ() - getPlayerChunkZ();
  //              int dx2 = chunk2.getChunkX() - getPlayerChunkX();
  //              int dz2 = chunk2.getChunkZ() - getPlayerChunkZ();
  //
  //              // Sort by Z first, then X for XZ ordering
  //              if (dz1 != dz2) {
  //                  return Integer.compare(dz1, dz2);
  //              }
  //              return Integer.compare(dx1, dx2);
  //          })
  //          .collect(Collectors.toList()); // Use Collectors.toList() for compatibility
  //
  //      // Render chunks after sorting
  //      for (Chunk chunk : sortedChunks) {
  //          if (isWithinRenderDistance(chunk)) {
  //              chunk.render(g);
  //          }
  //      }
  //
  //      g.disableFaceCulling();
  //  }

  //  private void renderChunks(Graphics g) {
  //    g.enableFaceCulling();
  //
  //    // Sort chunks based on distance from the camera (farthest first)
  //    List<Chunk> sortedChunks =
  //        activeChunks
  //            .values()
  //            .stream()
  //            .filter(this::isWithinRenderDistance) // Only consider chunks within the render
  // distance
  //            .sorted(
  //                (chunk1, chunk2) -> {
  ////                  float dist1 = chunk1.getPosition().distanceSquared(playerPosition);
  ////                  float dist2 = chunk2.getPosition().distanceSquared(playerPosition);
  //
  ////                  Vector3f c1 = chunk1.getPosition();
  ////                  Vector3f c2 = chunk2.getPosition();
  //                  Vector2f pos = new Vector2f(getPlayerChunkX(), getPlayerChunkZ());
  //                  Vector2f c10 = new Vector2f(chunk1.getChunkX(), chunk1.getChunkZ());
  //                  Vector2f c20 = new Vector2f(chunk1.getChunkX(), chunk1.getChunkZ());
  //
  //                  float dist1 = c10.distanceSquared(pos);
  //                  float dist2 = c20.distanceSquared(pos);
  //
  //                  return Float.compare(dist2, dist1); // Sort by descending distance
  //                })
  //            .collect(Collectors.toList());
  //
  //    // Render chunks in sorted order
  //    for (Chunk chunk : sortedChunks) {
  //      chunk.render(g); // Let the chunk handle rendering, including water blocks
  //    }
  //
  //    g.disableFaceCulling();
  //  }

  @Override
  public void onUpdate(float tpf) {
    playerPosition.set(player.getPosition());
    loadChunksAroundPlayer();
    updateChunks();
  }

  private void updateChunks() {
    // TODO: We can improve performance further by threading this too
    // Ensure data is ready for all active chunks
    for (Chunk chunk : activeChunks.values()) {
      if (!chunk.isDataReady()) {
        chunk.generateData(chunkGenerator);
      }
    }

    // Mesh generation for chunks within render distance and ready data
    for (Chunk chunk : activeChunks.values()) {
      if (chunk.isDataReady() && isWithinRenderDistance(chunk)) {
        chunk.scheduleMeshGeneration(this);
      }
      chunk.updateMesh();
    }
  }

  private void debugRenderActiveChunks(Graphics g) {
    if (!debugVisualsEnabled) {
      return;
    }

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

  private void loadChunksAroundPlayer() {
    int playerChunkX = getPlayerChunkX();
    int playerChunkZ = getPlayerChunkZ();

    // No need to update if the player hasn't moved to a new chunk
    if (playerChunkX == lastPlayerChunkX && playerChunkZ == lastPlayerChunkZ) {
      return;
    }

    // Load new chunks within the buffer distance
    Map<Long, Chunk> newChunks = new HashMap<>();
    for (int x = -bufferDistance; x <= bufferDistance; x++) {
      for (int z = -bufferDistance; z <= bufferDistance; z++) {
        int chunkX = playerChunkX + x;
        int chunkZ = playerChunkZ + z;
        long chunkKey = toChunkKey(chunkX, chunkZ);

        if (!activeChunks.containsKey(chunkKey)) {
          Vector3f chunkPos = new Vector3f(chunkX * Chunk.WIDTH, 0, chunkZ * Chunk.DEPTH);
          Chunk chunk;

          if (chunkPool.isEmpty()) {
            chunk = new Chunk(chunkPos);
          } else {
            chunk = chunkPool.pop();
            chunk.setPosition(chunkPos);
            recycledChunks++;
          }

          newChunks.put(chunkKey, chunk);
        } else {
          newChunks.put(chunkKey, activeChunks.get(chunkKey));
        }
      }
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
        chunk.setupForPooling();
        chunkPool.add(chunk);
      }
    }
  }

  private boolean isWithinRenderDistance(Chunk chunk) {
    int playerChunkX = getPlayerChunkX();
    int playerChunkZ = getPlayerChunkZ();

    int dx = Math.abs(chunk.getChunkX() - playerChunkX);
    int dz = Math.abs(chunk.getChunkZ() - playerChunkZ);

    return dx <= renderDistance && dz <= renderDistance;
  }

  public boolean isChunkActive(int x, int z) {
    return activeChunks.containsKey(toChunkKey(x, z));
  }

  public Chunk getChunk(int x, int z) {
    return activeChunks.get(toChunkKey(x, z));
  }

  private long toChunkKey(int x, int z) {
    return ((long) x << 32) | (z & 0xFFFFFFFFL);
  }

  public void setRenderDistance(int renderDistance) {
    this.renderDistance = renderDistance;
    this.bufferDistance = renderDistance + 2;
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

  public int getPlayerChunkX() {
    return (int) Math.floor(playerPosition.x / Chunk.WIDTH);
  }

  public int getPlayerChunkZ() {
    return (int) Math.floor(playerPosition.z / Chunk.DEPTH);
  }

  @Override
  public void onAttach() {
    // Initialize if necessary
  }

  @Override
  public void onDetach() {
    // Clean up if necessary
  }
}
