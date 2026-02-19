package engine.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.scene.Scene;

public class SystemManager {

  private final List<System> systems = new ArrayList<>();
  private final Map<Class<?>, System> systemMap = new HashMap<>();

  public void addSystem(System system, Scene scene) {
    systems.add(system);
    systemMap.put(system.getClass(), system);
    system.onAttach(scene);
  }

  public void update(float deltaTime) {
    for (System system : systems) {
      system.update(deltaTime);
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends System> T getSystem(Class<T> type) {
    return (T) systemMap.get(type);
  }
}
