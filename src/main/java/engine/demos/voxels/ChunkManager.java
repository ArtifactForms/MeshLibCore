package engine.demos.voxels;

import java.util.HashMap;
import java.util.Map;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import math.Color;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.modifier.SnapToGroundModifier;
import workspace.ui.Graphics;

public class ChunkManager extends AbstractComponent implements RenderableComponent {

  private static final int RENDER_DISTANCE = 16; // Visible range of chunks
  private static final int BUFFER_DISTANCE =
      RENDER_DISTANCE + 2; // Preemptive loading range beyond visible chunks

  private int lastPlayerChunkX = Integer.MIN_VALUE;
  private int lastPlayerChunkZ = Integer.MIN_VALUE;

  private boolean debugVisualsEnabled = false;
  private Mesh3D debugBox = new BoxCreator(16, 16, 16).create();

  private Player player;
  private Vector3f playerPosition;
  private HashMap<Long, Chunk> activeChunks;

  public ChunkManager(Player player) {
    debugBox.apply(new SnapToGroundModifier());
    this.player = player;
    this.playerPosition = new Vector3f(0, 0, 0);
    this.activeChunks = new HashMap<>();
    loadChunksAroundPlayer();
  }

  @Override
  public void render(Graphics g) {
    debugRenderActiveChunks(g);
    renderChunks(g);
  }

  private void renderChunks(Graphics g) {
    for (Chunk chunk : activeChunks.values()) {
      if (isWithinRenderDistance(chunk)) {
        chunk.render(g);
      }
    }
  }

  @Override
  public void onUpdate(float tpf) {
    playerPosition.set(player.getPosition());
    loadChunksAroundPlayer();
    updateChunks();
  }

  //  private void updateChunks() {
  //    for (Chunk chunk : activeChunks.values()) {
  //      if (isWithinRenderDistance(chunk)) {
  //        chunk.updateMesh();
  //      }
  //    }
  //  }

  private void updateChunks() {
    // Ensure data is ready for all active chunks
    for (Chunk chunk : activeChunks.values()) {
      if (!chunk.isDataReady()) {
        chunk.generateData();
      }
    }

    // Mesh generation for chunks with ready data
    for (Chunk chunk : activeChunks.values()) {
      //      if (chunk.isDataReady() && !chunk.isMeshReady()) {
      //        chunk.scheduleMeshGeneration(this);
      //      }
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
    int playerChunkX = (int) Math.floor(playerPosition.x / Chunk.CHUNK_SIZE);
    int playerChunkZ = (int) Math.floor(playerPosition.z / Chunk.CHUNK_SIZE);

    if (playerChunkX == lastPlayerChunkX && playerChunkZ == lastPlayerChunkZ) {
      return; // No need to update if the player hasn't moved to a new chunk
    }

    // Load new chunks within the BUFFER_DISTANCE
    Map<Long, Chunk> newChunks = new HashMap<>();
    for (int x = -BUFFER_DISTANCE; x <= BUFFER_DISTANCE; x++) {
      for (int z = -BUFFER_DISTANCE; z <= BUFFER_DISTANCE; z++) {
        int chunkX = playerChunkX + x;
        int chunkZ = playerChunkZ + z;
        long chunkKey = toChunkKey(chunkX, chunkZ);

        if (!activeChunks.containsKey(chunkKey)) {
          Vector3f chunkPos = new Vector3f(chunkX * Chunk.CHUNK_SIZE, 0, chunkZ * Chunk.CHUNK_SIZE);
          Chunk chunk = new Chunk(chunkPos);
          //          chunk.scheduleMeshGeneration(this);
          newChunks.put(chunkKey, chunk);
        } else {
          newChunks.put(chunkKey, activeChunks.get(chunkKey));
        }
      }
    }

    //    // Remove chunks outside the BUFFER_DISTANCE
    //    for (Long chunkKey : activeChunks.keySet()) {
    //      if (!newChunks.containsKey(chunkKey)) {
    //        Chunk chunk = activeChunks.get(chunkKey);
    //        // Optionally clean up resources for removed chunks
    //      }
    //    }

    activeChunks.clear();
    activeChunks.putAll(newChunks);

    lastPlayerChunkX = playerChunkX;
    lastPlayerChunkZ = playerChunkZ;
  }

  private boolean isWithinRenderDistance(Chunk chunk) {
    int chunkX = chunk.getChunkX();
    int chunkZ = chunk.getChunkZ();

    int playerChunkX = (int) Math.floor(playerPosition.x / Chunk.CHUNK_SIZE);
    int playerChunkZ = (int) Math.floor(playerPosition.z / Chunk.CHUNK_SIZE);

    int dx = Math.abs(chunkX - playerChunkX);
    int dz = Math.abs(chunkZ - playerChunkZ);

    return dx <= RENDER_DISTANCE && dz <= RENDER_DISTANCE;
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

  @Override
  public void onAttach() {
    // Initialize if necessary
  }

  @Override
  public void onDetach() {
    // Clean up if necessary
  }
}
