package demos.collision.main;

import demos.collision.CollisionTestScene;
import demos.collision.Settings;
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
    Settings settings = new Settings();
    CollisionTestScene scene = new CollisionTestScene();
    scene.init(input, settings);
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
