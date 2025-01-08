package engine.demos.landmass;

import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.components.Geometry;
import engine.components.RoundReticle;
import engine.components.SmoothFlyByCameraControl;
import engine.render.Material;
import engine.resources.Texture2D;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.PerspectiveCamera;
import engine.scene.light.DirectionalLight;
import math.Color;
import mesh.Mesh3D;
import mesh.modifier.CenterAtModifier;
import mesh.modifier.ScaleModifier;
import workspace.ui.Graphics;

/**
 * A demo showcasing a procedurally generated landmass using Perlin noise. This demo features a
 * terrain mesh, directional lighting, and smooth fly-by camera controls. It is based on the Unity
 * tutorial series "Procedural Landmass Generation" by Sebastian Lague on YouTube.
 *
 * <p>The Unity scripts have been adapted and tweaked to fit this engine's architecture and existing
 * mesh framework.
 *
 * <p>Special thanks to Sebastian Lague for the excellent tutorial series, which provided valuable
 * insights and inspiration for this implementation.
 *
 * <p>For more details, watch the tutorial series: <a
 * href=>https://www.youtube.com/playlist?list=PLFt_AvWsXl0eBW2EiBtl_sxmDtSgZBxB3>Procedural
 * Landmass Generation</a>
 */
public class ProceduralLandmassDemo extends BasicApplication {

  /** Enum to define the rendering mode of the terrain. */
  public enum DrawMode {
    NOISE_MAP,
    COLOR_MAP
  }

  // Configuration fields
  private int levelOfDetail = 1; // Level of detail for the terrain mesh (0 - 6)
  private DrawMode drawMode = DrawMode.COLOR_MAP;
  private Scene scene;

  public static void main(String[] args) {
    ProceduralLandmassDemo application = new ProceduralLandmassDemo();
    ApplicationSettings settings = ApplicationSettings.defaultSettings();
    settings.setFullscreen(true);
    application.launch(settings);
  }

  /** Initializes the demo scene, including terrain generation, lighting, and UI components. */
  @Override
  public void onInitialize() {
    setupScene();
    setupUI();
    createTerrain();
    createCamera();
  }

  /** Sets up the base scene with a background color and directional lighting. */
  private void setupScene() {
    scene = new Scene();
    scene.setBackground(Color.getColorFromInt(116, 146, 190));
    setActiveScene(scene);

    DirectionalLight directionalLight = new DirectionalLight();
    scene.addLight(directionalLight);
  }

  /** Sets up the UI elements, such as the round reticle. */
  private void setupUI() {
    SceneNode reticleNode = new SceneNode();
    reticleNode.addComponent(new RoundReticle());
    rootUI.addChild(reticleNode);
  }

  /** Creates the terrain based on the selected draw mode and level of detail. */
  private void createTerrain() {
    MapGenerator generator = new MapGenerator();
    float[][] noiseMap = generator.getHeightMap();

    // Create and display the noise map or color map based on the draw mode
    NoiseMapDisplay display = new NoiseMapDisplay(noiseMap.length, noiseMap[0].length);
    if (drawMode == DrawMode.COLOR_MAP) {
      display.setPixels(generator.createColorMap());
    } else {
      display.setPixels(generator.createNoiseMap());
    }
    SceneNode noiseDisplayNode = new SceneNode();
    noiseDisplayNode.addComponent(display);
    scene.addNode(noiseDisplayNode);

    // Create a texture from the display and apply it to the terrain material
    Texture2D texture = display.getTexture();
    Material mapMaterial = new Material.Builder().setDiffuseTexture(texture).build();

    // Generate the terrain mesh, apply transformations, and create a geometry node
    Mesh3D terrainMesh = new TerrainMeshLOD(noiseMap, levelOfDetail).getMesh();
    terrainMesh.apply(new ScaleModifier(3));
    terrainMesh.apply(new CenterAtModifier());

    Geometry terrainGeometry = new Geometry(terrainMesh, mapMaterial);
    SceneNode terrainNode = new SceneNode();
    terrainNode.addComponent(terrainGeometry);
    scene.addNode(terrainNode);
  }

  /** Sets up the camera with smooth fly-by controls. */
  private void createCamera() {
    PerspectiveCamera camera = new PerspectiveCamera();
    SmoothFlyByCameraControl cameraControl = new SmoothFlyByCameraControl(input, camera);
    cameraControl.setMoveSpeed(70);

    SceneNode cameraNode = new SceneNode();
    cameraNode.addComponent(cameraControl);
    scene.addNode(cameraNode);
    scene.setActiveCamera(camera);
  }

  /** Update logic (currently empty). */
  @Override
  public void onUpdate(float tpf) {
    // No update logic for this demo
  }

  /** Render logic (currently empty). */
  @Override
  public void onRender(Graphics g) {
    // No custom rendering for this demo
  }

  /** Cleanup logic (currently empty). */
  @Override
  public void onCleanup() {
    // No cleanup logic for this demo
  }
}
