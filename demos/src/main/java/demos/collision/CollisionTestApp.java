package demos.collision;

import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import workspace.ui.Graphics;

public class CollisionTestApp extends BasicApplication {

  public static void main(String[] args) {
    CollisionTestApp app = new CollisionTestApp();
    app.launch(ApplicationSettings.defaultSettings().setFullscreen(true));
  }

  @Override
  public void onInitialize() {
    CollisionTestScene scene = new CollisionTestScene();
    scene.init(input);
    setActiveScene(scene);
  }

  @Override
  public void onUpdate(float tpf) {
    // Do nothing
  }

  @Override
  public void onRender(Graphics g) {
    // Do nothing
  }

  @Override
  public void onCleanup() {
    // Do nothing
  }
}
