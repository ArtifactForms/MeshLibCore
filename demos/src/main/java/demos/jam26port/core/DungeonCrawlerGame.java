package demos.jam26port.core;

import demos.jam26port.assets.AssetRefs;
import demos.jam26port.combat.ShootComponent;
import demos.jam26port.game.ui.GameUi;
import demos.jam26port.game.ui.StartScreenComponent;
import demos.jam26port.game.world.GameWorldContext;
import demos.jam26port.level.LevelBuilder;
import demos.jam26port.level.TileMap;
import demos.jam26port.level.TileType;
import demos.jam26port.pickup.HealthPickupComponent;
import demos.jam26port.player.FPSCameraController;
import demos.jam26port.player.HealthComponent;
import demos.jam26port.player.PlayerContext;
import demos.jam26port.world.ExitTriggerComponent;
import demos.jam26port.world.GridCollisionComponent;
import demos.jam26port.world.SphereCollider;
import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.rendering.Graphics;
import engine.runtime.debug.core.DebugDraw;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.SceneNodeVisitor;
import engine.scene.audio.SoundManager;
import engine.scene.camera.PerspectiveCamera;
import engine.scene.light.AmbientLight;
import engine.scene.light.PointLight;
import engine.scene.nodes.Billboard;
import math.Color;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class DungeonCrawlerGame extends BasicApplication {

  public static void main(String[] args) {
    ApplicationSettings settings = ApplicationSettings.defaultSettings();
    settings.setFullscreen(true);
    DungeonCrawlerGame game = new DungeonCrawlerGame();
    game.launch(settings);
  }

  private boolean debug = true;
  private boolean drawDebugNormals = false;

  private Scene scene;
  private Mesh3D levelMesh;
  private LevelBuilder levelBuilder;
  private TileMap tileMap;
  private HealthComponent health;
  private PerspectiveCamera camera;

  private SceneNode uiRoot;

  private PlayerContext player;
  private GameWorldContext world;

  private PointLight light;
  private SceneNode lightNode;

  @Override
  public void onInitialize() {
    setupDebug();
    setupAudio();

    setupScene();

    uiRoot = scene.getUIRoot();

    setupLevel();
    setupCamera();
    setupPlayer();

    setupContext();

    setupWorldEntities();

    setupStartScreen();

//    scene.addLight(new AmbientLight(Color.BLUE));

    SoundManager.loopSound(AssetRefs.SOUND_BACKGROUND_KEY);
  }

  private void setupStartScreen() {
    StartScreenComponent startScreenComponent = new StartScreenComponent(world, input);
    SceneNode startScreenNode = new SceneNode("Start-Screen", startScreenComponent);
    uiRoot.addChild(startScreenNode);
  }

  private void setupContext() {
    GameUi ui = new GameUi(scene.getUIRoot(), tileMap);

    world = new GameWorldContext(player, scene, tileMap, ui);

    world.setLobbySpawn(levelBuilder.getLobbySpawn());
    world.setLevelSpawn(levelBuilder.getPlayerSpawn());
  }

  private void setupPlayer() {
    SceneNode playerNode = new SceneNode("Player");
    playerNode.addComponent(new ShootComponent(input));

    HealthComponent health =
        new HealthComponent(100) {
          @Override
          protected void onPlayerDeath() {
            world.requestPlayerDeath();
          }

          @Override
          protected void onPlayerDamaged() {
            world.requestPlayerDamage();
          }
        };

    playerNode.addComponent(health);

    player = new PlayerContext(health, camera);

    scene.addNode(playerNode);
  }

  private void setupCamera() {
    camera = new PerspectiveCamera();
    camera.setFarPlane(6000);
    FPSCameraController controller = new FPSCameraController(input, camera);
    controller.setMoveSpeed(200);
    SceneNode cameraNode = new SceneNode("Camera", controller);

    GridCollisionComponent gridCollisionComponent =
        new GridCollisionComponent(tileMap, 2, TileMap.TILE_SIZE);
    cameraNode.addComponent(gridCollisionComponent);

    scene.addNode(cameraNode);
    scene.setActiveCamera(camera);
  }

  private void setupScene() {
    scene = new Scene();
    light = new PointLight(Color.WHITE, new Vector3f(384, -30, 256), 3, 0.001f);
//    lightNode = new SceneNode("Light", new LightComponent(light));
//    scene.addLight(light);
//    scene.addNode(lightNode);
    uiRoot = scene.getUIRoot();
    setActiveScene(scene);
  }

  private void setupWorldEntities() {
    setupOverlayLayerEntities();
    setupExit();
  }

  private void setupLevel() {
    tileMap = new TileMap();
    levelBuilder = new LevelBuilder(tileMap);
    SceneNode level = levelBuilder.createLevel();
    scene.addNode(level);
    levelMesh = levelBuilder.getLevelMesh(); // Reference only needed for debug normals
  }

  private void setupDebug() {
    setDisplayInfo(debug);
    setDebugDrawVisible(debug);
  }

  private void setupAudio() {
    SoundManager.addEffect(AssetRefs.SOUND_SHOOT_KEY, AssetRefs.SOUND_SHOOT_PATH, 6);
    SoundManager.addSound(AssetRefs.SOUND_EXIT_KEY, AssetRefs.SOUND_EXIT_PATH);
    SoundManager.addSound(AssetRefs.SOUND_BACKGROUND_KEY, AssetRefs.SOUND_BACKGROUND_PATH);
    SoundManager.addSound(AssetRefs.SOUND_PLAYER_DEAD_KEY, AssetRefs.SOUND_PLAYER_DEAD_PATH);
    SoundManager.addEffect(
        AssetRefs.SOUND_HEALTH_PICK_UP_KEY, AssetRefs.SOUND_HEALTH_PICK_UP_PATH, 6);
    SoundManager.addEffect(
        AssetRefs.SOUND_ENEMY_HIT_SHRIEK_KEY, AssetRefs.SOUND_ENEMY_HIT_SHRIEK_PATH, 6);
    SoundManager.addEffect(AssetRefs.SOUND_PLAYER_HIT_KEY, AssetRefs.SOUND_PLAYER_HIT_PATH, 6);
  }

  private void setupOverlayLayerEntities() {
    for (int x = 0; x < tileMap.getNumTilesX(); x++) {
      for (int y = 0; y < tileMap.getNumTilesY(); y++) {
        TileType tileType = tileMap.getOverlayTypeAt(x, y);

        if (tileType == TileType.ENEMY_SPAWN) {
          world.spawnEnemyAtTile(x, y);
        }

        if (tileType == TileType.HEALTH) {
          spawnHealthPickUpAt(x, y);
        }
      }
    }
  }

  private void spawnHealthPickUpAt(int x, int y) {
    Billboard pickup = new Billboard(AssetRefs.HEALTH_TEXTURE, AssetRefs.HEALTH_UV, 16);
    pickup.at(x * TileMap.TILE_SIZE, -24, y * TileMap.TILE_SIZE);
    pickup.addComponent(new HealthPickupComponent(player, 10));
    scene.addNode(pickup);
  }

  private void setupExit() {
    Billboard billboard = new Billboard(AssetRefs.EXIT_TEXTURE, 16);
    billboard.setUvRect(AssetRefs.EXIT_UV);
    billboard.getTransform().setPosition(levelBuilder.getExit());
    billboard.getTransform().translate(0, -32, 0);
    billboard.addComponent(new ExitTriggerComponent(world));
    scene.addNode(billboard);
  }

  @Override
  public void onUpdate(float tpf) {
//    light.setPosition(player.getPosition().subtract(0, 32, 0));
    world.update(tpf);
    debugColliders();
  }

  private void debugColliders() {
    SceneNodeVisitor visitor =
        new SceneNodeVisitor() {
          @Override
          public void visit(SceneNode node) {
            SphereCollider collider = node.getComponent(SphereCollider.class);
            if (collider != null) {
              DebugDraw.drawSphere(collider.getCenter(), collider.getRadius(), Color.BLUE);
            }
          }
        };
    scene.visitRootNodes(visitor);
  }

  private void debugRenderNormals(Graphics g) {
    if (!drawDebugNormals) return;

    float maxDistance = 1000;

    g.setColor(Color.RED);
    float normalLength = 5;

    for (Face3D f : levelMesh.getFaces()) {

      Vector3f center = new Vector3f();
      for (int idx : f.indices) {
        center.addLocal(levelMesh.getVertexAt(idx));
      }
      center.divideLocal(f.indices.length);

      if (center.distance(camera.getTransform().getPosition()) >= maxDistance) continue;

      Vector3f end = new Vector3f(center).addLocal(new Vector3f(f.normal).multLocal(normalLength));
      DebugDraw.drawLine(center, end, Color.RED);
    }
  }

  @Override
  public void onRender(Graphics g) {
    debugRenderNormals(g);
  }

  @Override
  public void onCleanup() {
    // No cleanup needed for demo
  }
}
