package client.scene.screen;

import client.settings.KeyBinds;
import client.ui.InventoryView;
import client.ui.cursor.SimpleCursorComponent;
import engine.runtime.input.Key;
import engine.runtime.input.KeyEvent;
import engine.runtime.input.MouseEvent;
import engine.scene.SceneNode;
import engine.scene.screen.GameScreen;
import engine.scene.screen.GlobalInput;

public class InventoryScreen extends GameScreen {

  private InventoryView view;

  public InventoryScreen(InventoryView view) {
    this.view = view;
    uiRoot.addChild(new SceneNode("Cursor", new SimpleCursorComponent(GlobalInput.input)));
  }

  @Override
  public void onEnter() {
    view.display();
  }

  @Override
  public void onExit() {
    view.hide();
  }

  @Override
  public void update(float tpf) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean capturesMouse() {
    return false;
  }

  @Override
  public boolean isTransparent() {
    return true;
  }

  @Override
  public boolean blocksGameplay() {
    return true;
  }

  @Override
  public boolean onMouseClicked(MouseEvent e) {
    return true;
  }

  @Override
  public boolean onMousePressed(MouseEvent e) {
    return true;
  }

  @Override
  public boolean onMouseMoved(MouseEvent e) {
    return true;
  }

  @Override
  public boolean onMouseDragged(MouseEvent e) {
    return true;
  }

  @Override
  public boolean onMouseReleased(MouseEvent e) {
    return true;
  }

  @Override
  public boolean onKeyPressed(KeyEvent e) {
    if (e.getKey() == KeyBinds.openCloseInventory) {
      getScene().popScreen();
      return true;
    }

    if (e.getKey() == Key.ESCAPE) {
      getScene().popScreen();
      return true;
    }

    return true;
  }

  @Override
  public boolean onKeyReleased(KeyEvent e) {
    return true;
  }

  @Override
  public boolean onKeyTyped(KeyEvent e) {
    return true;
  }
}
