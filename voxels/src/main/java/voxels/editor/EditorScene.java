package voxels.editor;

import engine.runtime.input.Input;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.OrbitCamera;
import engine.scene.camera.OrbitCameraControl;
import engine.scene.light.AmbientLight;
import engine.scene.light.DirectionalLight;
import engine.scene.nodes.DefaultTestCube;
import math.Color;
import math.Vector3f;
import voxels.render.RegionRenderSystem;
import voxels.world.NoiseTerrainGenerator;
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
//    setupLight();
    setupWorld();
    setupStreaming();
  
  
    addNode(new DefaultTestCube(10));
  }

  private void setupWorld() {
    world = new VoxelWorld();
    renderSystem = new RegionRenderSystem();
  }

  private void setupStreaming() {
    long seed = 0;
    int visibleChunkRadius = 20;
    int unloadChunkRadius = visibleChunkRadius + 2;

    NoiseTerrainGenerator generator = new NoiseTerrainGenerator(seed);
    WorldStreamer streamer =
        new WorldStreamer(anchor, world, generator, renderSystem, visibleChunkRadius, unloadChunkRadius);

    SceneNode streamingNode = new SceneNode("World-Streamer", streamer);
    addNode(streamingNode);

    VoxelEditTool editTool = new VoxelEditTool(input, world, streamer);
    SceneNode editorNode = new SceneNode("Voxel-Edit-Tool", editTool);
    addNode(editorNode);
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