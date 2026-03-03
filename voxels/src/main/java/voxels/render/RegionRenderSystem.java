package voxels.render;

import mesh.Mesh3D;
import voxels.mesh.RegionMesher;
import voxels.world.Region;
import voxels.world.VoxelWorld;

import java.util.Map;
import java.util.concurrent.*;

public class RegionRenderSystem {

  private final Map<Long, Mesh3D> meshCache = new ConcurrentHashMap<>();

  public void buildMeshesParallel(VoxelWorld world, int threadCount) {

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    for (Region region : world.getRegions()) {
      executor.submit(() -> buildMesh(region, world));
    }

    executor.shutdown();

    try {
      executor.awaitTermination(10, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public Mesh3D buildMesh(Region region, VoxelWorld world) {
    RegionMesher mesher = new RegionMesher();
    Mesh3D mesh = mesher.create(region, world);
    meshCache.put(key(region), mesh);
    return mesh;
  }

  public void invalidateRegion(int regionX, int regionZ) {
    meshCache.remove(key(regionX, regionZ));
  }

  public Mesh3D getMesh(Region region) {
    return meshCache.get(key(region));
  }

  private long key(Region region) {
    return key(region.getRegionX(), region.getRegionZ());
  }

  private long key(int regionX, int regionZ) {
    return (((long) regionX) << 32) | (regionZ & 0xffffffffL);
  }
}
