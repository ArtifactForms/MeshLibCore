package demos.voxels;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import engine.backend.processing.BufferedShape;
import engine.components.StaticGeometry;
import engine.rendering.Graphics;
import math.Vector3f;

public class Chunk {

	public static final int WIDTH = 16;
	public static final int DEPTH = 16;
	public static final int HEIGHT = 384;

	// 🔥 EIN ThreadPool für Data + Mesh (wie vorher)
	private static final ExecutorService executorService = Executors
			.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors() - 1));

	private short[] blockData;
	private int[] heightMap;

	private Vector3f position;
	private StaticGeometry geometry;

	private Future<StaticGeometry> meshFuture;
	private Future<?> dataFuture;

	private boolean isMeshReady = false;
	private volatile boolean dataReady = false;

	private boolean meshScheduled = false;
	private boolean dataScheduled = false;

	private BufferedShape shape = new BufferedShape(ChunkMesher.sharedMaterial);

	public Chunk() {
	}

	public Chunk(Vector3f position) {
		this.position = position;
		this.blockData = new short[WIDTH * DEPTH * HEIGHT];
		this.heightMap = new int[WIDTH * DEPTH];
	}

	// =========================================
	// 🔥 ASYNC DATA GENERATION
	// =========================================

	public void scheduleDataGeneration(World world) {
		if (dataScheduled)
			return;

		dataScheduled = true;

		dataFuture = executorService.submit(() -> {
			world.generate(this); // dein alter Code
			dataReady = true;
		});
	}

	public void updateData() {
		if (!dataReady && dataFuture != null && dataFuture.isDone()) {
			try {
				dataFuture.get(); // propagate exceptions
				dataReady = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void generateData(ChunkGenerator chunkGenerator) {
		Arrays.fill(blockData, BlockType.AIR.getId());
		Arrays.fill(heightMap, 0);
		chunkGenerator.generate(this);
		dataReady = true;
	}

	// =========================================
	// MESH (unverändert)
	// =========================================

	public void scheduleMeshGeneration(ChunkManager chunkManager) {
		if (meshScheduled)
			return;

		meshScheduled = true;

		meshFuture = executorService.submit(() -> {
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

	// =========================================
	// POOL RESET
	// =========================================

	public void setupForPooling() {
//		Arrays.fill(blockData, BlockType.AIR.getId());
//		Arrays.fill(heightMap, 0);

		isMeshReady = false;
		dataReady = false;

		meshScheduled = false;
		dataScheduled = false;

		meshFuture = null;
		dataFuture = null;

		geometry = null;
	}

	// =========================================
	// RENDER
	// =========================================

	public void render(Graphics g) {
		if (geometry == null)
			return;

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

	// =========================================
	// BLOCK ACCESS
	// =========================================

	public boolean isBlockSolid(int x, int y, int z) {
		int index = getIndex(x, y, z);
		if (index < 0 || index >= blockData.length)
			return false;

		short id = blockData[index];
		if (id == BlockType.AIR.getId())
			return false;
		if (id == BlockType.GRASS.getId())
			return false;

		return true;
	}

	public void setBlockDataAt(short data, int x, int y, int z) {
		int index = getIndex(x, y, z);
		blockData[index] = data;
	}

	public void setBlockAt(BlockType block, int x, int y, int z) {
		int index = getIndex(x, y, z);
		if (index >= blockData.length || index < 0)
			return;

		blockData[index] = block.getId();

		int height = Math.max(y, getHeightValueAt(x, z));
		setHeightValueAt(height, x, z);
	}

	public int getBlockData(int x, int y, int z) {
		return blockData[getIndex(x, y, z)];
	}

	public BlockType getBlock(int x, int y, int z) {
		int index = getIndex(x, y, z);
		if (index < 0 || index >= blockData.length)
			return null;
		return BlockType.fromId(blockData[index]);
	}

	public int getHeightValueAt(int x, int z) {
		if (x < 0 || x >= 16)
			return 0;
		if (z < 0 || z >= 16)
			return 0;

		int index = x + z * WIDTH;
		if (index >= heightMap.length)
			return -1;

		return heightMap[index];
	}

	public void setHeightValueAt(int value, int x, int z) {
		if (x < 0 || x >= 16)
			return;
		if (z < 0 || z >= 16)
			return;

		int index = x + z * WIDTH;
		if (index >= heightMap.length)
			return;

		heightMap[index] = value;
	}

	public int[] getHeightMap() {
		return heightMap;
	}

	private int getIndex(int x, int y, int z) {
		return x + WIDTH * (y + HEIGHT * z);
	}

	public boolean isWithinBounds(int x, int y, int z) {
		return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && z >= 0 && z < DEPTH;
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