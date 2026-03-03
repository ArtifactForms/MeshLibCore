package voxels.editor;

import engine.components.AbstractComponent;
import engine.components.StaticGeometry;
import engine.rendering.Material;
import engine.scene.Scene;
import engine.scene.SceneNode;
import mesh.Mesh3D;
import voxels.render.RegionRenderSystem;
import voxels.world.Chunk;
import voxels.world.NoiseTerrainGenerator;
import voxels.world.Region;
import voxels.world.VoxelWorld;

import java.util.*;

public class WorldStreamer extends AbstractComponent {

  private final WorldAnchor anchor;
  private final VoxelWorld world;
  private final NoiseTerrainGenerator generator;
  private final RegionRenderSystem renderSystem;
  private final int chunkRadius;
  private final int unloadRadius;

  // frame budgets to avoid visible spikes while streaming
  private final int maxChunkGenerationsPerFrame;
  private final int maxChunkUnloadsPerFrame;
  private final int maxRegionRemeshesPerFrame;

  private final Map<Long, SceneNode> regionNodes = new HashMap<>();

  private final Deque<Long> pendingGeneration = new ArrayDeque<>();
  private final Set<Long> pendingGenerationSet = new HashSet<>();

  private final Deque<Long> pendingUnload = new ArrayDeque<>();
  private final Set<Long> pendingUnloadSet = new HashSet<>();

  private final Deque<Long> pendingDirtyRegions = new ArrayDeque<>();
  private final Set<Long> pendingDirtyRegionSet = new HashSet<>();

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
    this(anchor, world, generator, renderSystem, chunkRadius, unloadRadius, 6, 8, 2);
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
      int maxRegionRemeshesPerFrame) {
    this.anchor = anchor;
    this.world = world;
    this.generator = generator;
    this.renderSystem = renderSystem;
    this.chunkRadius = chunkRadius;
    this.unloadRadius = unloadRadius;
    this.maxChunkGenerationsPerFrame = Math.max(1, maxChunkGenerationsPerFrame);
    this.maxChunkUnloadsPerFrame = Math.max(1, maxChunkUnloadsPerFrame);
    this.maxRegionRemeshesPerFrame = Math.max(1, maxRegionRemeshesPerFrame);
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
    rebuildDirtyRegions();
  }

  private void queueMissingChunksNearAnchor(int anchorChunkX, int anchorChunkZ) {
    List<Long> missingChunks = new ArrayList<>();

    for (int chunkX = anchorChunkX - chunkRadius; chunkX <= anchorChunkX + chunkRadius; chunkX++) {
      for (int chunkZ = anchorChunkZ - chunkRadius; chunkZ <= anchorChunkZ + chunkRadius; chunkZ++) {
        if (world.hasChunk(chunkX, chunkZ)) {
          continue;
        }

        long key = key(chunkX, chunkZ);
        if (pendingGenerationSet.contains(key)) {
          continue;
        }

        missingChunks.add(key);
      }
    }

    // closest chunks first to make visible area fill seamlessly from the anchor outward
    missingChunks.sort(
        Comparator.comparingInt(
            key -> {
              int chunkX = xFromKey(key);
              int chunkZ = zFromKey(key);
              int dx = chunkX - currentAnchorChunkX;
              int dz = chunkZ - currentAnchorChunkZ;
              return dx * dx + dz * dz;
            }));

    for (long key : missingChunks) {
      pendingGeneration.addLast(key);
      pendingGenerationSet.add(key);
    }
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

  private void rebuildDirtyRegions() {
    int rebuilt = 0;

    while (rebuilt < maxRegionRemeshesPerFrame && !pendingDirtyRegions.isEmpty()) {
      long regionKey = pendingDirtyRegions.removeFirst();
      pendingDirtyRegionSet.remove(regionKey);

      int regionX = xFromKey(regionKey);
      int regionZ = zFromKey(regionKey);

      renderSystem.invalidateRegion(regionX, regionZ);

      Region region = world.getRegion(regionX, regionZ);
      if (region == null || region.isEmpty()) {
        removeRegionNode(regionKey);
        rebuilt++;
        continue;
      }

      Mesh3D mesh = renderSystem.buildMesh(region, world);
      if (mesh == null || mesh.getFaceCount() == 0) {
        removeRegionNode(regionKey);
        rebuilt++;
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
      rebuilt++;
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

    queueRegionIfPresent(regionX, regionZ);

    if (Math.floorMod(chunkX, Region.REGION_SIZE) == 0) {
      queueRegionIfPresent(regionX - 1, regionZ);
    }
    if (Math.floorMod(chunkX + 1, Region.REGION_SIZE) == 0) {
      queueRegionIfPresent(regionX + 1, regionZ);
    }
    if (Math.floorMod(chunkZ, Region.REGION_SIZE) == 0) {
      queueRegionIfPresent(regionX, regionZ - 1);
    }
    if (Math.floorMod(chunkZ + 1, Region.REGION_SIZE) == 0) {
      queueRegionIfPresent(regionX, regionZ + 1);
    }
  }

  private void queueRegionIfPresent(int regionX, int regionZ) {
    long regionKey = key(regionX, regionZ);

    Region region = world.getRegion(regionX, regionZ);
    if ((region == null || region.isEmpty()) && !regionNodes.containsKey(regionKey)) {
      return;
    }

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
}
