package demos.collision.main;

import demos.collision.CollisionTestScene;
import demos.collision.Settings;
import demos.collision.TestEnvironmentFactory;
import demos.collision.scene.EnvironmentFactory2;
import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.rendering.Graphics;
import engine.scene.SceneNode;

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
    scene.addNode(new TestEnvironmentFactory().createEnvironment(settings));
    
    SceneNode pillars = new EnvironmentFactory2().create();
    pillars.getTransform().translate(0, 0, 60);
    
    scene.addNode(pillars);
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
