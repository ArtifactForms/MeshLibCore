package engine.demos.voxels;

import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.components.CrossLineReticle;
import engine.components.SmoothFlyByCameraControl;
import engine.demos.skybox.DefaultTestCube;
import engine.demos.skybox.SkyBox;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.PerspectiveCamera;
import engine.scene.light.AmbientLight;
import engine.scene.light.DirectionalLight;
import math.Color;
import math.Vector3f;
import workspace.ui.Graphics;

public class VoxelGameDemo extends BasicApplication {

  public static void main(String[] args) {
    VoxelGameDemo application = new VoxelGameDemo();
    ApplicationSettings settings = new ApplicationSettings();
    settings.setFullscreen(true);
    application.launch(settings);
  }

  private float speed = 32;
  private boolean useDirectionalLight = true;
  private SkyBox skyBox;
  private Player player;
  private Scene scene;
  private SceneNode chunkBorders;

  @Override
  public void onInitialize() {
    player = new Player();
    scene = new Scene();

    setupLights();
    setupSkyBox();
    setupTestCube();
    setupChunkManager();
    setupUI();
    setupCamera();

    chunkBorders = new SceneNode("Chunk-Borders");
    chunkBorders.addComponent(new ChunkVisualizer1());
    scene.addNode(chunkBorders);

    setActiveScene(scene);
  }

  private void setupCamera() {
    PerspectiveCamera camera = new PerspectiveCamera();
    camera.setFarPlane(10000);

    // Spawn
    camera.getTransform().setPosition(-180, -17, -140);

    SmoothFlyByCameraControl control = new SmoothFlyByCameraControl(input, camera);
    control.setMoveSpeed(speed);
    SceneNode camNode = new SceneNode("Camera", control);
    scene.addNode(camNode);
    scene.setActiveCamera(camera);

    Vector3f camPosition = camera.getTransform().getPosition();
    player.setPosition(camPosition);
  }

  private void setupChunkManager() {
    ChunkManager chunkManager = new ChunkManager(scene, player);
    SceneNode managerNode = new SceneNode();
    managerNode.addComponent(chunkManager);
    scene.addNode(managerNode);

    ChunkProfile profile = new ChunkProfile(chunkManager);
    SceneNode profileNode = new SceneNode("Chunk Profile", profile);
    profileNode.getTransform().setPosition(220, 0, 0);
    rootUI.addChild(profileNode);
  }

  private void setupUI() {
    rootUI.addChild(new SceneNode("Cross-Hair", new CrossLineReticle()));
  }

  private void setupTestCube() {
    DefaultTestCube cube = new DefaultTestCube(20);
    cube.getTransform().setPosition(-100, -200, 0);
    scene.addNode(cube);
  }

  private void setupSkyBox() {
    skyBox = new SkyBox(4000);
    scene.addNode(skyBox);
  }

  private void setupLights() {
    AmbientLight ambientLight = new AmbientLight();
    scene.addLight(ambientLight);

    if (!useDirectionalLight) return;

    DirectionalLight directionalLight =
        new DirectionalLight(Color.WHITE, new Vector3f(0.5f, 1, 0.5f));
    scene.addLight(directionalLight);
  }

  @Override
  public void onUpdate(float tpf) {
    Vector3f camPosition = getActiveScene().getActiveCamera().getTransform().getPosition();
    player.setPosition(camPosition);
    skyBox.getTransform().setPosition(camPosition);

//    Vector3f playerPosition = player.getPosition();
//    int x = (int) (playerPosition.x / Chunk.WIDTH) * Chunk.WIDTH + Chunk.WIDTH / 2;
//    int z = (int) (playerPosition.z / Chunk.DEPTH) * Chunk.DEPTH + Chunk.DEPTH / 2;
//    chunkBorders.getTransform().setPosition(x, 0, z);
  }

  @Override
  public void onRender(Graphics g) {}

  @Override
  public void onCleanup() {}
}
