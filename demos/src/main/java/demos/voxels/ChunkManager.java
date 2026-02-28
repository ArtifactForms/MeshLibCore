package demos.voxels;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

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

	private Stack<Chunk> chunkPool;
	private Map<Long, Chunk> activeChunks = new ConcurrentHashMap<>();

	private int recycledChunks;

	private World world;

	// 🔥 Controlled chunk processing
	private final Queue<Chunk> dataQueue = new ArrayDeque<>();
	private final Queue<Chunk> meshQueue = new ArrayDeque<>();

	private static final int MAX_DATA_PER_FRAME = 10;
	private static final int MAX_MESH_PER_FRAME = 10;

	public ChunkManager(Player player) {
		this.debugBox = new BoxCreator(16, 16, 16).create();
		new SnapToGroundModifier().modify(debugBox);

		this.player = player;
		this.playerPosition = new Vector3f(0, 0, 0);
		this.activeChunks = new HashMap<>();
		this.chunkPool = new Stack<>();

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

	@Override
	public void onUpdate(float tpf) {
		playerPosition.set(player.getPosition());
		loadChunksAroundPlayer();
		updateChunks();
	}

	// ===============================
	// 🔥 ASYNC DATA GENERATION
	// ===============================

	private void updateChunks() {

		// 1️⃣ Schedule async data generation
		for (Chunk chunk : activeChunks.values()) {
			if (!chunk.isDataReady()) {
				chunk.scheduleDataGeneration(world);
			}

			chunk.updateData(); // check if async finished
		}

		// 2️⃣ Mesh generation (unchanged)
		for (Chunk chunk : activeChunks.values()) {
			if (chunk.isDataReady() && isWithinRenderDistance(chunk)) {
				chunk.scheduleMeshGeneration(this);
			}

			chunk.updateMesh();
		}
	}

//	private void updateChunks() {
//
//	    int playerChunkX = getPlayerChunkX();
//	    int playerChunkZ = getPlayerChunkZ();
//
//	    // Queue missing chunks
//	    for (Chunk chunk : activeChunks.values()) {
//
//	        if (!chunk.isDataReady() && !dataQueue.contains(chunk)) {
//	            dataQueue.offer(chunk);
//	        }
//
//	        if (chunk.isDataReady()
//	                && isWithinRenderDistance(chunk)
//	                && !meshQueue.contains(chunk)) {
//	            meshQueue.offer(chunk);
//	        }
//	    }
//
//	    // DATA generation (FIFO)
//	    for (int i = 0; i < MAX_DATA_PER_FRAME && !dataQueue.isEmpty(); i++) {
//	        Chunk chunk = dataQueue.poll();
//
//	        if (!activeChunks.containsValue(chunk))
//	            continue;
//
//	        chunk.scheduleDataGeneration(world);
//	        chunk.updateData();
//	    }
//
//	    // MESH generation (FIFO)
//	    for (int i = 0; i < MAX_MESH_PER_FRAME && !meshQueue.isEmpty(); i++) {
//	        Chunk chunk = meshQueue.poll();
//
//	        if (!activeChunks.containsValue(chunk))
//	            continue;
//
//	        chunk.scheduleMeshGeneration(this);
//	        chunk.updateMesh();
//	    }
//	}

	// ===============================
	// DEBUG
	// ===============================

	private void debugRenderActiveChunks(Graphics g) {
		if (!debugVisualsEnabled)
			return;

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

	// ===============================
	// CHUNK LOADING
	// ===============================

	private void loadChunksAroundPlayer() {

		int playerChunkX = getPlayerChunkX();
		int playerChunkZ = getPlayerChunkZ();

		if (playerChunkX == lastPlayerChunkX && playerChunkZ == lastPlayerChunkZ) {
			return;
		}

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

	// ===============================
	// HELPERS
	// ===============================

	public void setWorld(World world) {
		this.world = world;
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

	public int getPlayerChunkX() {
		return (int) Math.floor(playerPosition.x / Chunk.WIDTH);
	}

	public int getPlayerChunkZ() {
		return (int) Math.floor(playerPosition.z / Chunk.DEPTH);
	}

	@Override
	public void onAttach() {
	}

	@Override
	public void onDetach() {
	}
}