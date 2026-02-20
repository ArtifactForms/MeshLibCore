package engine.application;

import engine.components.RoundReticle;
import engine.components.SmoothFlyByCameraControl;
import engine.runtime.input.Input;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.PerspectiveCamera;
import engine.scene.nodes.DefaultTestCube;
import math.Color;

public class DefaultScene extends Scene {

  private Input input;

  public DefaultScene(Input input) {
    super("Default scene");
    this.input = input;
    setBackground(Color.DARK_GRAY);
    setupCamera();
    setupUI();
    addNode(new DefaultTestCube(0.5f));
  }

  private void setupCamera() {
    PerspectiveCamera defaultCamera = new PerspectiveCamera();
    setActiveCamera(defaultCamera);

    SmoothFlyByCameraControl control = new SmoothFlyByCameraControl(input, defaultCamera);
    control.setMoveSpeed(5);

    SceneNode cameraNode = new SceneNode("DefaultCamera");
    cameraNode.addComponent(control);
    addNode(cameraNode);
  }

  private void setupUI() {
    getUIRoot().addChild(new SceneNode("Reticle", new RoundReticle()));
  }
}
