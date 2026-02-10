package demos.jam26port.game.ui;

import demos.jam26port.game.ui.health.HealthBarComponent;
import demos.jam26port.game.ui.health.HealthBarViewImpl;
import demos.jam26port.game.ui.hitflash.HitFlashComponent;
import demos.jam26port.game.ui.hitflash.HitFlashViewImpl;
import demos.jam26port.game.ui.minimap.MiniMapComponent;
import demos.jam26port.game.ui.minimap.MiniMapViewImpl;
import demos.jam26port.game.ui.minimap.MinimapView;
import demos.jam26port.game.ui.weaponhud.WeaponHudComponent;
import demos.jam26port.game.ui.weaponhud.WeaponView;
import demos.jam26port.game.ui.weaponhud.WeaponViewImpl;
import demos.jam26port.level.TileMap;
import demos.texture.Title;
import engine.components.RoundReticle;
import engine.scene.SceneNode;
import math.Color;
import math.Vector3f;

public class GameUi {

  private TileMap tileMap;

  private SceneNode uiRoot;

  private HealthBarComponent healthBar;
  private TitleDisplayComponent title;
  private MiniMapComponent miniMap;
  private HitFlashComponent hitFlash;

  public GameUi(SceneNode uiRoot, TileMap tileMap) {
    this.uiRoot = uiRoot;
    this.tileMap = tileMap;
    setupUI();
  }

  private void setupUI() {
    setupHealthBar();
    setupWeaponHud();
    setupMiniMap();
    setupTitle();
    setupReticle();
    setupHitFlash();
  }

  private void setupHitFlash() {
    hitFlash = new HitFlashComponent(new HitFlashViewImpl());
    SceneNode hitFlashNode = new SceneNode("Hit-Flash", hitFlash);
    uiRoot.addChild(hitFlashNode);
  }

  private void setupTitle() {
    SceneNode titleNode = new SceneNode();
    title = new TitleDisplayComponent();
    titleNode.addComponent(title);
    uiRoot.addChild(titleNode);
  }

  private void setupHealthBar() {
    SceneNode healthUI = new SceneNode("Health-Bar");
    healthBar = new HealthBarComponent(new HealthBarViewImpl());
    healthUI.addComponent(healthBar);
    uiRoot.addChild(healthUI);
  }

  private void setupWeaponHud() {
    WeaponView view = new WeaponViewImpl();
    SceneNode weaponHud = new SceneNode("Weapon-Hud", new WeaponHudComponent(view));
    uiRoot.addChild(weaponHud);
  }

  private void setupMiniMap() {
    MinimapView view = new MiniMapViewImpl(tileMap);
    miniMap = new MiniMapComponent(view);
    SceneNode miniMapNode = new SceneNode("MiniMap");
    miniMapNode.addComponent(miniMap);
    uiRoot.addChild(miniMapNode);
  }

  private void setupReticle() {
    uiRoot.addChild(new SceneNode("Reticle", new RoundReticle()));
  }

  public void displayNormalizedHealth(float normalizedHealth) {
    healthBar.displayNormalizedHealth(normalizedHealth);
  }

  public void displayTitle(String text) {
    Title title =
        new Title.Builder()
            .text(text)
            .size(200)
            .stayTime(4)
            .fadeInTime(1)
            .fadeOutTime(1)
            .color(Color.WHITE)
            .build();
    this.title.display(title);
  }

  public void displayPlayerOnMinimap(Vector3f position) {
    miniMap.setPlayerWorldPosition(position);
  }

  public void displayHitFlash(float strength) {
    hitFlash.displayHitFlash(strength);
  }
}
