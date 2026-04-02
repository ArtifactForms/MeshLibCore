package client.ui.screens;

import client.ui.button.SimpleButton;
import client.ui.core.Label;
import client.ui.core.RowLayout;
import client.ui.core.TextField;
import client.ui.core.UiComponent;
import client.ui.core.UiContainer;
import client.ui.cursor.SimpleCursorComponent;
import client.usecases.connecttoserver.ConnectToServerController;
import engine.runtime.input.Input;
import engine.runtime.input.MouseMode;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.PerspectiveCamera;
import math.Color;

public class DirectConnectScreen extends Scene {

  private Input input;
  private ConnectToServerController controller;

  public DirectConnectScreen(Input input, ConnectToServerController controller) {
    this.input = input;
    this.controller = controller;
  }

  @Override
  public void onEnter() {
    setActiveCamera(new PerspectiveCamera());
    input.setMouseMode(MouseMode.ABSOLUTE);
    setBackground(Color.GRAY);
    setupUI();
  }

  private void setupUI() {
    UiContainer container = new UiContainer(200, 100, 200, 20);
    container.setLayout(new RowLayout(5));

    Label hostLabel = new Label("Host:", 0, -80);
    TextField hostField = new TextField(0, -40, 200, 40);

    Label playerLabel = new Label("Player:", 0, 10);
    TextField playerField = new TextField(0, 40, 200, 40);

    SimpleButton connectButton = new SimpleButton("Connect", 0, 120, 140, 40);

//    connectButton.setCallback(() -> controller.connect());

    container.add(hostLabel);
    container.add(hostField);
    container.add(playerLabel);
    container.add(playerField);
    container.add(connectButton);

    UiComponent component = new UiComponent(input, container);

    SceneNode node = new SceneNode("ConnectUI", component);

    getUIRoot().addChild(node);
    getUIRoot().addChild(new SceneNode("Cursor", new SimpleCursorComponent(input)));
  }
}
