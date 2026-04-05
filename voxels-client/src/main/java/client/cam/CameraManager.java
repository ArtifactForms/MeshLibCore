package client.cam;

import client.app.GameClient;
import client.player.PlayerController;
import client.ray.RaycastMode;
import client.settings.GameSettings;
import client.ui.cursor.SimpleCursorComponent;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.debug.DebugCameraRenderer;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.runtime.input.MouseMode;
import engine.scene.CameraMode;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.Camera;
import engine.scene.camera.OrbitCamera;
import engine.scene.camera.OrbitCameraControl;
import engine.scene.screen.GameScreen;
import math.Vector3f;

public class CameraManager {

  private boolean orbit;

  private Input input;

  private Camera camera;

  private OrbitCamera orbitCamera;

  private OrbitCameraControl orbitCameraControl;

  private SceneNode cursorNode;

  private GameClient client;

  private PlayerController playerController;

  public CameraManager(
      Scene scene, Input input, PlayerController playerController, GameClient client) {
    this.input = input;
    this.playerController = playerController;
    this.client = client;
    setupOrbitCam();

    this.camera = client.getPlayerCamera();

    SceneNode control = new SceneNode("", new ControlComponent());
    scene.addNode(control);

    SceneNode cameraNode = new SceneNode("Control");
    cameraNode.addComponent(orbitCameraControl);
    scene.addNode(cameraNode);

    cursorNode = new SceneNode("Cursor", new SimpleCursorComponent(input));
  }

  private void setupOrbitCam() {
    orbitCamera = new OrbitCamera();
    orbitCameraControl = new OrbitCameraControl(input, orbitCamera);
    orbitCameraControl.setActive(false);
  }

  public class ControlComponent extends AbstractComponent implements RenderableComponent {

    @Override
    public void onUpdate(float tpf) {
      GameScreen screen = getOwner().getScene().getTopScreen();
      if (screen == null || screen.blocksGameplay()) {
        return;
      }

      Vector3f position = camera.getTransform().getPosition();

      //      client.getNetwork().send(new PlayerMovePacket(position.x, -position.y, position.z, 0,
      // 0));

      if (input.wasKeyReleased(Key.J)) {
        orbit();
        getOwner().getScene().setCameraMode(CameraMode.WORLD_SPACE);
      }

      if (input.wasKeyReleased(Key.K)) {
        playerCam();
        getOwner().getScene().setCameraMode(CameraMode.CAMERA_RELATIVE);
      }

      if (input.wasKeyPressed(Key.R)) {
        client.getChunkManager().forceRebuild();
      }

      if (input.wasKeyReleased(Key.C)) {
        //        orbitCamera.setTarget(camera.getTransform().getPosition());
        orbitCamera.setTarget(Vector3f.ZERO);
      }
    }

    private void orbit() {
      orbit = true;
      playerController.setActive(false);
      orbitCameraControl.setActive(true);

      input.setMouseMode(MouseMode.ABSOLUTE);
      client.setRaycastMode(RaycastMode.CURSOR);
      GameSettings.fog = false;
      getOwner().getScene().getUIRoot().addChild(cursorNode);

      getOwner().getScene().setActiveCamera(orbitCamera);
    }

    private void playerCam() {
      orbit = false;
      playerController.setActive(true);
      orbitCameraControl.setActive(false);

      input.setMouseMode(MouseMode.LOCKED);
      client.setRaycastMode(RaycastMode.CROSS_HAIR);
      GameSettings.fog = true;
      getOwner().getScene().getUIRoot().removeChild(cursorNode);

      getOwner().getScene().setActiveCamera(client.getPlayerCamera());
    }

    @Override
    public void render(Graphics g) {
      if (orbit) DebugCameraRenderer.render(g, camera, CameraMode.CAMERA_RELATIVE);
    }
  }
}
