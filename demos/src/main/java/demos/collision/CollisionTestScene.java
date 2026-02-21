package demos.collision;

import demos.collision.debug.DebugCollisionRenderer;
import engine.components.CameraFollowComponent;
import engine.components.CrossLineReticle;
import engine.physics.PhysicsQuerySystem;
import engine.runtime.input.Input;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.PerspectiveCamera;
import engine.scene.light.AmbientLight;
import engine.scene.light.DirectionalLight;
import math.Color;
import math.Vector3f;

public class CollisionTestScene extends Scene {

  private SceneNode player;

  public void init(Input input) {
    setupPhysicsSystem();
    setupEnvironment();
    setupPlayer(input);
    setupDebugRenderer();
    setupLight();
    setupUI();
    setupCamera();
  }

  private void setupCamera() {
    PerspectiveCamera camera = new PerspectiveCamera();
    setActiveCamera(camera);

    Vector3f offset = new Vector3f(0, -15, 20);
    CameraFollowComponent cameraFollowComponent = new CameraFollowComponent(camera, player, offset);
    cameraFollowComponent.setSmoothness(2f);

    SceneNode cameraNode = new SceneNode("Camera", cameraFollowComponent);
    addNode(cameraNode);
  }

  private void setupPhysicsSystem() {
    addSystem(new PhysicsQuerySystem());
  }

  private void setupDebugRenderer() {
    addNode(new DebugCollisionRenderer());
  }

  private void setupEnvironment() {
    addNode(new TestEnvironmentFactory().createEnvironment());
  }

  private void setupPlayer(Input input) {
    this.player = new PlayerFactory().createTestCapsulePlayer(input);
    addNode(player);
  }

  private void setupUI() {
    getUIRoot().addChild(new SceneNode("Cross-Line-Reticle", new CrossLineReticle()));
  }

  private void setupLight() {
    addLight(createAmbientLight());
    addLight(createSunLight());
    addLight(createFillLight());
  }

  private AmbientLight createAmbientLight() {
    return new AmbientLight(new Color(0.5f, 0.5f, 0.5f));
  }

  private DirectionalLight createSunLight() {
    return new DirectionalLight(
        new Color(1.0f, 0.98f, 0.85f), new Vector3f(-0.5f, 1.0f, -0.3f).normalize());
  }

  private DirectionalLight createFillLight() {
    return new DirectionalLight(
        new Color(0.15f, 0.15f, 0.18f), new Vector3f(0.5f, -1.0f, 0.3f).normalize());
  }
}
