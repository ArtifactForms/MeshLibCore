package voxels.editor;

import engine.components.AbstractComponent;
import engine.components.StaticGeometry;
import engine.rendering.Material;
import engine.scene.Scene;
import engine.scene.SceneNode;
import mesh.Mesh3D;
import voxels.render.RegionRenderSystem;
import voxels.world.BlockAccess;
import voxels.world.Chunk;
import voxels.world.NoiseTerrainGenerator;
import voxels.world.Region;
import voxels.world.VoxelWorld;

import java.util.*;
import java.util.concurrent.*;

public class WorldStreamer extends AbstractComponent {

  private final WorldAnchor anchor;
  private final VoxelWorld world;
  private final NoiseTerrainGenerator generator;
  private final RegionRenderSystem renderSystem;
  private final int chunkRadius;
  private final int unloadRadius;

  private final int maxChunkGenerationsPerFrame;
  private final int maxChunkUnloadsPerFrame;
  private final int maxRegionMeshSubmissionsPerFrame;
  private final int maxRegionMeshAppliesPerFrame;

  private final Map<Long, SceneNode> regionNodes = new HashMap<>();

  private final Deque<Long> pendingGeneration = new ArrayDeque<>();
  private final Set<Long> pendingGenerationSet = new HashSet<>();

  private final Deque<Long> pendingUnload = new ArrayDeque<>();
  private final Set<Long> pendingUnloadSet = new HashSet<>();

  private final Deque<Long> pendingDirtyRegions = new ArrayDeque<>();
  private final Set<Long> pendingDirtyRegionSet = new HashSet<>();

  private final Set<Long> inFlightRegionBuilds = new HashSet<>();
  private final Set<Long> dirtyAfterBuild = new HashSet<>();
  private final ConcurrentLinkedQueue<MeshBuildResult> completedMeshBuilds = new ConcurrentLinkedQueue<>();

  private final ExecutorService meshExecutor = Executors.newSingleThreadExecutor();

  private Scene scene;
  private int currentAnchorChunkX = Integer.MIN_VALUE;
  private int currentAnchorChunkZ = Integer.MIN_VALUE;

  public WorldStreamer(
      WorldAnchor anchor,
      VoxelWorld world,
      NoiseTerrainGenerator generator,
      RegionRenderSystem renderSystem,
      int chunkRadius,
      int unloadRadius) {
    this(anchor, world, generator, renderSystem, chunkRadius, unloadRadius, 6, 8, 1, 2);
  }

  public WorldStreamer(
      WorldAnchor anchor,
      VoxelWorld world,
      NoiseTerrainGenerator generator,
      RegionRenderSystem renderSystem,
      int chunkRadius,
      int unloadRadius,
      int maxChunkGenerationsPerFrame,
      int maxChunkUnloadsPerFrame,
      int maxRegionMeshSubmissionsPerFrame,
      int maxRegionMeshAppliesPerFrame) {
    this.anchor = anchor;
    this.world = world;
    this.generator = generator;
    this.renderSystem = renderSystem;
    this.chunkRadius = chunkRadius;
    this.unloadRadius = unloadRadius;
    this.maxChunkGenerationsPerFrame = Math.max(1, maxChunkGenerationsPerFrame);
    this.maxChunkUnloadsPerFrame = Math.max(1, maxChunkUnloadsPerFrame);
    this.maxRegionMeshSubmissionsPerFrame = Math.max(1, maxRegionMeshSubmissionsPerFrame);
    this.maxRegionMeshAppliesPerFrame = Math.max(1, maxRegionMeshAppliesPerFrame);
  }

  @Override
  public void onAttachToScene(Scene scene) {
    this.scene = scene;
    updateStreamingTargets(true);
    processQueues();
  }

  @Override
  public void onUpdate(float tpf) {
    updateStreamingTargets(false);
    processQueues();
  }

