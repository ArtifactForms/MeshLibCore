package client.scene.screen;

import engine.runtime.input.KeyEvent;
import engine.runtime.input.MouseEvent;
import engine.scene.screen.GameScreen;

public class InventoryScreen extends GameScreen {

  public InventoryScreen() {}

  @Override
  public void onEnter() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onExit() {
    // TODO Auto-generated method stub

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
