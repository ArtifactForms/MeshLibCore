package demos.cube;

import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.runtime.debug.core.DebugDraw;
import engine.runtime.input.Key;
import engine.scene.Scene;
import engine.scene.nodes.DefaultTestCube;
import math.Color;
import math.Vector3f;
import workspace.ui.Graphics;

public class CubeApp extends BasicApplication {

  public static void main(String[] args) {
    CubeApp app = new CubeApp();
    app.launch(ApplicationSettings.defaultSettings().setFullscreen(true));
  }

  private boolean debugDrawVisible = false;
  private Scene scene;

  @Override
  public void onInitialize() {
    scene = new CubeScene(input);
    
    DefaultTestCube cube = new DefaultTestCube(10);
    cube.getTransform().setPosition(0, -60, 0);
    scene.addNode(cube);
    
    setActiveScene(scene);
    refreshDebug();
  }

  public void refreshDebug() {
    setDebugDrawVisible(debugDrawVisible);
  }

  @Override
  public void onUpdate(float tpf) {
    if (input.wasKeyReleased(Key.NUM_1)) {
      debugDrawVisible = !debugDrawVisible;
      refreshDebug();
    }
    DebugDraw.drawGrid(Vector3f.ZERO, 500, 1, 5, Color.WHITE, Color.LIGHT_GRAY);
  }

  @Override
  public void onRender(Graphics g) {}

  @Override
  public void onCleanup() {}
}
