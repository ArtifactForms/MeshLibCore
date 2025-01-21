package engine.demos.voxels;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import engine.components.StaticGeometry;
import math.Mathf;
import math.PerlinNoise;
import math.Vector3f;
import workspace.ui.Graphics;

public class Chunk {

  private static final ExecutorService executorService =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  public static final int WIDTH = 16;
  public static final int DEPTH = 16;
  public static final int HEIGHT = 255;

  private int[] blockData;
  private int[] heightMap;

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

  public void generateData() {
    if (dataReady) return;
    int baseHeight = 0;
    float scale = 0.01f;
    PerlinNoise noise = new PerlinNoise(0);

    // Precompute height map for the entire chunk
    for (int x = 0; x < WIDTH; x++) {
      for (int z = 0; z < DEPTH; z++) {
        float wx = position.x + x;
        float wz = position.z + z;

        // Generate and scale noise value
        float noiseValue = (float) noise.noise(wx * scale, wz * scale);
        int heightValue = (int) Mathf.map(noiseValue, 0, 1, 0, 60) + baseHeight;

        heightMap[x + z * WIDTH] = heightValue;

        // Fill blocks based on height value
        for (int y = 0; y <= heightValue; y++) {
          if (y < 40 && y > 20) {
            setBlockAt(BlockType.GRASS, x, y, z);
          } else {
            setBlockAt(BlockType.STONE, x, y, z);
          }
        }
      }
    }

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
      return blockData[index] != BlockType.AIR.getId();
    }
  }

  public void setBlockDataAt(int data, int x, int y, int z) {
    int index = getIndex(x, y, z);
    blockData[index] = data;
  }

  public void setBlockAt(BlockType block, int x, int y, int z) {
    blockData[getIndex(x, y, z)] = block.getId();
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

  private int getIndex(int x, int y, int z) {
    return x + WIDTH * (y + HEIGHT * z);
  }

  public Vector3f getPosition() {
    return position;
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
