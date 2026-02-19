package engine.system;

public enum UpdatePhase {
  INPUT,
  PRE_SIMULATION,
  PHYSICS,
  POST_PHYSICS,
  WORLD_UPDATE,
  POST_WORLD,
  RENDER_PREP
}
