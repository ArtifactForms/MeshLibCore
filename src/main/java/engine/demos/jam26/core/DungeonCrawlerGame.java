package engine.demos.jam26.core;

import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.application.Viewport;
import engine.components.RoundReticle;
import engine.debug.core.DebugDraw;
import engine.demos.jam26.assets.AssetRefs;
import engine.demos.jam26.combat.ShootComponent;
import engine.demos.jam26.enemy.DeathAnimationComponent;
import engine.demos.jam26.enemy.EnemyAttackComponent;
import engine.demos.jam26.enemy.EnemyChaseComponent;
import engine.demos.jam26.enemy.EnemyComponent;
import engine.demos.jam26.enemy.HitReactionComponent;
import engine.demos.jam26.level.LevelBuilder;
import engine.demos.jam26.level.TileMap;
import engine.demos.jam26.level.TileType;
import engine.demos.jam26.pickup.HealthPickupComponent;
import engine.demos.jam26.player.FPSCameraController;
import engine.demos.jam26.player.PlayerHealthComponent;
import engine.demos.jam26.ui.HealthBarUIComponent;
import engine.demos.jam26.ui.HitFlashComponent;
import engine.demos.jam26.ui.MiniMapComponent;
import engine.demos.jam26.ui.StartScreenComponent;
import engine.demos.jam26.ui.WeaponHudComponent;
import engine.demos.jam26.world.ExitTriggerComponent;
import engine.demos.jam26.world.GridCollisionComponent;
import engine.demos.jam26.world.SphereCollider;
import engine.demos.texture.Title;
import engine.demos.texture.TitleTextComponent;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.SceneNodeVisitor;
import engine.scene.audio.SoundManager;
import engine.scene.camera.PerspectiveCamera;
import engine.scene.nodes.Billboard;
import math.Color;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import workspace.ui.Graphics;

public class DungeonCrawlerGame extends BasicApplication {

  public static void main(String[] args) {
    ApplicationSettings settings = ApplicationSettings.defaultSettings();
    settings.setFullscreen(true);
    DungeonCrawlerGame game = new DungeonCrawlerGame();
    game.launch(settings);
  }

  private boolean debug = false;
  private boolean drawDebugNormals = false;

  private float eyeHeight = 40;

  private Scene scene;
  private Mesh3D levelMesh;
  private LevelBuilder levelBuilder;
  private TileMap tileMap;
  private PlayerHealthComponent health;
  private PerspectiveCamera camera;

  @Override
  public void onInitialize() {

    setupDebug();
    setupAudio();

    setupScene();
    setupLevel();
    setupCamera();
    setupPlayer();
    setupWorldEntities();
    setupUI();

    SoundManager.loopSound(AssetRefs.SOUND_BACKGROUND_KEY);
  }

  private void setupPlayer() {
    SceneNode playerNode = new SceneNode("Player");
    playerNode.addComponent(new ShootComponent(input));

    health = createHealthComponent();
    playerNode.addComponent(health);

    HitFlashComponent hitFlashComponent = new HitFlashComponent();

    playerNode.addComponent(hitFlashComponent);

    SceneNode hitFlash = new SceneNode("Hit-Flash", hitFlashComponent);
    rootUI.addChild(hitFlash);

    scene.addNode(playerNode);
    teleportPlayerToSpawn();
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
    setActiveScene(scene);
  }

