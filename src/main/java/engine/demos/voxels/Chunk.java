package engine.demos.voxels;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import engine.components.StaticGeometry;
import math.Vector3f;
import workspace.ui.Graphics;

public class Chunk {

  private static final ExecutorService executorService =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  public static final int WIDTH = 16;
  public static final int DEPTH = 16;
  public static final int HEIGHT = 320;

  private int[] blockData;
  private int[] heightMap; // Stores max block values for y

  private Vector3f position;
  private StaticGeometry geometry;
  private Future<StaticGeometry> meshFuture;
  private boolean isMeshReady = false;
  private boolean dataReady = false;
  private boolean shedule = false;

  public Chunk(Vector3f position) {
    this.position = position;
    this.blockData = new int[WIDTH * DEPTH * HEIGHT];
    this.heightMap = new int[WIDTH * DEPTH];
  }

  public void setupForPooling() {
    Arrays.fill(blockData, BlockType.AIR.getId());
    Arrays.fill(heightMap, 0);
    isMeshReady = false;
    dataReady = false;
    shedule = false;
    meshFuture = null;
    geometry = null;
  }

  public void generateData() {
    if (dataReady) return;
    new ChunkGenerator().generate(this);
    dataReady = true;
  }

  public void scheduleMeshGeneration(ChunkManager chunkManager) {
    if (shedule) return;
    shedule = true;
    meshFuture =
        executorService.submit(
            () -> {
              ChunkMesher mesher = new ChunkMesher(this, chunkManager);
              return mesher.createMeshFromBlockData();
            });
  }

  public void updateMesh() {
    if (!isMeshReady && meshFuture != null && meshFuture.isDone()) {
      try {
        geometry = meshFuture.get();
        isMeshReady = true;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void render(Graphics g) {
    if (geometry == null) return;

    g.pushMatrix();
    g.translate(position.x, position.y, position.z);
    geometry.render(g);
    g.popMatrix();
  }

  public boolean isMeshReady() {
    return geometry != null;
  }

  public boolean isDataReady() {
    return dataReady;
  }

  public boolean isBlockSolid(int x, int y, int z) {
    int index = getIndex(x, y, z);
    if (index < 0 || index >= blockData.length) {
      return false;
    } else {
      if (blockData[index] == BlockType.AIR.getId()) return false;
      if (blockData[index] == BlockType.GRASS.getId()) return false;
      return true;
    }
  }

  public void setBlockDataAt(int data, int x, int y, int z) {
    int index = getIndex(x, y, z);
    blockData[index] = data;
  }

  public void setBlockAt(BlockType block, int x, int y, int z) {
    int index = getIndex(x, y, z);
    if (index >= blockData.length || index < 0) return; // TODO print err?
    blockData[index] = block.getId();
  }

  public int getBlockData(int x, int y, int z) {
    return blockData[getIndex(x, y, z)];
  }

  public BlockType getBlock(int x, int y, int z) {
    return BlockType.fromId(blockData[getIndex(x, y, z)]);
  }

  public int getHeightValueAt(int x, int z) {
    return heightMap[x + z * WIDTH];
  }

  public int[] getHeightMap() {
    return heightMap;
  }

  private int getIndex(int x, int y, int z) {
    return x + WIDTH * (y + HEIGHT * z);
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }

  public int getWidth() {
    return WIDTH;
  }

  public int getHeight() {
    return HEIGHT;
  }

  public int getDepth() {
    return DEPTH;
  }

  public int getChunkX() {
    return (int) position.x / WIDTH;
  }

  public int getChunkZ() {
    return (int) position.z / DEPTH;
  }
}
