package demos.voxels;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import engine.components.StaticGeometry;
import engine.processing.BufferedShape;
import math.Vector3f;
import workspace.ui.Graphics;

public class Chunk {

  public static final int WIDTH = 16;
  public static final int DEPTH = 16;
  public static final int HEIGHT = 384;

  private static final ExecutorService executorService =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private short[] blockData;
  private int[] heightMap; // Stores max block values for y

  private Vector3f position;
  private StaticGeometry geometry;
  private Future<StaticGeometry> meshFuture;
  private boolean isMeshReady = false;
  private boolean dataReady = false;
  private boolean shedule = false;
  private BufferedShape shape = new BufferedShape(ChunkMesher.sharedMaterial);

  public Chunk(Vector3f position) {
    this.position = position;
    this.blockData = new short[WIDTH * DEPTH * HEIGHT];
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

  public void generateData(ChunkGenerator chunkGenerator) {
    if (dataReady) return;
    chunkGenerator.generate(this);
    dataReady = true;
  }

  public void scheduleMeshGeneration(ChunkManager chunkManager) {
    if (shedule) return;
    shedule = true;
    meshFuture =
        executorService.submit(
            () -> {
              ChunkMesher mesher = new ChunkMesher(this, chunkManager);
              return mesher.createMeshFromBlockData(shape);
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

  public void setBlockDataAt(short data, int x, int y, int z) {
    int index = getIndex(x, y, z);
    blockData[index] = data;
  }

  public void setBlockAt(BlockType block, int x, int y, int z) {
    int index = getIndex(x, y, z);
    if (index >= blockData.length || index < 0) return; // TODO print err?
    blockData[index] = block.getId();
    // Update height map if needed
    int height = y > getHeightValueAt(x, z) ? y : getHeightValueAt(x, z);
    setHeightValueAt(height, x, z);
  }

  public int getBlockData(int x, int y, int z) {
    return blockData[getIndex(x, y, z)];
  }

  public BlockType getBlock(int x, int y, int z) {
    int index = getIndex(x, y, z);
    if (index < 0 || index > blockData.length) return null;
    return BlockType.fromId(blockData[getIndex(x, y, z)]);
  }
  
  public int getHeightValueAt(int x, int z) {
    if (x < 0 || x >= 16) return 0;
    if (z < 0 || z >= 16) return 0;
    int index = x + z * WIDTH;
    if (index >= heightMap.length) return -1;
    return heightMap[index];
  }

  public void setHeightValueAt(int value, int x, int z) {
    if (x < 0 || x >= 16) return;
    if (z < 0 || z >= 16) return;
    int index = x + z * WIDTH;
    if (index >= heightMap.length) return;
    heightMap[index] = value;
  }

  public int[] getHeightMap() {
    return heightMap;
  }

  private int getIndex(int x, int y, int z) {
    return x + WIDTH * (y + HEIGHT * z);
    //      return y * HEIGHT + z * WIDTH + x;
  }

  public boolean isWithinBounds(int x, int y, int z) {
    return x >= 0 && x < Chunk.WIDTH && y >= 0 && y < Chunk.HEIGHT && z >= 0 && z < Chunk.DEPTH;
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