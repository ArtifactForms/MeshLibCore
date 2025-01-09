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
  protected boolean active;

  /** Reference to the owning {@link SceneNode}. */
  protected SceneNode owner;

  /**
   * Sets the owner (parent node) of this component.
   *
   * <p>This method is called when the component is added to a {@link SceneNode}. It stores a
   * reference to the owning node, which can be used for interactions with other components or scene
   * graph operations.
   *
   * @param owner The {@link SceneNode} that owns this component; must not be {@code null}.
   */
  @Override
  public void setOwner(SceneNode owner) {
    this.owner = owner;
  }

  /**
   * Retrieves the owning {@link SceneNode} of this component.
   *
   * <p>This method provides convenient access to the parent node, allowing components to interact
   * with the scene graph or access shared properties such as transformations or child nodes.
   *
   * @return The {@link SceneNode} instance that owns this component, or {@code null} if not set.
   */
  @Override
  public SceneNode getOwner() {
    return owner;
  }

  /**
   * Sets the active state of this component.
   *
   * <p>An active component participates in updates and other operations within the scene's
   * lifecycle. Inactive components are effectively disabled and will not be processed during
   * updates or rendering.
   *
   * @param active A boolean value where {@code true} sets the component as active, and {@code
   *     false} sets it as inactive.
   */
  @Override
  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * Returns the active state of this component.
   *
   * <p>If the component is active, it will be included in updates and other scene lifecycle
   * processes. Inactive components are skipped to optimize performance.
   *
   * @return {@code true} if the component is active, {@code false} otherwise.
   */
  @Override
  public boolean isActive() {
    return active;
  }

  /**
   * Updates this component during the scene's update cycle.
   *
   * <p>This method checks if the component is active before invoking the {@link #onUpdate(float)}
   * method. If the component is inactive, the update logic is skipped.
   *
   * @param tpf The time per frame in seconds (time delta) since the last update.
   */
  @Override
  public void update(float tpf) {
    if (active) {
      onUpdate(tpf);
    }
  }

  /**
   * Abstract method to be implemented by subclasses to define component-specific update logic.
   *
   * <p>This method is only called if the component is active. Subclasses should implement their
   * frame-specific logic here, such as animations, physics calculations, or interactions with other
   * components.
   *
   * <p>Example use cases include:
   *
   * <ul>
   *   <li>Animating properties of the node (e.g., position, rotation, scale)
   *   <li>Handling physics calculations for the node
   *   <li>Interacting with other components or systems
   * </ul>
   *
   * @param tpf The time per frame in seconds (time delta) since the last update.
   */
  public abstract void onUpdate(float tpf);
}