  @Override
  public void onDetach() {
    meshExecutor.shutdownNow();
  }

  private void updateStreamingTargets(boolean force) {
    int anchorChunkX = Math.floorDiv((int) Math.floor(anchor.getX()), Chunk.SIZE_X);
    int anchorChunkZ = Math.floorDiv((int) Math.floor(anchor.getZ()), Chunk.SIZE_Z);

    if (!force && anchorChunkX == currentAnchorChunkX && anchorChunkZ == currentAnchorChunkZ) {
      return;
    }

    currentAnchorChunkX = anchorChunkX;
    currentAnchorChunkZ = anchorChunkZ;

    queueMissingChunksNearAnchor(anchorChunkX, anchorChunkZ);
    queueChunksForUnload(anchorChunkX, anchorChunkZ);
  }

  private void processQueues() {
    generateQueuedChunks();
    unloadQueuedChunks();
    submitRegionMeshBuilds();
    applyCompletedRegionMeshes();
  }

  private void queueMissingChunksNearAnchor(int anchorChunkX, int anchorChunkZ) {
    for (int ring = 0; ring <= chunkRadius; ring++) {
      int minX = anchorChunkX - ring;
      int maxX = anchorChunkX + ring;
      int minZ = anchorChunkZ - ring;
      int maxZ = anchorChunkZ + ring;

      enqueueChunkForGeneration(minX, minZ);

      for (int x = minX + 1; x <= maxX; x++) {
        enqueueChunkForGeneration(x, minZ);
      }
      for (int z = minZ + 1; z <= maxZ; z++) {
        enqueueChunkForGeneration(maxX, z);
      }
      for (int x = maxX - 1; x >= minX; x--) {
        enqueueChunkForGeneration(x, maxZ);
      }
      for (int z = maxZ - 1; z > minZ; z--) {
        enqueueChunkForGeneration(minX, z);
      }
    }
  }

  private void enqueueChunkForGeneration(int chunkX, int chunkZ) {
    if (world.hasChunk(chunkX, chunkZ)) {
      return;
    }

    long chunkKey = key(chunkX, chunkZ);
    if (!pendingGenerationSet.add(chunkKey)) {
      return;
    }

    pendingGeneration.addLast(chunkKey);
  }

  private void queueChunksForUnload(int anchorChunkX, int anchorChunkZ) {
    for (Chunk chunk : world.getChunks()) {
      int chunkX = chunk.getChunkX();
      int chunkZ = chunk.getChunkZ();

      int dx = Math.abs(chunkX - anchorChunkX);
      int dz = Math.abs(chunkZ - anchorChunkZ);

      if (dx <= unloadRadius && dz <= unloadRadius) {
        continue;
      }

      long key = key(chunkX, chunkZ);
      if (pendingUnloadSet.contains(key)) {
        continue;
      }

      pendingUnload.addLast(key);
      pendingUnloadSet.add(key);
    }
  }

  private void generateQueuedChunks() {
    int generated = 0;

    while (generated < maxChunkGenerationsPerFrame && !pendingGeneration.isEmpty()) {
      long key = pendingGeneration.removeFirst();
      pendingGenerationSet.remove(key);

      int chunkX = xFromKey(key);
      int chunkZ = zFromKey(key);

      if (!isWithinRadius(chunkX, chunkZ, currentAnchorChunkX, currentAnchorChunkZ, chunkRadius)) {
        continue;
      }

      if (world.hasChunk(chunkX, chunkZ)) {
        continue;
      }

      Chunk chunk = new Chunk(chunkX, chunkZ);
      generator.generate(chunk);
      world.addChunk(chunk);

      markChunkRegionAndNeighborsDirty(chunkX, chunkZ);
      generated++;
    }
  }

