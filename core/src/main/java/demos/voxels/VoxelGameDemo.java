package demos.voxels;

import demos.skybox.SkyBox;
import demos.voxels.client.Client;
import demos.voxels.client.event.EventManager;
import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.components.ControlWASD;
import engine.components.CrossLineReticle;
import engine.components.SmoothFlyByCameraControl;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.PerspectiveCamera;
import engine.scene.nodes.DefaultTestCube;
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

  private float speed = GameSettings.flySpeed;
  private SkyBox skyBox;
  private Player player;
  private Scene scene;
  private SceneNode chunkBorders;

  private long seed = 1324;
  private ChunkManager chunkManager;
  private World world;
  private EventManager eventManager;
  private Client client;
  private PerspectiveCamera camera;

  private SceneNode playerNode;
  private TextDisplay display;
  private SceneNode uiRoot;

  @Override
  public void onInitialize() {
    //    setDisplayInfo(false);

    client = new Client();
    player = new Player();
    scene = new Scene();
    uiRoot = scene.getUIRoot();

    setupPlayer();

    scene.setBackground(Color.DARK_GRAY);

    setupSkyBox();
    setupTestCube();
    setupChunkManager();
    world = new World(chunkManager, seed);
    setupUI();
    setupCamera();

    SceneNode hotbarNode = new SceneNode("Hotbar", new Hotbar());
    uiRoot.addChild(hotbarNode);

    chunkBorders = new SceneNode("Chunk-Borders");
    chunkBorders.addComponent(new ChunkVisualizer1());
    scene.addNode(chunkBorders);

    setActiveScene(scene);

    //    new Thread(
    //            new Runnable() {
    //
    //              @Override
    //              public void run() {
    //                client.connect();
    //              }
    //            })
    //        .start();

    display = new TextDisplay();
    SceneNode displayNode = new SceneNode("Display", display);
    uiRoot.addChild(displayNode);

    //    SceneNode rayNode = new SceneNode("Ray", new RayVisualizer(camera));
    SceneNode rayNode = new SceneNode("Ray", new RayBlockDetector(camera, world, display));
    //    scene.addNode(rayNode);

  }

  private void setupPlayer() {
    ControlWASD control = new ControlWASD(input);
    control.mapArrowKeys();
    control.setSpeed(16);
    playerNode = new SceneNode();
    playerNode.addComponent(new PlayerVisual());
    playerNode.addComponent(control);
    scene.addNode(playerNode);
  }

  private void setupCamera() {
    camera = new PerspectiveCamera();
    camera.setFarPlane(10000);
    //
    SmoothFlyByCameraControl control = new SmoothFlyByCameraControl(input, camera);
    control.setMoveSpeed(speed);
    SceneNode camNode = new SceneNode("Camera", control);

    camera.setTarget(new Vector3f(0, 0, 0));
    camera.getTransform().setPosition(0, -200, 400);

    //    SceneNode camNode = new SceneNode();
    //    camNode.addComponent(new EditorControl(input, scene, rootUI));

    scene.addNode(camNode);
    scene.setActiveCamera(camera);

    Vector3f camPosition = camera.getTransform().getPosition();
    //    player.setPosition(camPosition);

    eventManager = new EventManager();
    //    NetworkListener networkListener = new NetworkListener();

    // Subscribe the listener to the event manager
    eventManager.addListener(client);

    // In your ChatComponent constructor, pass the eventManager:
    ChatComponent chatComponent = new ChatComponent(input, camera, eventManager);

    SceneNode chatNode = new SceneNode("Test", chatComponent);
    uiRoot.addChild(chatNode);
  }

  private void setupChunkManager() {
    chunkManager = new ChunkManager(player);
    SceneNode managerNode = new SceneNode();
    managerNode.addComponent(chunkManager);
    scene.addNode(managerNode);

    ChunkProfile profile = new ChunkProfile(chunkManager);
    SceneNode profileNode = new SceneNode("Chunk Profile", profile);
    profileNode.getTransform().setPosition(220, 0, 0);
    uiRoot.addChild(profileNode);
  }

  private void setupUI() {
    uiRoot.addChild(new SceneNode("Cross-Hair", new CrossLineReticle()));
  }

  private void setupTestCube() {
    DefaultTestCube cube = new DefaultTestCube(20);
    cube.getTransform().setPosition(0, -200, 0);
    scene.addNode(cube);
  }

  private void setupSkyBox() {
    skyBox = new SkyBox(4000);
    scene.addNode(skyBox);
  }

  @Override
  public void onUpdate(float tpf) {
    Vector3f position = playerNode.getTransform().getPosition();
    int x = (int) position.x;
    int y = (int) position.y;
    int z = (int) position.z;

    Chunk chunk = world.getChunkAt(x, y, z);
    //    int localX = (int) position.x % Chunk.WIDTH;
    //    int localZ = (int) position.z % Chunk.DEPTH;

    int localX = (x % Chunk.WIDTH + Chunk.WIDTH) % Chunk.WIDTH;
    int localZ = (z % Chunk.DEPTH + Chunk.DEPTH) % Chunk.DEPTH;

    if (chunk != null) {

      int yP = chunk.getHeightValueAt(localX, localZ);

      BlockType type = chunk.getBlock(localX, yP, localZ);

      display.setText("Chunk: " + chunk.getChunkX() + "," + chunk.getChunkZ() + " Block: " + type);
      
      playerNode.getTransform().setPosition(position.x, -yP, position.z);
    }

    Vector3f camPosition = getActiveScene().getActiveCamera().getTransform().getPosition();
    player.setPosition(camPosition);
    //    skyBox.getTransform().setPosition(camPosition);

    //    Vector3f playerPosition = player.getPosition();
    //    int x = (int) (playerPosition.x / Chunk.WIDTH) * Chunk.WIDTH + Chunk.WIDTH / 2;
    //    int z = (int) (playerPosition.z / Chunk.DEPTH) * Chunk.DEPTH + Chunk.DEPTH / 2;
    //    chunkBorders.getTransform().setPosition(x, 0, z);
  }

  @Override
  public void onRender(Graphics g) {

  }

  @Override
  public void onCleanup() {}
}
