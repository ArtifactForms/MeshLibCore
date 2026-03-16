package engine.scene.screen;

import engine.runtime.input.Input;
import engine.scene.SceneNode;

public abstract class GameScreen {

  protected Input input;
  protected SceneNode root;
  protected SceneNode uiRoot;

  public GameScreen(Input input) {
    this.input = input;
    this.root = new SceneNode();
    this.uiRoot = new SceneNode();
  }

  public abstract void onEnter();

  public abstract void onExit();

  public abstract void update(float tpf);

  public abstract boolean consumeInput();

  public SceneNode getRootNode() {
    return root;
  }

  public SceneNode getUiRootNode() {
    return uiRoot;
  }
}
