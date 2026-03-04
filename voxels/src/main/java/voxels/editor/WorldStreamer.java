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

  private final Map<Long, SceneNode> regionNodes = new HashMap<>();

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
    this.anchor = anchor;
    this.world = world;
    this.generator = generator;
    this.renderSystem = renderSystem;
    this.chunkRadius = chunkRadius;
    this.unloadRadius = unloadRadius;
  }

  @Override
  public void onAttachToScene(Scene scene) {
    this.scene = scene;
    streamWorld(true);
  }

  @Override
  public void onUpdate(float tpf) {
    streamWorld(false);
  }

  private void streamWorld(boolean force) {
    int anchorChunkX = Math.floorDiv((int) Math.floor(anchor.getX()), Chunk.SIZE_X);
    int anchorChunkZ = Math.floorDiv((int) Math.floor(anchor.getZ()), Chunk.SIZE_Z);

    if (!force && anchorChunkX == currentAnchorChunkX && anchorChunkZ == currentAnchorChunkZ) {
      return;
    }

    currentAnchorChunkX = anchorChunkX;
    currentAnchorChunkZ = anchorChunkZ;

    Set<Long> dirtyRegions = new HashSet<>();

    generateMissingChunks(anchorChunkX, anchorChunkZ, dirtyRegions);
    unloadFarChunks(anchorChunkX, anchorChunkZ, dirtyRegions);
    rebuildDirtyRegions(dirtyRegions);
  }

  private void generateMissingChunks(int anchorChunkX, int anchorChunkZ, Set<Long> dirtyRegions) {
    for (int chunkX = anchorChunkX - chunkRadius; chunkX <= anchorChunkX + chunkRadius; chunkX++) {
      for (int chunkZ = anchorChunkZ - chunkRadius; chunkZ <= anchorChunkZ + chunkRadius; chunkZ++) {
        if (world.hasChunk(chunkX, chunkZ)) {
          continue;
        }

        Chunk chunk = new Chunk(chunkX, chunkZ);
        generator.generate(chunk);
        world.addChunk(chunk);

        markChunkRegionAndNeighborsDirty(chunkX, chunkZ, dirtyRegions);
      }
    }
  }

  private void unloadFarChunks(int anchorChunkX, int anchorChunkZ, Set<Long> dirtyRegions) {
    for (Chunk chunk : world.getChunks()) {
      int dx = Math.abs(chunk.getChunkX() - anchorChunkX);
      int dz = Math.abs(chunk.getChunkZ() - anchorChunkZ);

      if (dx <= unloadRadius && dz <= unloadRadius) {
        continue;
      }

      int chunkX = chunk.getChunkX();
      int chunkZ = chunk.getChunkZ();

      world.removeChunk(chunkX, chunkZ);
      markChunkRegionAndNeighborsDirty(chunkX, chunkZ, dirtyRegions);
    }
  }

  private void rebuildDirtyRegions(Set<Long> dirtyRegions) {
    for (long regionKey : dirtyRegions) {
      int regionX = (int) (regionKey >> 32);
      int regionZ = (int) regionKey;

      renderSystem.invalidateRegion(regionX, regionZ);

      Region region = world.getRegion(regionX, regionZ);
      if (region == null || region.isEmpty()) {
        removeRegionNode(regionKey);
        continue;
      }

      Mesh3D mesh = renderSystem.buildMesh(region, world);
      if (mesh == null || mesh.getFaceCount() == 0) {
        removeRegionNode(regionKey);
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
    }
  }

  private void removeRegionNode(long regionKey) {
    SceneNode node = regionNodes.remove(regionKey);
    if (node != null) {
      node.destroy();
    }
  }

  private void markChunkRegionAndNeighborsDirty(int chunkX, int chunkZ, Set<Long> dirtyRegions) {
    int regionX = Math.floorDiv(chunkX, Region.REGION_SIZE);
    int regionZ = Math.floorDiv(chunkZ, Region.REGION_SIZE);

    addRegionIfPresent(regionX, regionZ, dirtyRegions);

    if (Math.floorMod(chunkX, Region.REGION_SIZE) == 0) {
      addRegionIfPresent(regionX - 1, regionZ, dirtyRegions);
    }
    if (Math.floorMod(chunkX + 1, Region.REGION_SIZE) == 0) {
      addRegionIfPresent(regionX + 1, regionZ, dirtyRegions);
    }
    if (Math.floorMod(chunkZ, Region.REGION_SIZE) == 0) {
      addRegionIfPresent(regionX, regionZ - 1, dirtyRegions);
    }
    if (Math.floorMod(chunkZ + 1, Region.REGION_SIZE) == 0) {
      addRegionIfPresent(regionX, regionZ + 1, dirtyRegions);
    }
  }

  private void addRegionIfPresent(int regionX, int regionZ, Set<Long> dirtyRegions) {
    Region region = world.getRegion(regionX, regionZ);
    long regionKey = key(regionX, regionZ);
    if (region != null && !region.isEmpty()) {
      dirtyRegions.add(regionKey);
      return;
    }

    if (regionNodes.containsKey(regionKey)) {
      dirtyRegions.add(regionKey);
    }
  }

  private long key(int x, int z) {
    return (((long) x) << 32) | (z & 0xffffffffL);
  }
}
