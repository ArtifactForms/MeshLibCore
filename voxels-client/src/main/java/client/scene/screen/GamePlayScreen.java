package client.scene.screen;

import client.app.GameClient;
import client.ui.actionbar.ActionBarComponent;
import client.usecases.chat.ChatViewComponent;
import engine.components.CrossLineReticle;
import engine.runtime.input.Input;
import engine.scene.SceneNode;
import engine.scene.screen.GameScreen;

public class GamePlayScreen extends GameScreen {

  private GameClient client;

  public GamePlayScreen(Input input, GameClient client) {
    super(input);
    this.client = client;
    setupWorld();
    setupUI();
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
  public void onEnter() {}

  @Override
  public void onExit() {}

  @Override
  public void update(float tpf) {}

  @Override
  public boolean consumeInput() {
    return false;
  }
}
