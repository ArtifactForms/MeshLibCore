package voxels.editor;

import engine.components.StaticGeometry;
import engine.rendering.Material;
import engine.runtime.input.Input;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.OrbitCamera;
import engine.scene.camera.OrbitCameraControl;
import engine.scene.light.AmbientLight;
import engine.scene.light.DirectionalLight;
import math.Color;
import math.Vector3f;
import mesh.Mesh3D;
import voxels.render.RegionRenderSystem;
import voxels.world.Chunk;
import voxels.world.NoiseTerrainGenerator;
import voxels.world.Region;
import voxels.world.VoxelWorld;

public class EditorScene extends Scene {

  private Input input;
  private WorldAnchor anchor;
  private VoxelWorld world;
  private RegionRenderSystem renderSystem;

  public EditorScene(Input input) {
    this.input = input;

    anchor = new WorldAnchor();
    WorldAnchorMovement movement = new WorldAnchorMovement(input, anchor);
    SceneNode anchorNode = new SceneNode("Anchor", movement);
    addNode(anchorNode);

    setBackground(Color.DARK_GRAY);

    setupCamera();
    setupUI();
    setupLight();
    setupWorld();
    setupRendering();
  }

  private void setupWorld() {

    world = new VoxelWorld();

    long seed = 0;
    int chunkRadius = 10;

    NoiseTerrainGenerator generator = new NoiseTerrainGenerator(seed);

    for (int chunkX = -chunkRadius; chunkX < chunkRadius; chunkX++) {
      for (int chunkZ = -chunkRadius; chunkZ < chunkRadius; chunkZ++) {

        Chunk chunk = new Chunk(chunkX, chunkZ);
        generator.generate(chunk);

        world.addChunk(chunk);
      }
    }
  }

  private void setupRendering() {

    renderSystem = new RegionRenderSystem();

    renderSystem.buildMeshesParallel(world, 8);

    for (Region region : world.getRegions()) {

      Mesh3D mesh = renderSystem.getMesh(region);

      if (mesh == null || mesh.getFaceCount() == 0) continue;

      Material material = new Material();
      StaticGeometry geometry = new StaticGeometry(mesh, material);

      SceneNode node =
          new SceneNode(
              "Region [" + region.getRegionX() + "," + region.getRegionZ() + "]", geometry);

      addNode(node);
    }
  }

  private void setupCamera() {
    OrbitCamera camera = new OrbitCamera();
    OrbitCameraControl control = new OrbitCameraControl(input, camera);
    SceneNode node = new SceneNode("Orbit-Cam", control);
    addNode(node);
    setActiveCamera(camera);
  }

  private void setupUI() {
    SceneNode cursor = new SceneNode("Cursor", new CursorComponent(input));
    getUIRoot().addChild(cursor);
  }

  private void setupLight() {
    addLight(createAmbientLight());
    addLight(createSunLight());
    addLight(createFillLight());
  }

  private AmbientLight createAmbientLight() {
    return new AmbientLight(new Color(0.5f, 0.5f, 0.5f));
  }

  private DirectionalLight createSunLight() {
    return new DirectionalLight(
        new Color(1.0f, 0.98f, 0.85f), new Vector3f(-0.5f, 1.0f, -0.3f).normalize());
  }

  private DirectionalLight createFillLight() {
    return new DirectionalLight(
        new Color(0.15f, 0.15f, 0.18f), new Vector3f(0.5f, -1.0f, 0.3f).normalize());
  }
}
