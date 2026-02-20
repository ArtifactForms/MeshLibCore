package engine.system;

import java.util.EnumSet;

import engine.scene.Scene;

public interface SceneSystem {

  void onAttach(Scene scene);

  EnumSet<UpdatePhase> getPhases();

  void update(UpdatePhase phase, float deltaTime);
}
