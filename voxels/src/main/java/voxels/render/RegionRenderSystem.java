package voxels.render;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import mesh.Mesh3D;
import voxels.mesh.RegionMesher;
import voxels.world.Region;
import voxels.world.VoxelWorld;

public class RegionRenderSystem {

  private final Map<Region, Mesh3D> meshCache = new ConcurrentHashMap<>();

  public void buildMeshesParallel(VoxelWorld world, int threadCount) {

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    for (Region region : world.getRegions()) {

      executor.submit(
          () -> {
            RegionMesher mesher = new RegionMesher();
            Mesh3D mesh = mesher.create(region, world);
            meshCache.put(region, mesh);
          });
    }

    executor.shutdown();

    try {
      executor.awaitTermination(10, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public Mesh3D getMesh(Region region) {
    return meshCache.get(region);
  }
}
