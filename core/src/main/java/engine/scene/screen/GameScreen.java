package engine.scene.screen;

import engine.runtime.input.KeyEvent;
import engine.runtime.input.MouseEvent;
import engine.scene.Scene;
import engine.scene.SceneNode;

public abstract class GameScreen {

  protected SceneNode root;

  protected SceneNode uiRoot;

  public GameScreen() {
    this.root = new SceneNode();
    this.uiRoot = new SceneNode();
  }

  public abstract void onEnter();

  public abstract void onExit();

  public abstract void update(float tpf);

  public abstract boolean capturesMouse();

  public abstract boolean isTransparent();

  public abstract boolean blocksGameplay();

  public abstract boolean onMouseClicked(MouseEvent e);

  public abstract boolean onMousePressed(MouseEvent e);

  public abstract boolean onMouseMoved(MouseEvent e);

  public abstract boolean onMouseDragged(MouseEvent e);

  public abstract boolean onMouseReleased(MouseEvent e);

  public abstract boolean onKeyPressed(KeyEvent e);

  public abstract boolean onKeyReleased(KeyEvent e);

  public abstract boolean onKeyTyped(KeyEvent e);

  public SceneNode getRootNode() {
    return root;
  }

  public SceneNode getUiRootNode() {
    return uiRoot;
  }

  public Scene getScene() {
    return root.getScene();
  }
}
