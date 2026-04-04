package client.scene;

import client.app.GameClient;
import client.resources.Resources;
import client.ui.SimpleLabel;
import client.ui.TextAlignment;
import client.ui.button.ButtonClickCallback;
import client.ui.button.SimpleButton;
import client.ui.core.TextField;
import client.ui.core.UiComponent;
import client.ui.core.UiElement;
import client.ui.cursor.SimpleCursorComponent;
import client.usecases.connecttoserver.ConnectToServerController;
import client.usecases.connecttoserver.ConnectToServerView;
import engine.runtime.input.Input;
import engine.runtime.input.MouseMode;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.audio.SoundManager;
import engine.scene.camera.PerspectiveCamera;
import math.Color;

public class DirectConnectScene extends Scene implements ConnectToServerView {

  private static final int WIDTH = 600;

  private static final int HEIGHT = 40;

  private Input input;

  private GameClient client;

  private TextField serverAddressField;

  private TextField playerNameField;

  private SimpleLabel messageLabel;

  private ConnectToServerView view;

  public DirectConnectScene(Input input, GameClient client) {
    this.input = input;
    this.client = client;
    setupCamera();
    setupBackground();
    setupUI();
    this.view = this;
  }

  private void setupCamera() {
    setActiveCamera(new PerspectiveCamera());
  }

  @Override
  public void onEnter() {
    input.setMouseMode(MouseMode.ABSOLUTE);
  }

  @Override
  public void onExit() {}

  private void setupUI() {
    setupLabel();
    setupServerAddressLabel();
    setupServerAddressField();
    setupPlayerNameLabel();
    setupNameField();
    setupJoinServerButton();
    setupCancelButton();
    setupMessageLabel();
    setupCursor(); // On top of all
  }

  private void setupBackground() {
    setBackground(Color.getColorFromInt(120, 120, 120));
  }

  private void setupCursor() {
    SimpleCursorComponent cursor = new SimpleCursorComponent(input);
    SceneNode cursorNode = new SceneNode("Cursor", cursor);
    getUIRoot().addChild(cursorNode);
  }

  private void setupLabel() {
    SimpleLabel label = new SimpleLabel("Direct Connect", 0, -300, WIDTH, HEIGHT);
    add(label, "Direct-Connect-Label");
  }

  private void setupPlayerNameLabel() {
    SimpleLabel label = new SimpleLabel("Player Name:", 0, -150, WIDTH, HEIGHT);
    label.setAlignment(TextAlignment.LEFT);
    add(label, "Player-Name-Label");
  }

  private void setupNameField() {
    playerNameField = new TextField(0, -100, WIDTH, HEIGHT);
    add(playerNameField, "Name-Field");
  }

  private void setupServerAddressLabel() {
    SimpleLabel label = new SimpleLabel("Server Address:", 0, -50, WIDTH, HEIGHT);
    label.setAlignment(TextAlignment.LEFT);
    add(label, "Server-Address-Label");
  }

  private void setupServerAddressField() {
    serverAddressField = new TextField(0, 0, WIDTH, HEIGHT);
    serverAddressField.setText("localhost:25565");
    add(serverAddressField, "Server-Field");
  }

  private void setupJoinServerButton() {
    SimpleButton button = new SimpleButton("Join Server", 0, 100, WIDTH, HEIGHT);
    button.setCallback(
        new ButtonClickCallback() {

          @Override
          public void onButtonClicked() {
            String input = serverAddressField.getText();
            String playerName = playerNameField.getText();
            new ConnectToServerController(client, view).connect(input, playerName);
          }
        });
    add(button, "Join-Server-Button");
  }

  private void setupCancelButton() {
    SimpleButton button = new SimpleButton("Cancel", 0, 150, WIDTH, HEIGHT);
    button.setCallback(
        new ButtonClickCallback() {

          @Override
          public void onButtonClicked() {
            client.getSceneManager().setActiveScene(new StartScene(input, client));
          }
        });
    add(button, "Cancel-Button");
  }

  private void setupMessageLabel() {
    messageLabel = new SimpleLabel("", 0, 250, WIDTH, HEIGHT);
    add(messageLabel, "Message-Label");
  }

  private void add(UiElement element, String name) {
    UiComponent component = new UiComponent(input, element);
    SceneNode node = new SceneNode(name, component);
    getUIRoot().addChild(node);
  }

  @Override
  public void displayMessage(String message) {
    messageLabel.setText(message);
  }

  @Override
  public void displayGameScene() {
    SoundManager.stopSound(Resources.MENU_BACKGROUND_MUSIC_KEY);
    client.getSceneManager().setActiveScene(new GameScene(input, client));
  }
}
