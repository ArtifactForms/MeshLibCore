package demos.landmass;

import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.components.RoundReticle;
import engine.components.SmoothFlyByCameraControl;
import engine.components.StaticGeometry;
import engine.render.Material;
import engine.resources.Texture2D;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.PerspectiveCamera;
import engine.scene.light.DirectionalLight;
import math.Color;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.modifier.transform.CenterAtModifier;
import mesh.modifier.transform.ScaleModifier;
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
  private int levelOfDetail = 0; // Level of detail for the terrain mesh (0 - 6)
  //  private int chunkSize = 960; // TODO Note that the size has to fit LOD
  private int chunkSize = 480;
  private int chunkScale = 4;
  private DrawMode drawMode = DrawMode.COLOR_MAP;
  private Scene scene;
  //  private EndlessTerrain endlessTerrain;

  //  private LoadingScreen loadingScreen;
  private RoundReticle roundReticle;

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
    setupCamera();
    runTerrainCreation();
    //    endlessTerrain = new EndlessTerrain(scene, chunkSize * chunkScale);
  }

  private void runTerrainCreation() {
    new Thread(
            new Runnable() {
              @Override
              public void run() {
                createTerrain();
              }
            })
        .start();
  }

  /** Sets up the base scene with a background color and directional lighting. */
  private void setupScene() {
    scene = new Scene();
    scene.setBackground(Color.getColorFromInt(116, 146, 190));
    setActiveScene(scene);

    DirectionalLight directionalLight = new DirectionalLight();
    scene.addLight(directionalLight);
  }

  /** Sets up the camera with smooth fly-by controls. */
  private void setupCamera() {
    PerspectiveCamera camera = new PerspectiveCamera();
    camera.getTransform().setPosition(0, -chunkSize / 2f * chunkScale, 0);

    SmoothFlyByCameraControl cameraControl = new SmoothFlyByCameraControl(input, camera);
    cameraControl.setMoveSpeed(200);

    SceneNode cameraNode = new SceneNode();
    cameraNode.addComponent(cameraControl);
    scene.addNode(cameraNode);
    scene.setActiveCamera(camera);
  }

  /** Sets up the UI elements, such as the round reticle. */
  private void setupUI() {
    SceneNode reticleNode = new SceneNode();
    roundReticle = new RoundReticle();
    roundReticle.setActive(false);
    reticleNode.addComponent(roundReticle);
    scene.getUIRoot().addChild(reticleNode);

    SceneNode loadingScreenNode = new SceneNode();
    //    loadingScreen = new LoadingScreen();
    //    loadingScreenNode.addComponent(loadingScreen);
    scene.getUIRoot().addChild(loadingScreenNode);
  }

  /** Creates the terrain based on the selected draw mode and level of detail. */
  private void createTerrain() {
    MapGenerator generator = new MapGenerator(chunkSize);
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
    scene.getUIRoot().addChild(noiseDisplayNode);

    // Create a texture from the display and apply it to the terrain material
    Texture2D texture = display.getTexture();
    Material mapMaterial = new Material();
    mapMaterial.setDiffuseTexture(texture);

    // Generate the terrain mesh, apply transformations, and create a geometry node
    Mesh3D terrainMesh = new TerrainMeshLOD(noiseMap, levelOfDetail).getMesh();
    new ScaleModifier(chunkScale).modify(terrainMesh);
    new CenterAtModifier().modify(terrainMesh);

    StaticGeometry terrainGeometry = new StaticGeometry(terrainMesh, mapMaterial);
    SceneNode terrainNode = new SceneNode();
    terrainNode.addComponent(terrainGeometry);
    scene.addNode(terrainNode);

    // Visualize chunk
    SceneNode chunkDisplayNode = new SceneNode();
    chunkDisplayNode.addComponent(new ChunkBoxDisplay(chunkSize * chunkScale));
    scene.addNode(chunkDisplayNode);

    roundReticle.setActive(true);
    //    loadingScreen.hide();
  }

  /** Update logic (currently empty). */
  @Override
  public void onUpdate(float tpf) {
    Vector3f viewerPosition = activeScene.getActiveCamera().getTransform().getPosition();
    //    endlessTerrain.setViewerPosition(viewerPosition);
    //    endlessTerrain.update();
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
