package client.scene;

import client.app.GameClient;
import client.cam.CameraManager;
import client.entity.EntitiesComponent;
import client.player.PlayerController;
import client.rendering.RenderSettings;
import client.ui.InventoryComponent;
import client.ui.actionbar.ActionBarComponent;
import client.ui.hotbar.HotbarComponent;
import client.ui.hotbar.HotbarViewComponent;
import client.usecases.chat.ChatComponent;
import client.usecases.chat.ChatController;
import client.usecases.chat.ChatViewComponent;
import client.usecases.chat.SendChatMessageController;
import client.usecases.interact.InteractionComponent;
import client.usecases.interact.InteractionController;
import client.usecases.interact.TargetingService;
import client.usecases.openinventory.OpenInventoryComponent;
import client.usecases.openinventory.OpenInventoryController;
import common.game.Hotbar;
import common.game.ItemStack;
import common.game.block.Blocks;
import engine.components.AbstractComponent;
import engine.components.CrossLineReticle;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.Camera;
import math.Color;

public class GameScene extends Scene {

  private Input input;
  private SkyBox skyBox;
  private SceneNode player;
  private ActionBarComponent actionBar;
  private GameClient client;

  public GameScene(Input input, GameClient client) {
    this.client = client;

    setBackground(Color.getColorFromInt(180, 210, 255));

    this.input = input;
    setupPlayer();
    //    setupSkyBox();
    setupUI();

    SceneNode world = new SceneNode("World", client.getChunkManager());
    addNode(world);

    // Add after world because of block overlay
    TargetingService targetingService = new TargetingService(input, client);
    InteractionController controller = new InteractionController(client);
    SceneNode interactNode =
        new SceneNode("Interact", new InteractionComponent(input, controller, targetingService));
    addNode(interactNode);
    //    ApplicationContext.activeScene = this;

    SceneNode entities = new SceneNode("Entities");
    entities.addComponent(new EntitiesComponent(client));
    addNode(entities);
    //    view.getActionBarView().display("Test Message", 6);

    setupDebug();
  }

  private void setupDebug() {
    SceneNode cullingNode =
        new SceneNode(
            "Culling",
            new AbstractComponent() {
              @Override
              public void onUpdate(float tpf) {
                if (input.wasKeyReleased(Key.F)) {
                  RenderSettings.frustum_Culling = !RenderSettings.frustum_Culling;
                }
              }
            });
    addNode(cullingNode);
  }

  private void setupPlayer() {
    player = new SceneNode("Player");

    Camera camera = client.getPlayerCamera();
    setActiveCamera(camera);

    PlayerController controller = new PlayerController(input, camera, client);
    player.addComponent(controller);

    // TODO Remove later
    // editor / debug view support
    new CameraManager(this, input, controller, client);

    addNode(player);
  }

  private void setupSkyBox() {
    skyBox = new SkyBox(2000);
    skyBox.addComponent(new SkyBoxFollowPlayerComponent(player));
    addNode(skyBox);
  }

  private void setupUI() {
    setupActionBar();
    setupHotBar();
    setupChat();
    setupInventory();
    setupReticle();
  }

  private void setupChat() {
    SendChatMessageController controller = new SendChatMessageController(client);
    ChatController chatController = new ChatController(controller);
    ChatComponent chatComponent = new ChatComponent(input, chatController);
    SceneNode chatNode = new SceneNode("Chat", chatComponent);
    getUIRoot().addChild(chatNode);

    ChatViewComponent chatView = new ChatViewComponent();
    SceneNode chatViewNode = new SceneNode();
    chatViewNode.addComponent(chatView);
    client.getView().setChatView(chatView);
    getUIRoot().addChild(chatViewNode);
  }

  private void setupHotBar() {
    Hotbar hotbar = new Hotbar();
    hotbar.setSlot(0, new ItemStack(Blocks.STONE.getId(), 64));
    hotbar.setSlot(1, new ItemStack(Blocks.GRASS_BLOCK.getId(), 64));
    hotbar.setSlot(2, new ItemStack(Blocks.SAND.getId(), 64));

    HotbarComponent control = new HotbarComponent(input, hotbar);
    HotbarViewComponent view = new HotbarViewComponent(hotbar);

    client.getView().setHotbarView(view);

    SceneNode hotbarNode = new SceneNode("Hotbar");
    hotbarNode.addComponent(control);
    hotbarNode.addComponent(view);

    getUIRoot().addChild(hotbarNode);
  }

  private void setupInventory() {
    InventoryComponent component = new InventoryComponent();
    client.getView().setInventoryView(component);

    OpenInventoryController controller = new OpenInventoryController(client);
    OpenInventoryComponent openComponent = new OpenInventoryComponent(input, controller);

    SceneNode inventoryNode = new SceneNode("Inventory", component, openComponent);

    getUIRoot().addChild(inventoryNode);
  }

  private void setupActionBar() {
    actionBar = new ActionBarComponent();
    client.getView().setActionBarView(actionBar);
    getUIRoot().addChild(new SceneNode("Action-Bar", actionBar));
  }

  private void setupReticle() {
    getUIRoot().addChild(new SceneNode("Cross-Hair", new CrossLineReticle()));
    //    getUIRoot().addChild(new SceneNode("Reticle", new RoundReticle()));
  }
}
