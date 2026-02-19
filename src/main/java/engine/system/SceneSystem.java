package engine.system;

import engine.scene.Scene;

public interface System {

  void update(float deltaTime);
  
  void onAttach(Scene scene);
  
}
