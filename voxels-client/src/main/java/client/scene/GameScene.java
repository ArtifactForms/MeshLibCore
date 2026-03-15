package client.scene;

import client.app.GameClient;
import client.cam.CameraManager;
import client.entity.EntitiesComponent;
import client.player.PlayerController;
import client.ui.actionbar.ActionBarComponent;
import client.ui.hotbar.HotbarComponent;
import client.ui.hotbar.HotbarViewComponent;
import client.ui.title.TitleTextComponent;
import client.usecases.chat.ChatComponent;
import client.usecases.chat.ChatController;
import client.usecases.chat.ChatViewComponent;
import client.usecases.chat.SendChatMessageController;
import client.usecases.debug.displaychunkborders.DisplayChunkBordersComponent;
import client.usecases.interact.InteractionComponent;
import client.usecases.interact.InteractionController;
import client.usecases.interact.TargetingService;
import client.usecases.openinventory.InventoryViewComponent;
import client.usecases.openinventory.OpenInventoryComponent;
import client.usecases.openinventory.OpenInventoryController;
import common.game.Hotbar;
import common.game.Inventory;
import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import debug.DebugComponent;
import debug.DebugController;
import engine.components.CrossLineReticle;
import engine.runtime.input.Input;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.Camera;
import math.Color;
import messages.ChatMessageService;

public class GameScene extends Scene {

  private Input input;
  private SkyBox skyBox;
  private SceneNode player;
  private ActionBarComponent actionBar;
  private GameClient client;
  private PlayerController playerController;

  public GameScene(Input input, GameClient client) {

    this.client = client;
    
    TitleTextComponent textComponent = new TitleTextComponent();
    SceneNode titleText = new SceneNode("Title", textComponent);
    getUIRoot().addChild(titleText);
    client.getView().setTitleView(textComponent);
    
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
    ChatMessageService messageService = new ChatMessageService(client.getView().getChatView());

    DebugController controller = new DebugController(messageService);
    DebugComponent debugComponent = new DebugComponent(input, controller);
    addNode(new SceneNode("Debug", debugComponent));

    //    SceneNode cullingNode =
    //        new SceneNode(
    //            "Culling",
    //            new AbstractComponent() {
    //              @Override
    //              public void onUpdate(float tpf) {
    //                if (input.wasKeyReleased(KeyBinds.enableDisableFrustumCulling)) {
    //                  RenderSettings.frustum_Culling = !RenderSettings.frustum_Culling;
    //                  String value = RenderSettings.frustum_Culling ? "enabled" : "disabled";
    //                  messageService.displayMessage(MessagePrefix.DEBUG, "Frustum culling: " +
    // value);
    //                }
    //              }
    //            });
//    addNode(cullingNode);

    SceneNode debugNode =
        new SceneNode(
            "Debug-Chunk",
            new DisplayChunkBordersComponent(input, client.getPlayer(), messageService));
    addNode(debugNode);
  }

  private void setupPlayer() {
    player = new SceneNode("Player");

    Camera camera = client.getPlayerCamera();
    setActiveCamera(camera);

    playerController = new PlayerController(input, camera, client);
    player.addComponent(playerController);

    // TODO Remove later
    // editor / debug view support
    new CameraManager(this, input, playerController, client);

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
    Hotbar hotbar = new Hotbar(client.getPlayer().getInventory());
    //    hotbar.setSlot(0, new ItemStack(Blocks.STONE.getId(), 64));
    //    hotbar.setSlot(1, new ItemStack(Blocks.GRASS_BLOCK.getId(), 64));
    //    hotbar.setSlot(2, new ItemStack(Blocks.SAND.getId(), 64));

    HotbarComponent control = new HotbarComponent(input, hotbar);
    HotbarViewComponent view = new HotbarViewComponent(hotbar);

    client.getView().setHotbarView(view);

    SceneNode hotbarNode = new SceneNode("Hotbar");
    hotbarNode.addComponent(control);
    hotbarNode.addComponent(view);

    getUIRoot().addChild(hotbarNode);
  }

  private void setupInventory() {
    Inventory inventory = client.getPlayer().getInventory();
    for (BlockType type : BlockRegistry.getAll()) {
      inventory.addItem(type.getId(), 64);
    }

    //    InventoryComponent component = new InventoryComponent();
    InventoryViewComponent component = new InventoryViewComponent(input, inventory);
    //    component.setActive(false);

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
