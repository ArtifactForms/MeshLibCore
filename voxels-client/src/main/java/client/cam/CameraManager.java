package client.cam;

import client.app.ApplicationContext;
import client.app.GameSettings;
import client.ray.RaycastMode;
import client.ui.cursor.SimpleCursorComponent;
import common.network.packets.PlayerMovePacket;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.components.SmoothFlyByCameraControl;
import engine.rendering.Graphics;
import engine.runtime.debug.DebugCameraRenderer;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.runtime.input.MouseMode;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.OrbitCamera;
import engine.scene.camera.OrbitCameraControl;
import engine.scene.camera.PerspectiveCamera;
import math.Vector3f;

public class CameraManager {

  private Input input;
  private PerspectiveCamera camera;
  private OrbitCamera orbitCamera;
  private SmoothFlyByCameraControl flyByCameraControl;
  private OrbitCameraControl orbitCameraControl;
  private SceneNode cursorNode;

  public CameraManager(Scene scene, Input input) {
    this.input = input;
    camera = new PerspectiveCamera();
    camera.setFarPlane(600);
    camera.setNearPlane(0.1f);

    flyByCameraControl = new SmoothFlyByCameraControl(input, camera);
    flyByCameraControl.setMoveSpeed(12);

    scene.setActiveCamera(camera);

    SceneNode movement = new SceneNode("", new ControlComponent());
    scene.addNode(movement);

    orbitCamera = new OrbitCamera();
    orbitCameraControl = new OrbitCameraControl(input, orbitCamera);
    orbitCameraControl.setActive(false);

    SceneNode cameraNode = new SceneNode("Control");
    cameraNode.addComponent(flyByCameraControl);
    cameraNode.addComponent(orbitCameraControl);
    scene.addNode(cameraNode);

    cursorNode = new SceneNode("Cursor", new SimpleCursorComponent(input));
    
    ApplicationContext.playerCamera = camera;
  }

  public class ControlComponent extends AbstractComponent implements RenderableComponent {

    @Override
    public void onUpdate(float tpf) {
      Vector3f position = camera.getTransform().getPosition();

      ApplicationContext.network.send(
          new PlayerMovePacket(position.x, -position.y, position.z, 0, 0));

      if (input.wasKeyReleased(Key.J)) {
        orbit();
      }

      if (input.wasKeyReleased(Key.K)) {
        flyCam();
      }
      
      if (input.wasKeyPressed(Key.R)) {
    	  ApplicationContext.chunkManager.forceRebuild();
      }
      
      if (input.wasKeyReleased(Key.C)) {
    	  orbitCamera.setTarget(camera.getTransform().getPosition());
      }
    }

    private void orbit() {
      ApplicationContext.raycastMode = RaycastMode.CURSOR;
      GameSettings.fog = false;
      getOwner().getScene().getUIRoot().addChild(cursorNode);
      flyByCameraControl.setActive(false);
      orbitCameraControl.setActive(true);
      input.setMouseMode(MouseMode.ABSOLUTE);
      getOwner().getScene().setActiveCamera(orbitCamera);
    }

    private void flyCam() {
      ApplicationContext.raycastMode = RaycastMode.CROSS_HAIR;
      GameSettings.fog = true;
      getOwner().getScene().getUIRoot().removeChild(cursorNode);
      flyByCameraControl.setActive(true);
      orbitCameraControl.setActive(false);
      input.setMouseMode(MouseMode.LOCKED);
      getOwner().getScene().setActiveCamera(camera);
    }

    @Override
    public void render(Graphics g) {
      DebugCameraRenderer.render(g, camera);
    }
  }
}
