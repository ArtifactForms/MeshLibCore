package engine.system;

import engine.scene.Scene;

public abstract class AbstractSceneSystem implements SceneSystem {

  private Scene scene;

  public void onAttach(Scene scene) {
    this.scene = scene;
    onInitialize();
  }

  protected void onInitialize() {
    // optional override hook
  }

  public abstract void update(UpdatePhase phase, float deltaTime);

  protected Scene getScene() {
    return scene;
  }
}
