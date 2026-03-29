package client.scene.screen;

import client.ui.SimpleLabel;
import client.ui.button.ButtonClickCallback;
import client.ui.button.SimpleButton;
import client.ui.core.UiComponent;
import client.ui.cursor.SimpleCursorComponent;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.KeyEvent;
import engine.runtime.input.MouseEvent;
import engine.scene.SceneNode;
import engine.scene.screen.GameScreen;
import engine.scene.screen.GlobalInput;
import math.Color;

public class MenuScreen extends GameScreen {

  private UiComponent quitButton;
  private UiComponent backToGameButton;

  public MenuScreen() {
    SceneNode backgroundNode = new SceneNode("", new MenuScreenComponent());
    uiRoot.addChild(backgroundNode);

    setupUI();
  }

  private void setupUI() {
    setupLabel();
    setupQuitButton();
    setupBackToGameButton();
    uiRoot.addChild(new SceneNode("", new SimpleCursorComponent(GlobalInput.input)));
  }

  private void setupLabel() {
    SimpleLabel label = new SimpleLabel("GAME MENU", 0, -100, 300, 40);
    UiComponent labelComponent = new UiComponent(null, label);
    SceneNode node = new SceneNode("Label", labelComponent);
    uiRoot.addChild(node);
  }

  private void setupBackToGameButton() {
    SimpleButton button = new SimpleButton("Back to Game", 0, -50, 300, 40);
    button.setCallback(
        new ButtonClickCallback() {

          @Override
          public void onButtonClicked() {
            close();
          }
        });
    backToGameButton = new UiComponent(null, button);
    SceneNode buttonNode = new SceneNode("Back-to-Game-Button", backToGameButton);
    uiRoot.addChild(buttonNode);
  }

  private void setupQuitButton() {
    SimpleButton button = new SimpleButton("Quit", 0, 0, 300, 40);
    button.setCallback(
        new ButtonClickCallback() {

          @Override
          public void onButtonClicked() {
            System.exit(0);
          }
        });
    quitButton = new UiComponent(null, button);
    SceneNode buttonNode = new SceneNode("Quit-Button", quitButton);
    uiRoot.addChild(buttonNode);
  }

  private void close() {
    getScene().popScreen();
  }

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
    quitButton.onMouseClicked(e.getX(), e.getY());
    backToGameButton.onMouseClicked(e.getX(), e.getY());
    return true;
  }

  @Override
  public boolean onMousePressed(MouseEvent e) {
    return true;
  }

  @Override
  public boolean onMouseMoved(MouseEvent e) {
    quitButton.onMouseMoved(e.getX(), e.getY());
    backToGameButton.onMouseMoved(e.getX(), e.getY());
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
//    if (e.getKey() == Key.ESCAPE) {
//      close();
//      return true;
//    }
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

  public class MenuScreenComponent extends AbstractComponent implements RenderableComponent {

    @Override
    public void render(Graphics g) {
      g.setColor(new Color(0, 0, 0, 0.4f));
      g.fillRect(0, 0, g.getWidth(), g.getHeight());
    }
  }
}