  private PlayerHealthComponent createHealthComponent() {
    return new PlayerHealthComponent() {
      @Override
      protected void onDeath() {
        teleportPlayerToLobbySpawn();

        SoundManager.stopSound(AssetRefs.SOUND_BACKGROUND_KEY);
        SoundManager.playSound(AssetRefs.SOUND_PLAYER_DEAD_KEY);

        Title title =
            new Title.Builder()
                .text(AssetRefs.TITLE_TEXT_GAME_OVER)
                .size(200)
                .stayTime(4)
                .fadeInTime(1)
                .fadeOutTime(1)
                .color(Color.WHITE)
                .build();
        SceneNode titleNode = new SceneNode();
        TitleTextComponent titleComp = new TitleTextComponent(title);
        titleNode.addComponent(titleComp);
        rootUI.addChild(titleNode);
      }
    };
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

  private void teleportPlayerToSpawn() {
    camera.getTransform().setPosition(levelBuilder.getPlayerSpawn().subtract(0, eyeHeight, 0));
  }

  private void teleportPlayerToLobbySpawn() {
    camera.getTransform().setPosition(levelBuilder.getLobbySpawn().subtract(0, eyeHeight, 0));
  }

  private void setupUI() {
    setupHealthBar();
    setupWeaponHud();
    setupMiniMap();
    setupReticle();
    setupStartScreen();
  }

  private void setupHealthBar() {
    SceneNode healthUI = new SceneNode("Health-Bar");
    healthUI.addComponent(new HealthBarUIComponent(health));
    rootUI.addChild(healthUI);
  }

  private void setupWeaponHud() {
    SceneNode weaponHud = new SceneNode("Weapon-Hud", new WeaponHudComponent());
    rootUI.addChild(weaponHud);
  }

  private void setupReticle() {
    rootUI.addChild(new SceneNode("Reticle", new RoundReticle()));
  }

  private void setupStartScreen() {
    StartScreenComponent startScreenComponent = new StartScreenComponent(input);
    SceneNode startScreenNode = new SceneNode("Start-Screen", startScreenComponent);
    rootUI.addChild(startScreenNode);
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
  }

  private void setupOverlayLayerEntities() {
    for (int x = 0; x < tileMap.getNumTilesX(); x++) {
      for (int y = 0; y < tileMap.getNumTilesY(); y++) {
        TileType tileType = tileMap.getOverlayTypeAt(x, y);

        if (tileType == TileType.ENEMY_SPAWN) {
          spawnEnemyAt(x, y);
        }

        if (tileType == TileType.HEALTH) {
          spawnHealthPickUpAt(x, y);
        }
      }
    }
  }

  private void spawnEnemyAt(int x, int y) {
    Billboard enemy = createEnemyBillboard(x, y);
    attachEnemyComponents(enemy);
    scene.addNode(enemy);
  }

  private Billboard createEnemyBillboard(int x, int y) {
    Billboard b = new Billboard(AssetRefs.ENEMY_EYE_IDLE_TEXTURE, AssetRefs.ENEMY_EYE_IDLE_UV, 16);
    b.at(x * TileMap.TILE_SIZE, -32, y * TileMap.TILE_SIZE);
    return b;
  }

  private void attachEnemyComponents(Billboard b) {
    b.addComponent(new EnemyComponent());
    b.addComponent(new HitReactionComponent());
    b.addComponent(new DeathAnimationComponent());
    b.addComponent(new EnemyChaseComponent());
    b.addComponent(new EnemyAttackComponent(health));
    b.addComponent(new SphereCollider(16));
  }

  private void spawnHealthPickUpAt(int x, int y) {
    Billboard pickup = new Billboard(AssetRefs.HEALTH_TEXTURE, AssetRefs.HEALTH_UV, 16);
    pickup.at(x * TileMap.TILE_SIZE, -24, y * TileMap.TILE_SIZE);
    pickup.addComponent(new HealthPickupComponent(health, 10));
    scene.addNode(pickup);
  }

  private void setupMiniMap() {
    Viewport viewport = getViewport();
    MiniMapComponent miniMapComponent = new MiniMapComponent(tileMap, scene);
    miniMapComponent.setPosition(viewport.getWidth() - 320, 20);

    SceneNode miniMapNode = new SceneNode("MiniMap");
    miniMapNode.addComponent(miniMapComponent);

    rootUI.addChild(miniMapNode);
  }

  private void setupExit() {
    Title title =
        new Title.Builder()
            .text(AssetRefs.TITLE_TEXT_LEVEL_COMPLETE)
            .size(200)
            .stayTime(4)
            .fadeInTime(1)
            .fadeOutTime(1)
            .color(Color.WHITE)
            .build();
    SceneNode titleNode = new SceneNode();
    TitleTextComponent titleComp = new TitleTextComponent(title);
    titleNode.addComponent(titleComp);
    titleNode.setActive(false);
    rootUI.addChild(titleNode);

    Billboard billboard = new Billboard(AssetRefs.EXIT_TEXTURE, 16);
    billboard.setUvRect(AssetRefs.EXIT_UV);
    billboard.getTransform().setPosition(levelBuilder.getExit());
    billboard.getTransform().translate(0, -32, 0);
    billboard.addComponent(
        new ExitTriggerComponent(
            titleNode, levelBuilder.getLobbySpawn().subtract(0, eyeHeight, 0)));
    scene.addNode(billboard);
  }

  @Override
  public void onUpdate(float tpf) {
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

    for (Face3D f : levelMesh.faces) {

      Vector3f center = new Vector3f();
      for (int idx : f.indices) {
        center.addLocal(levelMesh.vertices.get(idx));
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
