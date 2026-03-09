package client.app;

import client.player.BlockInteractionComponent;
import client.player.ClientMovementConsumer;
import client.player.FPSControl;
import client.ui.ClientView;
import client.ui.HotbarComponent;
import client.ui.HotbarViewComponent;
import client.ui.ActionBarComponent;
import client.ui.View;
import common.game.Hotbar;
import common.game.ItemStack;
import common.world.BlockType;
import engine.components.CrossLineReticle;
import engine.runtime.input.Input;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.PerspectiveCamera;
import math.Color;

public class GameScene extends Scene {

  private Input input;
  private SkyBox skyBox;
  private SceneNode player;
  private ActionBarComponent actionBar;
  private View view;

  public GameScene(Input input) {
    view = new View();

    setBackground(Color.getColorFromInt(180, 210, 255));
    
    this.input = input;

    setupCamera();
    setupPlayer();
//    setupSkyBox();
    setupUI();

    SceneNode world = new SceneNode("World", ApplicationContext.chunkManager);
    addNode(world);

    // Add after world because of block overlay
    SceneNode interactNode = new SceneNode("Interact", new BlockInteractionComponent(input));
    addNode(interactNode);
    ApplicationContext.activeScene = this;

    SceneNode entities = new SceneNode("Entities");
    entities.addComponent(new EntitiesComponent());
    addNode(entities);
    
    ApplicationContext.view = view;
    
//    view.getActionBarView().display("Test Message", 6);
  }

  private void setupCamera() {
    //	  PerspectiveCamera camera = new PerspectiveCamera();
    //	  SmoothFlyByCameraControl control = new SmoothFlyByCameraControl(input, camera);
    //	  SceneNode node = new SceneNode("", control);
    //	  setActiveCamera(camera);
    //	  addNode(node);

    PerspectiveCamera camera = new PerspectiveCamera();
    camera.setFarPlane(8000);
    SceneNode cameraNode = new SceneNode("Camera");
    addNode(cameraNode);
    setActiveCamera(camera);
  }

  private void setupPlayer() {
    player = new SceneNode("Player");
    ClientMovementConsumer movement = new ClientMovementConsumer(player);
    ApplicationContext.clientMovementConsumer = movement;
    FPSControl fpsControl = new FPSControl(input, movement);
    fpsControl.setEyeHeightOffset(2.8f);
    player.addComponent(fpsControl);
    addNode(player);
  }

  private void setupSkyBox() {
    skyBox = new SkyBox(2000);
    skyBox.addComponent(new SkyBoxFollowPlayerComponent(player));
    addNode(skyBox);
  }

  private void setupUI() {
    setupTextDisplay();
    setupHotBar();
    setupReticle();
  }

  private void setupHotBar() {
    Hotbar hotbar = new Hotbar();
    hotbar.setSlot(0, new ItemStack(BlockType.STONE.getId(), 64));
    hotbar.setSlot(1, new ItemStack(BlockType.GRASS_BLOCK.getId(), 64));
    hotbar.setSlot(2, new ItemStack(BlockType.SAND.getId(), 64));

    ApplicationContext.hotbar = hotbar;

    HotbarComponent control = new HotbarComponent(input, hotbar);
    HotbarViewComponent view = new HotbarViewComponent(hotbar);

    SceneNode hotbarNode = new SceneNode("Hotbar");
    hotbarNode.addComponent(control);
    hotbarNode.addComponent(view);

    getUIRoot().addChild(hotbarNode);
  }

  private void setupTextDisplay() {
    actionBar = new ActionBarComponent();
    view.setActionBarView(actionBar);
    getUIRoot().addChild(new SceneNode("ActionNar", actionBar));
  }

  private void setupReticle() {
    getUIRoot().addChild(new SceneNode("Cross-Hair", new CrossLineReticle()));
    //    getUIRoot().addChild(new SceneNode("Reticle", new RoundReticle()));
  }
}
