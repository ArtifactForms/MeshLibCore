package client.scene;

import engine.application.BasicApplication;
import engine.scene.Scene;

public class SceneManager {

  private BasicApplication application;

  public SceneManager(BasicApplication application) {
    if (application == null) {
      throw new IllegalArgumentException("Application cannot be null.");
    }
    this.application = application;
  }

  public void setActiveScene(Scene scene) {
    application.setActiveScene(scene);
  }
}
