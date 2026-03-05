package demos.voxels;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import engine.components.StaticGeometry;
import engine.rendering.Graphics;
import math.Vector3f;

public class Chunk {

  public static final int WIDTH = 16;
  public static final int DEPTH = 16;
  public static final int HEIGHT = 384;

  private static final ExecutorService executorService =
      Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors() - 1));

  private final short[] blockData;
  private final int[] heightMap;

  private Vector3f position;
  private StaticGeometry geometry;

  private Future<StaticGeometry> meshFuture;
  private Future<?> dataFuture;

  private volatile ChunkStatus status = ChunkStatus.EMPTY;

  public Chunk() {

    this.blockData = new short[WIDTH * DEPTH * HEIGHT];
    this.heightMap = new int[WIDTH * DEPTH];
  }

  // =========================================================
  // DATA GENERATION
  // =========================================================

  public void generateData(ChunkGenerator chunkGenerator) {
    chunkGenerator.generate(this);
  }

  public void scheduleDataGeneration(World world) {

    if (status != ChunkStatus.EMPTY) return;

    status = ChunkStatus.DATA_GENERATING;

    dataFuture =
        executorService.submit(
            () -> {
              world.generate(this);
            });
  }

  public void updateData() {

    if (status != ChunkStatus.DATA_GENERATING) return;

    if (dataFuture != null && dataFuture.isDone()) {

      try {

        dataFuture.get();

        status = ChunkStatus.DATA_READY;

      } catch (Exception e) {

        e.printStackTrace();
      }
    }
  }

  // =========================================================
  // MESH GENERATION
  // =========================================================

  public void scheduleMeshGeneration(ChunkManager manager) {

    if (status != ChunkStatus.DATA_READY) return;

    status = ChunkStatus.MESH_GENERATING;

    meshFuture =
        executorService.submit(
            () -> {
              ChunkMesher mesher = new ChunkMesher(this, manager);

              return mesher.createMesh();
            });
  }

  public void updateMesh() {

    if (status != ChunkStatus.MESH_GENERATING) return;

    if (meshFuture != null && meshFuture.isDone()) {

      try {

        geometry = meshFuture.get();

        status = ChunkStatus.MESH_READY;

      } catch (Exception e) {

        e.printStackTrace();
      }
    }
  }

  // =========================================================
  // POOL RESET
  // =========================================================

  public void setupForPooling() {

    if (meshFuture != null) meshFuture.cancel(true);
    if (dataFuture != null) dataFuture.cancel(true);

    Arrays.fill(blockData, BlockType.AIR.getId());
    Arrays.fill(heightMap, 0);

    meshFuture = null;
    dataFuture = null;

    geometry = null;

    status = ChunkStatus.EMPTY;
  }

  // =========================================================
  // RENDER
  // =========================================================

  public void render(Graphics g) {

    if (geometry == null) return;

    g.pushMatrix();
    g.translate(position.x, position.y, position.z);
    geometry.render(g);
    g.popMatrix();
  }

  // =========================================================
  // BLOCK ACCESS
  // =========================================================

  public short getBlockData(int x, int y, int z) {
    if (!isWithinBounds(x, y, z)) {
      return BlockType.AIR.getId();
    }

    return blockData[getIndex(x, y, z)];
  }

  public boolean isWithinBounds(int x, int y, int z) {

    return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && z >= 0 && z < DEPTH;
  }

  public boolean isBlockSolid(int x, int y, int z) {

    int index = getIndex(x, y, z);

    if (index < 0 || index >= blockData.length) return false;

    return blockData[index] != BlockType.AIR.getId();
  }

  public void setBlockAt(BlockType block, int x, int y, int z) {

    int index = getIndex(x, y, z);

    if (index < 0 || index >= blockData.length) return;

    blockData[index] = block.getId();

    int height = Math.max(y, getHeightValueAt(x, z));

    setHeightValueAt(height, x, z);
  }

  public BlockType getBlock(int x, int y, int z) {

    int index = getIndex(x, y, z);

    if (index < 0 || index >= blockData.length) return null;

    return BlockType.fromId(blockData[index]);
  }

  public short[] getBlockData() {
    return blockData;
  }

  // =========================================================
  // HEIGHTMAP
  // =========================================================

  public int getHeightValueAt(int x, int z) {

    if (x < 0 || x >= WIDTH || z < 0 || z >= DEPTH) return 0;

    return heightMap[x + z * WIDTH];
  }

  public void setHeightValueAt(int value, int x, int z) {

    if (x < 0 || x >= WIDTH || z < 0 || z >= DEPTH) return;

    heightMap[x + z * WIDTH] = value;
  }

  public int[] getHeightMap() {
    return heightMap;
  }

  // =========================================================
  // STATE HELPERS (IMPORTANT FOR CHUNKMANAGER)
  // =========================================================

  public boolean isDataReady() {
    return status == ChunkStatus.DATA_READY
        || status == ChunkStatus.MESH_GENERATING
        || status == ChunkStatus.MESH_READY;
  }

  public boolean isMeshReady() {
    return status == ChunkStatus.MESH_READY;
  }

  public boolean hasGeometry() {
    return geometry != null;
  }

  public StaticGeometry getGeometry() {
    return geometry;
  }

  // =========================================================
  // HELPERS
  // =========================================================

  private int getIndex(int x, int y, int z) {
    return x + WIDTH * (y + HEIGHT * z);
  }

  public boolean isInsideChunk(int x, int y, int z) {

    return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && z >= 0 && z < DEPTH;
  }

  // =========================================================
  // POSITION
  // =========================================================

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }

  public int getChunkX() {
    return (int) Math.floor(position.x / WIDTH);
  }

  public int getChunkZ() {
    return (int) Math.floor(position.z / DEPTH);
  }

  // =========================================================
  // STATUS
  // =========================================================

  public ChunkStatus getStatus() {
    return status;
  }
}
