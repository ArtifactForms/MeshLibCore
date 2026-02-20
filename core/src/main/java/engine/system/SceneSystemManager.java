package engine.system;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.scene.Scene;

public class SceneSystemManager {

  private final List<SceneSystem> systems = new ArrayList<>();
  
  private final Map<Class<?>, SceneSystem> systemMap = new HashMap<>();

  private final Map<UpdatePhase, List<SceneSystem>> systemsByPhase =
      new EnumMap<>(UpdatePhase.class);

  public SceneSystemManager() {
    for (UpdatePhase phase : UpdatePhase.values()) {
      systemsByPhase.put(phase, new ArrayList<>());
    }
  }

  public void addSystem(SceneSystem system, Scene scene) {
    systems.add(system);
    systemMap.put(system.getClass(), system);

    EnumSet<UpdatePhase> phases = system.getPhases();
    for (UpdatePhase phase : phases) {
      systemsByPhase.get(phase).add(system);
    }

    system.onAttach(scene);
  }

  public void updatePhase(UpdatePhase phase, float deltaTime) {
    List<SceneSystem> phaseSystems = systemsByPhase.get(phase);

    for (SceneSystem system : phaseSystems) {
      system.update(phase, deltaTime);
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends SceneSystem> T getSystem(Class<T> type) {
    return (T) systemMap.get(type);
  }
}