  private void unloadQueuedChunks() {
    int unloaded = 0;

    while (unloaded < maxChunkUnloadsPerFrame && !pendingUnload.isEmpty()) {
      long key = pendingUnload.removeFirst();
      pendingUnloadSet.remove(key);

      int chunkX = xFromKey(key);
      int chunkZ = zFromKey(key);

      if (isWithinRadius(chunkX, chunkZ, currentAnchorChunkX, currentAnchorChunkZ, unloadRadius)) {
        continue;
      }

      Chunk removed = world.removeChunk(chunkX, chunkZ);
      if (removed == null) {
        continue;
      }

      markChunkRegionAndNeighborsDirty(chunkX, chunkZ);
      unloaded++;
    }
  }

  private void submitRegionMeshBuilds() {
    int submitted = 0;

    while (submitted < maxRegionMeshSubmissionsPerFrame && !pendingDirtyRegions.isEmpty()) {
      long regionKey = pendingDirtyRegions.removeFirst();
      pendingDirtyRegionSet.remove(regionKey);

      if (inFlightRegionBuilds.contains(regionKey)) {
        dirtyAfterBuild.add(regionKey);
        continue;
      }

      int regionX = xFromKey(regionKey);
      int regionZ = zFromKey(regionKey);
      Region liveRegion = world.getRegion(regionX, regionZ);

      if (liveRegion == null || liveRegion.isEmpty()) {
        removeRegionNode(regionKey);
        renderSystem.invalidateRegion(regionX, regionZ);
        submitted++;
        continue;
      }

      Region regionSnapshot = new Region(regionX, regionZ);
      for (Chunk chunk : liveRegion.getChunks()) {
        regionSnapshot.addChunk(chunk);
      }

      BlockAccess blockSnapshot = createBlockSnapshot(regionX, regionZ);

      inFlightRegionBuilds.add(regionKey);
      meshExecutor.submit(
          () -> {
            Mesh3D mesh = renderSystem.buildMesh(regionSnapshot, blockSnapshot);
            completedMeshBuilds.add(new MeshBuildResult(regionKey, mesh));
          });
      submitted++;
    }
  }

  private BlockAccess createBlockSnapshot(int regionX, int regionZ) {
    int startChunkX = regionX * Region.REGION_SIZE;
    int startChunkZ = regionZ * Region.REGION_SIZE;

    Map<Long, Chunk> chunkSnapshot = new HashMap<>();

    for (int chunkX = startChunkX - 1; chunkX <= startChunkX + Region.REGION_SIZE; chunkX++) {
      for (int chunkZ = startChunkZ - 1; chunkZ <= startChunkZ + Region.REGION_SIZE; chunkZ++) {
        Chunk chunk = world.getChunk(chunkX, chunkZ);
        if (chunk != null) {
          chunkSnapshot.put(key(chunkX, chunkZ), chunk);
        }
      }
    }

    return new SnapshotBlockAccess(chunkSnapshot);
  }

  private void applyCompletedRegionMeshes() {
    int applied = 0;

    while (applied < maxRegionMeshAppliesPerFrame) {
      MeshBuildResult result = completedMeshBuilds.poll();
      if (result == null) {
        break;
      }

      long regionKey = result.regionKey;
      int regionX = xFromKey(regionKey);
      int regionZ = zFromKey(regionKey);

      inFlightRegionBuilds.remove(regionKey);

      Region liveRegion = world.getRegion(regionX, regionZ);
      if (liveRegion == null || liveRegion.isEmpty()) {
        removeRegionNode(regionKey);
        renderSystem.invalidateRegion(regionX, regionZ);
        applied++;
        continue;
      }

      Mesh3D mesh = result.mesh;
      if (mesh == null || mesh.getFaceCount() == 0) {
        removeRegionNode(regionKey);
        applied++;
        continue;
      }

      SceneNode oldNode = regionNodes.get(regionKey);
      if (oldNode != null) {
        oldNode.destroy();
      }

      Material material = new Material();
      StaticGeometry geometry = new StaticGeometry(mesh, material);

      SceneNode newNode = new SceneNode("Region [" + regionX + "," + regionZ + "]", geometry);
      scene.addNode(newNode);
      regionNodes.put(regionKey, newNode);

      if (dirtyAfterBuild.remove(regionKey)) {
        queueRegion(regionKey);
      }

      applied++;
    }
  }

