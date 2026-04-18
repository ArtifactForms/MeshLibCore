package client.scene;

import client.app.GameClient;
import client.cam.CameraManager;
import client.entity.EntitiesComponent;
import client.player.PlayerController;
import client.resources.Resources;
import client.scene.screen.GamePlayScreen;
import client.ui.title.TitleTextComponent;
import client.usecases.chat.ChatViewComponent;
import client.usecases.debug.displaychunkborders.DisplayChunkBordersComponent;
import client.usecases.interact.InteractionComponent;
import client.usecases.interact.InteractionController;
import client.usecases.interact.TargetingService;
import client.usecases.openinventory.InventoryViewComponent;
import client.usecases.openinventory.OpenInventoryComponent;
import client.usecases.openinventory.OpenInventoryController;
import common.game.Inventory;
import debug.DebugController;
import engine.runtime.input.Input;
import engine.scene.CameraMode;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.audio.SoundManager;
import engine.scene.camera.Camera;
import math.Color;
import messages.ChatMessageService;

public class GameScene extends Scene {

  private Input input;

  private SceneNode player;

  private GameClient client;

  private PlayerController playerController;

  public GameScene(Input input, GameClient client) {
    setCameraMode(CameraMode.CAMERA_RELATIVE);

    GamePlayScreen screen = new GamePlayScreen(client);
    pushScreen(screen);

    this.client = client;

    setBackground(Color.DARK_GRAY);

    this.input = input;
    setupPlayer();
    setupUI();

    // Add after world because of block overlay
    TargetingService targetingService = new TargetingService(input, client);
    InteractionController controller = new InteractionController(client);
    SceneNode interactNode =
        new SceneNode("Interact", new InteractionComponent(input, controller, targetingService));
    addNode(interactNode);

    SceneNode entities = new SceneNode("Entities");
    entities.addComponent(new EntitiesComponent(client));
    addNode(entities);

    ChatMessageService messageService = new ChatMessageService(client.getView().getChatView());
    DisplayChunkBordersComponent displayChunkBordersComponent =
        new DisplayChunkBordersComponent(input, client.getPlayer(), messageService);
    addNode(new SceneNode("Chunk-Borders", displayChunkBordersComponent));
    DebugController debugController =
        new DebugController(messageService, displayChunkBordersComponent);
    screen.setDebugController(debugController);
  }

  @Override
  public void onEnter() {
    SoundManager.loopSound(Resources.BACKGROUND_MUSIC_KEY);
  }

  @Override
  public void onExit() {
    SoundManager.stopSound(Resources.BACKGROUND_MUSIC_KEY);
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

  private void setupUI() {
    setupInventory();
    setupChat();
    setupTitleView();
  }

  private void setupTitleView() {
    TitleTextComponent textComponent = new TitleTextComponent();
    SceneNode titleText = new SceneNode("Title", textComponent);
    getUIRoot().addChild(titleText);
    client.getView().setTitleView(textComponent);
  }

  private void setupChat() {
    ChatViewComponent chatView = new ChatViewComponent();
    SceneNode chatViewNode = new SceneNode();
    chatViewNode.addComponent(chatView);
    client.getView().setChatView(chatView);
    getUIRoot().addChild(chatViewNode);
  }

  private void setupInventory() {
    Inventory inventory = client.getPlayer().getInventory();

    InventoryViewComponent component =
        new InventoryViewComponent(input, inventory, client.getNetwork());
    client.getView().setInventoryView(component);

    OpenInventoryController controller = new OpenInventoryController(client);
    OpenInventoryComponent openComponent = new OpenInventoryComponent(input, controller);

    SceneNode inventoryNode = new SceneNode("Inventory", component, openComponent);

    getUIRoot().addChild(inventoryNode);
  }
}
