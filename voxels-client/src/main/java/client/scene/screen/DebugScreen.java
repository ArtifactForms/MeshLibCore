package client.scene.screen;

import client.settings.KeyBinds;
import debug.DebugController;
import engine.runtime.input.KeyEvent;
import engine.runtime.input.MouseEvent;
import engine.scene.screen.GameScreen;

public class DebugScreen extends GameScreen {

  private DebugController controller;

  public DebugScreen(DebugController controller) {
    this.controller = controller;
  }

  @Override
  public void onEnter() {}

  @Override
  public void onExit() {}

  @Override
  public void update(float tpf) {}

  @Override
  public boolean capturesMouse() {
    return true;
  }

  @Override
  public boolean isTransparent() {
    return true;
  }

  @Override
  public boolean blocksGameplay() {
    return false;
  }

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
    if (e.getKey() == KeyBinds.showHideChunkBorders) {
      controller.onShowHideChunkBorders();
      return true;
    }
    if (e.getKey() == KeyBinds.enableDisableFrustumCulling) {
      controller.onEnableDisableFrustumCulling();
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
}
