package client.scene;

import client.app.GameClient;
import client.resources.Resources;
import client.ui.Sprite;
import client.ui.button.ButtonClickCallback;
import client.ui.button.SimpleButton;
import client.ui.core.UiComponent;
import client.ui.cursor.SimpleCursorComponent;
import client.usecases.start.StartGame;
import engine.runtime.input.Input;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.audio.SoundManager;
import engine.scene.camera.PerspectiveCamera;

public class StartScene extends Scene {

  private Input input;
  private boolean gameStarted;
  private GameClient client;

  public StartScene(Input input, GameClient client) {
    this.input = input;
    this.client = client;
    setupCamera();
    setupUI();
    SoundManager.loopSound(Resources.BACKGROUND_MUSIC_KEY);
  }

  private void setupCamera() {
    PerspectiveCamera camera = new PerspectiveCamera();
    setActiveCamera(camera);
  }

  private void setupUI() {
    setupBackground();
    setupCursor();
    setupButtons();
  }

  private void setupBackground() {
    getUIRoot().addChild(new Sprite(Resources.START_SCREEN_BACKGROUND));
  }

  private void setupCursor() {
    SimpleCursorComponent cursorComponent = new SimpleCursorComponent(input);
    SceneNode cursorNode = new SceneNode("Cursor", cursorComponent);
    getUIRoot().addChild(cursorNode);
  }

  private void setupButtons() {
    SimpleButton button = new SimpleButton(Resources.GAME_START_BUTTON_TEXT, 0, 0, 300, 40);
    button.setCallback(
        new ButtonClickCallback() {

          @Override
          public void onButtonClicked() {
            startGame();
          }
        });
    UiComponent component = new UiComponent(input, button);
    SceneNode buttonNode = new SceneNode("Button", component);
    getUIRoot().addChild(buttonNode);

    SimpleButton button2 = new SimpleButton("Exit", 0, 50, 300, 40);
    button2.setCallback(
        new ButtonClickCallback() {

          @Override
          public void onButtonClicked() {
            System.exit(0);
          }
        });
    UiComponent component2 = new UiComponent(input, button2);
    SceneNode buttonNode2 = new SceneNode("Button-2", component2);
    getUIRoot().addChild(buttonNode2);
  }

  private void startGame() {
    if (gameStarted) {
      return;
    }
    gameStarted = true;
    new StartGame(input, client).execute();
  }
}
