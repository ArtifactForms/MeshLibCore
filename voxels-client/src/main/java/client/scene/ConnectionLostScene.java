package client.scene;

import client.app.GameClient;
import client.ui.SimpleLabel;
import client.ui.button.ButtonClickCallback;
import client.ui.button.SimpleButton;
import client.ui.core.UiComponent;
import client.ui.cursor.SimpleCursorComponent;
import engine.runtime.input.Input;
import engine.runtime.input.MouseMode;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.screen.GlobalInput;
import math.Color;

public class ConnectionLostScene extends Scene {

  private final GameClient client;
  private final String reason;
  private Input input;

  public ConnectionLostScene(GameClient client, String reason) {
    this.client = client;
    this.reason = reason;
    this.input = GlobalInput.input;
    setupUI();
  }

  @Override
  public void onEnter() {
    input.setMouseMode(MouseMode.ABSOLUTE);
  }

  private void setupUI() {
    setupBackground();
    setupLabel();
    setupBackButton();
    setupCursor();
  }

  private void setupBackground() {
    setBackground(Color.getColorFromInt(120, 120, 120));
  }

  private void setupLabel() {
    SimpleLabel label = new SimpleLabel("Connection to server lost. " + reason, 0, 0, 300, 40);
    UiComponent component = new UiComponent(null, label);
    SceneNode node = new SceneNode("Connection-Lost-Label", component);
    getUIRoot().addChild(node);
  }

  private void setupBackButton() {
    SimpleButton button = new SimpleButton("Back", 0, 100, 300, 40);
    button.setCallback(
        new ButtonClickCallback() {

          @Override
          public void onButtonClicked() {
            client.getSceneManager().setActiveScene(new StartScene(input, client));
          }
        });
    UiComponent component = new UiComponent(input, button);
    SceneNode node = new SceneNode("Back-Button", component);
    getUIRoot().addChild(node);
  }

  private void setupCursor() {
    SimpleCursorComponent component = new SimpleCursorComponent(input);
    SceneNode node = new SceneNode("Cursor", component);
    getUIRoot().addChild(node);
  }
}
