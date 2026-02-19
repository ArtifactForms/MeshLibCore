package engine.components;

import engine.scene.Scene;
import engine.scene.SceneNode;

/**
 * Represents a generic component within the scene graph architecture.
 *
 * <p>Components are modular pieces of behavior or functionality that can be added to {@link
 * SceneNode} instances. They encapsulate logic, rendering, or other behaviors, following a
 * component-based design pattern.
 *
 * <p>Each component should manage its lifecycle with {@code onAttach()}, {@code update(float)}, and
 * {@code onDetach()} methods, allowing nodes to manage component behavior efficiently.
 */
public interface Component {

  /**
   * Sets the active state of this component.
   *
   * <p>An active component will participate in updates, rendering, or other operations during the
   * scene's lifecycle. Inactive components are skipped in these processes, allowing for optimized
   * performance and dynamic behavior control.
   *
   * @param active A boolean value where {@code true} sets the component as active, and {@code
   *     false} sets it as inactive.
   */
  void setActive(boolean active);

  /**
   * Returns the active state of this component.
   *
   * <p>An active component participates in updates and other operations. If a component is
   * inactive, it is effectively disabled and will not be updated or rendered by the owning {@link
   * SceneNode}.
   *
   * @return {@code true} if the component is active, {@code false} otherwise.
   */
  boolean isActive();

  /**
   * Returns the owning {@link SceneNode} of this component.
   *
   * <p>This method provides access to the {@link SceneNode} that owns this component, allowing the
   * component to interact with its parent node and other components within the scene graph.
   *
   * <p>If the component has not been attached to a {@link SceneNode}, this method may return {@code
   * null}.
   *
   * @return The {@link SceneNode} that owns this component, or {@code null} if not attached.
   */
  SceneNode getOwner();

  /**
   * Sets the owning {@link SceneNode} for this component.
   *
   * <p>This method is called when the component is added to a {@link SceneNode}. The owning node
   * provides context and access to the scene graph, allowing the component to interact with other
   * components, transformations, and scene properties.
   *
   * @param owner The {@link SceneNode} that owns this component; must not be {@code null}.
   */
  void setOwner(SceneNode owner);

  /**
   * Updates the component during the scene's update cycle.
   *
   * <p>This method is responsible for invoking the component's update logic if it is active. It
   * checks the component's active state and then calls {@link #onUpdate(float)} to perform any
   * custom behavior defined by the component.
   *
   * <p>The {@link AbstractComponent} base class typically implements this method to handle active
   * state checks, ensuring that {@link #onUpdate(float)} is only called when the component is
   * enabled.
   *
   * <p>Subclasses should override {@link #onUpdate(float)} to define their specific update logic,
   * rather than overriding this method directly.
   *
   * @param tpf The time per frame in seconds since the last update.
   */
  void update(float tpf);

  /**
   * Called during the scene's update cycle to perform component-specific logic.
   *
   * <p>This method is intended to be overridden by components to define their unique behavior.
   * Unlike {@code update(float)}, which is provided by the {@link AbstractComponent} base class,
   * this method does not need to handle active state checks. It is only called if the component is
   * active.
   *
   * <p>Implementations should ensure that the logic is efficient and avoids unnecessary
   * computations to maintain performance.
   *
   * @param tpf The time per frame in seconds since the last update.
   */
  void onUpdate(float tpf);

  /**
   * Called when the component is attached to a {@link SceneNode}.
   *
   * <p>This method provides an opportunity to perform any initialization or setup required when the
   * component becomes part of a node. It can be used to register listeners, allocate resources, or
   * initialize state.
   *
   * <p>Components should avoid performing heavy computations in this method to minimize performance
   * impact during scene setup.
   */
  void onAttach();

  /**
   * Called when the component's owning {@link SceneNode} is added to a {@link Scene}.
   *
   * <p>While {@link #onAttach()} is triggered when a component is added to a node, this method is
   * specifically triggered when that node (or its parent hierarchy) becomes part of an active scene
   * graph.
   *
   * <p>This is the ideal place to perform setup that requires access to scene-wide resources. If
   * the node is moved from one scene to another, this method will be called again with the new
   * scene context.
   *
   * @param scene The {@link Scene} instance this component is now a part of.
   */
  void onAttachToScene(Scene scene);

  /**
   * Called when the component is detached from a {@link SceneNode}.
   *
   * <p>This method allows the component to clean up any resources, references, or listeners that
   * were established during its lifecycle. It is essential to release resources here to avoid
   * memory leaks or lingering state that could impact performance or stability.
   *
   * <p>Common tasks include unregistering listeners, stopping threads, or nullifying references to
   * other objects in the scene.
   */
  void onDetach();
}