  private void removeRegionNode(long regionKey) {
    SceneNode node = regionNodes.remove(regionKey);
    if (node != null) {
      node.destroy();
    }
  }

  private void markChunkRegionAndNeighborsDirty(int chunkX, int chunkZ) {
    int regionX = Math.floorDiv(chunkX, Region.REGION_SIZE);
    int regionZ = Math.floorDiv(chunkZ, Region.REGION_SIZE);

    queueRegionIfRelevant(regionX, regionZ);

    if (Math.floorMod(chunkX, Region.REGION_SIZE) == 0) {
      queueRegionIfRelevant(regionX - 1, regionZ);
    }
    if (Math.floorMod(chunkX + 1, Region.REGION_SIZE) == 0) {
      queueRegionIfRelevant(regionX + 1, regionZ);
    }
    if (Math.floorMod(chunkZ, Region.REGION_SIZE) == 0) {
      queueRegionIfRelevant(regionX, regionZ - 1);
    }
    if (Math.floorMod(chunkZ + 1, Region.REGION_SIZE) == 0) {
      queueRegionIfRelevant(regionX, regionZ + 1);
    }
  }

  private void queueRegionIfRelevant(int regionX, int regionZ) {
    long regionKey = key(regionX, regionZ);

    if (inFlightRegionBuilds.contains(regionKey)) {
      dirtyAfterBuild.add(regionKey);
      return;
    }

    Region region = world.getRegion(regionX, regionZ);
    if ((region == null || region.isEmpty()) && !regionNodes.containsKey(regionKey)) {
      return;
    }

    queueRegion(regionKey);
  }

  private void queueRegion(long regionKey) {
    if (pendingDirtyRegionSet.add(regionKey)) {
      pendingDirtyRegions.addLast(regionKey);
    }
  }

  private boolean isWithinRadius(int chunkX, int chunkZ, int centerX, int centerZ, int radius) {
    return Math.abs(chunkX - centerX) <= radius && Math.abs(chunkZ - centerZ) <= radius;
  }

  private int xFromKey(long key) {
    return (int) (key >> 32);
  }

  private int zFromKey(long key) {
    return (int) key;
  }

  private long key(int x, int z) {
    return (((long) x) << 32) | (z & 0xffffffffL);
  }

  private static class MeshBuildResult {
    private final long regionKey;
    private final Mesh3D mesh;

    private MeshBuildResult(long regionKey, Mesh3D mesh) {
      this.regionKey = regionKey;
      this.mesh = mesh;
    }
  }

  private static class SnapshotBlockAccess implements BlockAccess {
    private final Map<Long, Chunk> chunks;

    private SnapshotBlockAccess(Map<Long, Chunk> chunks) {
      this.chunks = chunks;
    }

    @Override
    public short getBlock(int worldX, int worldY, int worldZ) {
      if (worldY < 0 || worldY >= Chunk.SIZE_Y) return voxels.world.Blocks.AIR;

      int chunkX = Math.floorDiv(worldX, Chunk.SIZE_X);
      int chunkZ = Math.floorDiv(worldZ, Chunk.SIZE_Z);

      Chunk chunk = chunks.get(key(chunkX, chunkZ));
      if (chunk == null) return voxels.world.Blocks.AIR;

      int localX = Math.floorMod(worldX, Chunk.SIZE_X);
      int localZ = Math.floorMod(worldZ, Chunk.SIZE_Z);

      return chunk.getBlock(localX, worldY, localZ);
    }

    private long key(int x, int z) {
      return (((long) x) << 32) | (z & 0xffffffffL);
    }
  }
}
