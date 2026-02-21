package demos.cube;

import demos.skybox.SkyBox;
import engine.runtime.input.Input;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.OrbitCamera;
import engine.scene.camera.OrbitCameraControl;
import engine.scene.light.AmbientLight;
import engine.scene.light.DirectionalLight;
import math.Color;
import math.Vector3f;

public class CubeScene extends Scene {

  private int gridSize = 5;
  private float partRadius = 10;
  private float margin = 0.5f;

  private Input input;



  public CubeScene(Input input) {
    super("Cube-Scene");
    this.input = input;
    setupCamera();
    setupRubiksCube();
    setupSkyBox();
    setupLight();
    setupUI();
  }

  private void setupCamera() {
    OrbitCamera camera = new OrbitCamera();
    OrbitCameraControl control = new OrbitCameraControl(input, camera);
    SceneNode cameraNode = new SceneNode("Orbit-Cam", control);
    addNode(cameraNode);
    setActiveCamera(camera);
  }

  private void setupRubiksCube() {
    float offset = ((gridSize - 1) * (partRadius * 2 + margin)) * 0.5f;

    for (int x = 0; x < gridSize; x++) {
      for (int y = 0; y < gridSize; y++) {
        for (int z = 0; z < gridSize; z++) {
          float cX = x * (partRadius * 2 + margin) - offset;
          float cY = y * (partRadius * 2 + margin) - offset;
          float cZ = z * (partRadius * 2 + margin) - offset;
          
          Direction direction = Direction.NONE;
          
          if (y == 0) 
        	  direction = Direction.TOP;
          
          setupCube(cX, cY, cZ, direction);
        }
      }
    }
  }

  private void setupCube(float x, float y, float z, Direction direction) {
	  CubePart cubePart = new CubePart(partRadius, new Vector3f(x, y, z), direction);
	  addNode(cubePart);
	  
//    Mesh3D mesh = new CubeCreator(partRadius).create();
//
//    BevelEdgesModifier modifier = new BevelEdgesModifier();
//    modifier.setWidthType(WidthType.WIDTH);
//    modifier.setAmount(0.6f);
//    modifier.modify(mesh);
//    
//    new PlanarVertexCenterModifier().modify(mesh);
//
//    Material material = new Material(Color.WHITE);
//    StaticGeometry geometry = new StaticGeometry(mesh, material);
//
//    SceneNode cubeNode = new SceneNode("Cube", geometry);
//    cubeNode.getTransform().setPosition(x, y, z);
//
//    addNode(cubeNode);
  }

  private void setupSkyBox() {
    addNode(new SkyBox(2000));
  }

  private void setupLight() {
//    Vector3f direction = new Vector3f(-0.4f, 1.0f, -0.3f);
//    DirectionalLight light = new DirectionalLight(Color.WHITE, direction);
//    addLight(light);

//    AmbientLight ambientLight = new AmbientLight(new Color(0.2f, 0.2f, 0.2f));
//    addLight(ambientLight);
  }

  private void setupUI() {
    getUIRoot().addChild(new SceneNode("Cursor", new Cursor(input)));
  }
}
