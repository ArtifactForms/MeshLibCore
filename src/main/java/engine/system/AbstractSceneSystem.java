package engine.system;

import engine.scene.Scene;

public abstract class AbstractSystem implements System {

  private Scene scene;

  public void onAttach(Scene scene) {
    this.scene = scene;
    onInitialize();
  }

  protected void onInitialize() {
    // optional override hook
  }

  public abstract void update(float deltaTime);

  protected Scene getScene() {
    return scene;
  }
}
