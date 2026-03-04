package voxels.mesh;

import mesh.Face3D;
import mesh.Mesh3D;
import org.junit.jupiter.api.Test;
import voxels.render.ProceduralBlockAtlas;
import voxels.world.Blocks;
import voxels.world.Chunk;
import voxels.world.Region;
import voxels.world.VoxelWorld;
import math.Vector3f;
import mesh.geometry.MeshGeometryUtil;

import static org.junit.jupiter.api.Assertions.*;

class RegionMesherAtlasIntegrationTest {

  @Test
  void createAssignsUvIndicesForAllGeneratedFaces() {
    VoxelWorld world = new VoxelWorld();

    Chunk chunk = new Chunk(0, 0);
    chunk.setBlock(0, 0, 0, Blocks.STONE);
    chunk.setHeight(0, 0, 0);
    world.addChunk(chunk);

    Region region = world.getRegion(0, 0);
    assertNotNull(region);

    ProceduralBlockAtlas atlas = new ProceduralBlockAtlas();
    RegionMesher mesher = new RegionMesher(atlas);

    Mesh3D mesh = mesher.create(region, world);

    assertEquals(6, mesh.getFaceCount());

    int uvCount = mesh.getSurfaceLayer().getUVCount();
    for (int i = 0; i < mesh.getFaceCount(); i++) {
      int[] faceUv = mesh.getSurfaceLayer().getFaceUVIndices(i);
      assertNotNull(faceUv);
      assertEquals(4, faceUv.length);
      for (int uvIndex : faceUv) {
        assertTrue(uvIndex >= 0);
        assertTrue(uvIndex < uvCount);
      }
    }
  }


  @Test
  void createDoesNotStretchAtlasByMergingAdjacentFaces() {
    VoxelWorld world = new VoxelWorld();

    Chunk chunk = new Chunk(0, 0);
    chunk.setBlock(0, 0, 0, Blocks.STONE);
    chunk.setBlock(1, 0, 0, Blocks.STONE);
    chunk.setHeight(0, 0, 0);
    chunk.setHeight(1, 0, 0);
    world.addChunk(chunk);

    Region region = world.getRegion(0, 0);
    assertNotNull(region);

    ProceduralBlockAtlas atlas = new ProceduralBlockAtlas();
    RegionMesher mesher = new RegionMesher(atlas);

    Mesh3D mesh = mesher.create(region, world);

    // Two adjacent cubes should keep per-block UV density (10 visible unit faces)
    // instead of being merged into large atlas-stretched quads.
    assertEquals(10, mesh.getFaceCount());
  }



  @Test
  void createProducesOutwardFacingWindingForSingleBlock() {
    VoxelWorld world = new VoxelWorld();

    Chunk chunk = new Chunk(0, 0);
    chunk.setBlock(0, 0, 0, Blocks.STONE);
    chunk.setHeight(0, 0, 0);
    world.addChunk(chunk);

    Region region = world.getRegion(0, 0);
    assertNotNull(region);

    RegionMesher mesher = new RegionMesher(new ProceduralBlockAtlas());
    Mesh3D mesh = mesher.create(region, world);

    Vector3f center = new Vector3f(0.5f, -0.5f, 0.5f);

    for (int i = 0; i < mesh.getFaceCount(); i++) {
      Face3D face = mesh.getFaceAt(i);
      Vector3f normal = MeshGeometryUtil.calculateFaceNormal(mesh, face);
      Vector3f faceCenter = MeshGeometryUtil.calculateFaceCenter(mesh, face);
      Vector3f fromCenter = faceCenter.subtract(center);

      assertTrue(normal.dot(fromCenter) > 0f, "Face winding should point outward for face " + i);
    }
  }

}
