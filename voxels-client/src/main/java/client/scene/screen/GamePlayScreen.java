package client.scene.screen;

import client.app.GameClient;
import client.settings.KeyBinds;
import client.ui.actionbar.ActionBarComponent;
import client.usecases.chat.ChatController;
import client.usecases.chat.ChatViewComponent;
import client.usecases.chat.SendChatMessageController;
import debug.DebugController;
import engine.components.CrossLineReticle;
import engine.runtime.input.Key;
import engine.runtime.input.KeyEvent;
import engine.runtime.input.MouseEvent;
import engine.scene.SceneNode;
import engine.scene.screen.GameScreen;

public class GamePlayScreen extends GameScreen {

  private final GameClient client;
  private final SendChatMessageController controller;
  private final ChatController chatController;
  private DebugController debugController;

  public GamePlayScreen(GameClient client) {
    this.client = client;
    setupWorld();
    setupUI();
    controller = new SendChatMessageController(client);
    chatController = new ChatController(controller);
  }

  private void setupWorld() {
    SceneNode world = new SceneNode("World", client.getChunkManager());
    root.addChild(world);
  }

  private void setupUI() {
    setupChatView();
    setupActionBar();
    setupReticle();
  }

  private void setupReticle() {
    uiRoot.addChild(new SceneNode("Cross-Hair", new CrossLineReticle()));
    //    uiRoot.addChild(new SceneNode("Reticle", new RoundReticle()));
  }

  private void setupActionBar() {
    ActionBarComponent actionBar = new ActionBarComponent();
    client.getView().setActionBarView(actionBar);
    uiRoot.addChild(new SceneNode("Action-Bar", actionBar));
  }

  private void setupChatView() {
    ChatViewComponent chatView = new ChatViewComponent();
    SceneNode chatViewNode = new SceneNode();
    chatViewNode.addComponent(chatView);
    client.getView().setChatView(chatView);
    uiRoot.addChild(chatViewNode);
  }

  @Override
  public boolean capturesMouse() {
    return true;
  }

  @Override
  public boolean isTransparent() {
    return false;
  }

  @Override
  public boolean blocksGameplay() {
    return false;
  }

  @Override
  public void onEnter() {}

  @Override
  public void onExit() {}

  @Override
  public void update(float tpf) {}

  @Override
  public boolean onMouseClicked(MouseEvent e) {
    return false;
  }

  @Override
  public boolean onMousePressed(MouseEvent e) {
    return false;
  }

  @Override
  public boolean onMouseMoved(MouseEvent e) {
    return false;
  }

  @Override
  public boolean onMouseDragged(MouseEvent e) {
    return false;
  }

  @Override
  public boolean onMouseReleased(MouseEvent e) {
    return false;
  }

  @Override
  public boolean onKeyPressed(KeyEvent e) {
    if (e.getKey() == KeyBinds.openChat) {
      getScene().pushScreen(new ChatScreen(chatController));
      return true;
    }

    if (e.getKey() == KeyBinds.showHideChunkBorders) {
      debugController.onShowHideChunkBorders();
      return true;
    }
    if (e.getKey() == KeyBinds.enableDisableFrustumCulling) {
      debugController.onEnableDisableFrustumCulling();
    }

    if (e.getKey() == Key.ESCAPE) {
      getScene().pushScreen(new MenuScreen());
      return true;
    }

    if (e.getKey() == KeyBinds.openCloseInventory) {
      getScene().pushScreen(new InventoryScreen(client.getView().getInventoryView()));
      return true;
    }

    return false;
  }

  @Override
  public boolean onKeyReleased(KeyEvent e) {
    return false;
  }

  @Override
  public boolean onKeyTyped(KeyEvent e) {
    return false;
  }

  public void setDebugController(DebugController debugController) {
    this.debugController = debugController;
  }
}
