package sandbox;

import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.components.RoundReticle;
import engine.components.StaticGeometry;
import engine.physics.ray.RaycastHit;
import engine.physics.ray.RaycastQuery;
import engine.physics.ray.Raycaster;
import engine.rendering.Graphics;
import engine.rendering.Material;
import engine.runtime.debug.core.DebugDraw;
import engine.runtime.input.Input;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.light.DirectionalLight;
import math.Bounds;
import math.Color;
import math.Ray3f;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;

/**
 *
 *
 * <h2>RayShootApp</h2>
 *
 * <p>Experimental application scenario for validating raycasting, scene traversal, bounds
 * calculation and runtime node destruction.
 *
 * <p><strong>Important:</strong>
 *
 * <ul>
 *   <li>This is NOT production gameplay code.
 *   <li>This is NOT a reference architecture.
 *   <li>This class exists purely for system exploration and validation.
 * </ul>
 *
 * <p>The following systems are exercised:
 *
 * <ul>
 *   <li>Mesh generation via {@link CubeCreator}
 *   <li>Geometry rendering
 *   <li>Directional lighting
 *   <li>Ray construction from active camera
 *   <li>RaycastQuery + RaycastHit processing
 *   <li>World-space bounds computation
 *   <li>Debug rendering (grid + bounds)
 *   <li>Runtime destruction of SceneNodes
 * </ul>
 *
 * <p>Interaction:
 *
 * <ul>
 *   <li>A ray is cast every frame from the screen center.
 *   <li>If a cube is hit, its bounds are visualized.
 *   <li>Releasing the left mouse button destroys the targeted cube.
 * </ul>
 *
 * <p>The goal of this playground is to ensure correct integration between camera projection, scene
 * traversal, physics-style queries and node lifecycle.
 */
public class RayShootApp extends BasicApplication {

  public static void main(String[] args) {
    RayShootApp app = new RayShootApp();
    app.launch(ApplicationSettings.defaultSettings().setFullscreen(true));
  }

  /** Defines half-extent of the generated cube grid. */
  private static final int GRID_SIZE = 11;

  private Scene scene;

  /** Used to detect mouse release events. */
  private boolean lastMouseDown;

  @Override
  public void onInitialize() {
    setupScene();
    setupTargets();
    setupUI();
  }

  /** Initializes the scene with background color and lighting. */
  private void setupScene() {
    scene = new Scene();
    scene.setBackground(Color.DARK_GRAY);
    scene.addLight(new DirectionalLight());
    setActiveScene(scene);
  }

  /** Creates a grid of target cubes in world space. */
  private void setupTargets() {
    for (int x = -GRID_SIZE; x <= GRID_SIZE; x++) {
      for (int z = -GRID_SIZE; z <= GRID_SIZE; z++) {
        createTargetBlock(x * 2, 0, z * 2);
      }
    }
  }

  /** Creates a single target cube at the given position. */
  private void createTargetBlock(float x, float y, float z) {
    float radius = 0.5f;

    Mesh3D mesh = new CubeCreator(radius).create();
    Material material = new Material(Color.BLUE);
    StaticGeometry geometry = new StaticGeometry(mesh, material);

    SceneNode node = new SceneNode("Cube", geometry);
    node.getTransform().setPosition(x, y, z);
    scene.addNode(node);
  }

  /** Adds a simple crosshair reticle to the UI. */
  private void setupUI() {
    scene.getUIRoot().addChild(new SceneNode("Reticle", new RoundReticle()));
  }

  @Override
  public void onUpdate(float tpf) {

    // Draw reference grid
    DebugDraw.drawGrid(Vector3f.ZERO, 100, 0.5f, 5, Color.LIGHT_GRAY, Color.GRAY);

    // Create ray from camera center
    Ray3f ray = Raycaster.crossHairRay(scene.getActiveCamera());

    // Execute raycast query
    RaycastQuery query = new RaycastQuery(ray);
    scene.visitRootNodes(query);
    RaycastHit hit = query.getResult();

    if (hit != null) {
      SceneNode target = hit.getTarget();

      StaticGeometry geometry = target.getComponent(StaticGeometry.class);

      Bounds bounds = geometry.getWorldBounds();

      // Visualize hit bounds
      DebugDraw.drawBounds(bounds, Color.WHITE);

      // Destroy on mouse release
      if (lastMouseDown && !input.isMousePressed(Input.LEFT)) {
        target.destroy();
      }
    }

    lastMouseDown = input.isMousePressed(Input.LEFT);
  }

  @Override
  public void onRender(Graphics g) {
    // No additional rendering in this experimental setup.
  }

  @Override
  public void onCleanup() {
    // Nothing to clean up in this experimental setup.
  }
}
