package server.world.structures.fin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import math.Bounds;
import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.IcoSphereCreator;
import mesh.modifier.deform.NoiseModifier;
import mesh.modifier.transform.RotateModifier;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TransformAxis;
import mesh.modifier.transform.TranslateModifier;
import voxels.Voxel;
import voxels.mesh.MeshVoxelizer;

public class RockFormationStructure implements VoxelStructure {

  // Cache, damit wir pro Seed nur einmal voxelizen
  private final Map<Long, Collection<Voxel>> voxelCache = new HashMap<>();

  private final Map<Long, Bounds> boundsCache = new HashMap<>();

  @Override
  public Collection<Voxel> getVoxels(long seed) {
    return voxelCache.computeIfAbsent(
        seed,
        s -> {
          Mesh3D mesh = createMesh(s);
          MeshVoxelizer voxelizer = new MeshVoxelizer();
          return voxelizer.voxelize(mesh);
        });
  }

  @Override
  public Bounds getLocalBounds(long seed) {
    return boundsCache.computeIfAbsent(
        seed,
        s -> {
          // Grobe Schätzung der Bounds für den ersten Test
          // (Später kannst du das aus dem Mesh berechnen)
          float r = 30;
          return new Bounds(new Vector3f(-r, -r, -r), new Vector3f(r, r, r));
        });
  }

  private Mesh3D createMesh(long seed) {
    Random rng = new Random(seed);
    Mesh3D finalMesh = new Mesh3D();

    float x = (rng.nextFloat() - 0.5f) * 10f;
    float z = (rng.nextFloat() - 0.5f) * 10f;

    Mesh3D rock = createSingleRock(x, z, rng);
    finalMesh = finalMesh.append(rock);

    return finalMesh;
  }

  private Mesh3D createSingleRock(float x, float z, Random rng) {
    float width = 0.5f + rng.nextFloat() * 2;
    float height = 0.5f + rng.nextFloat() * 4f;
    float depth = 0.5f + rng.nextFloat() * 2;

    IcoSphereCreator creator = new IcoSphereCreator();
    creator.setRadius(5);
    creator.setSubdivisions(2);
    Mesh3D mesh = creator.create();

    // Verformung
    NoiseModifier modifier = new NoiseModifier();
    modifier.setMinimum(0.5f);
    modifier.setMaximum(1.2f);
    modifier.modify(mesh);

    new ScaleModifier(width, height, depth).modify(mesh);
    new TranslateModifier(x, 0, z).modify(mesh);
    new RotateModifier(rng.nextFloat() * Mathf.QUARTER_PI, TransformAxis.Z).modify(mesh);
    new RotateModifier(rng.nextFloat() * Mathf.QUARTER_PI, TransformAxis.X).modify(mesh);

    return mesh;
  }
}
