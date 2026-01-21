package engine.components;

import engine.scene.SceneNode;

/**
 * Abstract base class for all components in the scene graph.
 *
 * <p>This class provides a shared implementation of common functionality across all components,
 * reducing boilerplate code and centralizing shared logic for ease of maintenance.
 *
 * <p>Components extending this class inherit basic lifecycle management, including activation state
 * handling and owner node reference management. The {@link #update(float)} method includes a check
 * for the active state, ensuring that inactive components are automatically skipped during the
 * update cycle.
 */
public abstract class AbstractComponent implements Component {

  /** Indicates whether the component is currently active. */
  protected boolean active = true;

  /** Reference to the owning {@link SceneNode}. */
  protected SceneNode owner;

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}

  @Override
  public void onUpdate(float tpf) {}

  @Override
  public void setOwner(SceneNode owner) {
    this.owner = owner;
  }

  @Override
  public SceneNode getOwner() {
    return owner;
  }

  @Override
  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public boolean isActive() {
    return active;
  }

  @Override
  public void update(float tpf) {
    if (active) {
      onUpdate(tpf);
    }
  }
}
