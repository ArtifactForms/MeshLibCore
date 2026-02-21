package demos.jam26port.game.world;

import java.util.ArrayList;
import java.util.List;

import demos.jam26port.assets.AssetRefs;
import demos.jam26port.enemy.DeathAnimationComponent;
import demos.jam26port.enemy.EnemyAttackComponent;
import demos.jam26port.enemy.EnemyChaseComponent;
import demos.jam26port.enemy.EnemyComponent;
import demos.jam26port.enemy.HitReactionComponent;
import demos.jam26port.game.event.GameEvent;
import demos.jam26port.game.event.GameEventHandler;
import demos.jam26port.game.event.PlayerDamageHandler;
import demos.jam26port.game.ui.GameUi;
import demos.jam26port.level.TileMap;
import demos.jam26port.player.PlayerContext;
import demos.jam26port.world.SphereCollider;
import engine.scene.Scene;
import engine.scene.audio.SoundManager;
import engine.scene.nodes.Billboard;
import math.Vector3f;

public class GameWorldContext implements WorldContext {

  private float eyeHeight = 40;
  private PlayerContext player;

  private final TileMap tileMap;
  private final Scene scene;

  private Vector3f lobbySpawn;
  private Vector3f levelSpawn;
  private GameUi ui;

  private List<GameEventHandler> eventHandlers;

  public GameWorldContext(PlayerContext player, Scene scene, TileMap tileMap, GameUi ui) {
    this.player = player;
    this.scene = scene;
    this.tileMap = tileMap;
    this.ui = ui;
    this.eventHandlers = new ArrayList<GameEventHandler>();
    this.eventHandlers.add(new PlayerDamageHandler(player, ui));
  }

  @Override
  public void spawnEnemyAtTile(int tileX, int tileY) {
    spawnEnemyAt(tileX, tileY);
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
    b.addComponent(new EnemyAttackComponent(this));
    b.addComponent(new SphereCollider(16));
  }

  @Override
  public void perform(GameEvent event) {
    for (GameEventHandler eventHandler : eventHandlers) {
      eventHandler.handle(event);
    }
  }

  @Override
  public void update(float tpf) {
    ui.displayNormalizedHealth(player.getHealth().getHealth01());
    ui.displayPlayerOnMinimap(player.getPosition());
  }

  @Override
  public PlayerContext getPlayer() {
    return player;
  }

  @Override
  public boolean isBlocked(Vector3f worldPos) {
    //    return tileMap.isBlocked(worldPos);
    return false;
  }

  @Override
  public void startLevel() {
    player.setPosition(levelSpawn.subtract(0, eyeHeight, 0));
    ui.displayTitle(AssetRefs.TITLE_TEXT_LEVEL_START);
  }

  @Override
  public void requestPlayerDeath() {
    teleportPlayerToLobby();
    ui.displayTitle(AssetRefs.TITLE_TEXT_GAME_OVER);
    SoundManager.stopSound(AssetRefs.SOUND_BACKGROUND_KEY);
    SoundManager.playSound(AssetRefs.SOUND_PLAYER_DEAD_KEY);
  }

  @Override
  public void requestLevelCompleted() {
    // hook: UI, sound, transition, etc.
  }

  @Override
  public void spawnEffect(String effectId, Vector3f position) {
    // hook: particles, sound-only effects, etc.
  }

  @Override
  public void requestLevelExit() {
    teleportPlayerToLobby();
    ui.displayTitle(AssetRefs.TITLE_TEXT_LEVEL_COMPLETE);
    SoundManager.stopSound(AssetRefs.SOUND_BACKGROUND_KEY);
    SoundManager.playSound(AssetRefs.SOUND_EXIT_KEY);
  }

  @Override
  public void requestPlayerDamage() {
    ui.displayHitFlash(1);
  }

  private void teleportPlayerToLobby() {
    player.setPosition(getLobbySpawn());
  }

  private Vector3f getLobbySpawn() {
    return lobbySpawn.subtract(0, eyeHeight, 0);
  }

  @Override
  public void setLobbySpawn(Vector3f lobbySpawn) {
    this.lobbySpawn = lobbySpawn;
  }

  @Override
  public void setLevelSpawn(Vector3f levelSpawn) {
    this.levelSpawn = levelSpawn;
  }
}
