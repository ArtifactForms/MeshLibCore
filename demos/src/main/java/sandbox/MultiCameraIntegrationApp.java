package sandbox;

import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.components.SmoothFlyByCameraControl;
import engine.rendering.Graphics;
import engine.runtime.debug.DebugCameraRenderer;
import engine.runtime.debug.core.DebugDraw;
import engine.runtime.input.Key;
import engine.runtime.input.MouseMode;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.OrbitCamera;
import engine.scene.camera.OrbitCameraControl;
import engine.scene.camera.PerspectiveCamera;
import engine.scene.nodes.DefaultTestCube;
import math.Color;
import math.Vector3f;

/**
 *
 *
 * <h2>MultiCameraIntegration</h2>
 *
 * <p>Exploratory application used to experiment with multiple camera systems and runtime camera
 * switching.
 *
 * <p><strong>Important:</strong>
 *
 * <ul>
 *   <li>This is NOT production-ready architecture.
 *   <li>This is NOT a reference implementation.
 *   <li>This class exists purely for experimentation and integration testing.
 * </ul>
 *
 * <p>The following aspects are explored:
 *
 * <ul>
 *   <li>OrbitCamera + OrbitCameraControl
 *   <li>PerspectiveCamera + SmoothFlyByCameraControl
 *   <li>Switching the active Scene camera at runtime
 *   <li>Mouse mode transitions (LOCKED / ABSOLUTE)
 *   <li>Debug grid rendering
 * </ul>
 *
 * <p>Controls:
 *
 * <ul>
 *   <li>NUM_1 → Activate Perspective Fly Camera
 *   <li>NUM_2 → Activate Orbit Camera
 *   <li>ESC → Exit Application
 * </ul>
 */
public class MultiCameraIntegrationApp extends BasicApplication {

  public static void main(String[] args) {
    MultiCameraIntegrationApp app = new MultiCameraIntegrationApp();
    app.launch(ApplicationSettings.defaultSettings().setFullscreen(true));
  }

  private Scene scene;

  private OrbitCamera orbitCamera;
  private SceneNode orbitCameraNode;

  private PerspectiveCamera perspectiveCamera;
  private SceneNode perspectiveCameraNode;

  @Override
  public void onInitialize() {
    setupScene();
    setupOrbitCamera();
    setupPerspectiveCamera();
    setupUI();

    enablePerspectiveCamera(); // Default active camera
  }

  /** Creates a minimal test scene containing a single cube. */
  private void setupScene() {
    float radius = 1f;

    scene = new Scene();
    scene.setBackground(Color.DARK_GRAY);
    scene.addNode(new DefaultTestCube(radius));

    setActiveScene(scene);
  }

  /** Initializes the orbit camera and attaches its control component. */
  private void setupOrbitCamera() {
    orbitCamera = new OrbitCamera();
    OrbitCameraControl control = new OrbitCameraControl(input, orbitCamera);

    orbitCameraNode = new SceneNode("Orbit-Camera-Node", control);
    scene.addNode(orbitCameraNode);
  }

  /** Initializes the perspective fly camera and attaches its control component. */
  private void setupPerspectiveCamera() {
    perspectiveCamera = new PerspectiveCamera();
    perspectiveCamera.setFarPlane(500);
    SmoothFlyByCameraControl control = new SmoothFlyByCameraControl(input, perspectiveCamera);

    perspectiveCameraNode = new SceneNode("Perspective-Camera-Node", control);

    scene.addNode(perspectiveCameraNode);
  }

  /** Adds a minimal cursor UI element to visualize mouse mode behavior. */
  private void setupUI() {
    scene.getUIRoot().addChild(new SceneNode("Cursor", new CursorComponent()));
  }

  /** Activates the perspective (fly) camera and locks the mouse. */
  private void enablePerspectiveCamera() {
    scene.setActiveCamera(perspectiveCamera);

    orbitCameraNode.setActive(false);
    perspectiveCameraNode.setActive(true);

    input.setMouseMode(MouseMode.LOCKED);
  }

  /** Activates the orbit camera and switches mouse to absolute mode. */
  private void enableOrbitCamera() {
    scene.setActiveCamera(orbitCamera);

    orbitCameraNode.setActive(true);
    perspectiveCameraNode.setActive(false);

    input.setMouseMode(MouseMode.ABSOLUTE);
  }

  @Override
  public void onUpdate(float tpf) {

    if (input.wasKeyPressed(Key.NUM_1)) {
      enablePerspectiveCamera();
    }

    if (input.wasKeyPressed(Key.NUM_2)) {
      enableOrbitCamera();
    }

    DebugDraw.drawGrid(new Vector3f(), 100, 1, 10, Color.LIGHT_GRAY, Color.GRAY);
  }

  @Override
  public void onRender(Graphics g) {
    if (scene.getActiveCamera() == orbitCamera) {
      DebugCameraRenderer.render(g, perspectiveCamera);
    }

    if (scene.getActiveCamera() == perspectiveCamera) {
      DebugCameraRenderer.render(g, orbitCamera);
    }
  }

  @Override
  public void onCleanup() {
    // Nothing to clean up in this experimental setup.
  }

  /**
   * Simple UI component that visualizes the current cursor position.
   *
   * <p>In LOCKED mode the cursor remains centered. In ABSOLUTE mode it follows the actual mouse
   * position.
   */
  private class CursorComponent extends AbstractComponent implements RenderableComponent {

    private float x;
    private float y;
    private float radius = 5;

    @Override
    public void onUpdate(float tpf) {

      if (input.getMouseMode() == MouseMode.LOCKED) {
        x = input.getScreenWidth() * 0.5f;
        y = input.getScreenHeight() * 0.5f;
        return;
      }

      x = input.getMouseX();
      y = input.getMouseY();
    }

    @Override
    public void render(Graphics g) {
      g.setColor(Color.WHITE);
      g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
    }
  }
}
