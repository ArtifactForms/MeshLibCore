package engine.components;

import engine.scene.SceneNode;

/**
 * Represents a generic component within the scene graph architecture.
 *
 * <p>Components are modular pieces of behavior or functionality that can be added to {@link
 * SceneNode} instances. They encapsulate logic, rendering, or other behaviors, following a
 * component-based design pattern.
 *
 * <p>Each component should manage its lifecycle, with {@code onAttach()}, {@code update()}, and
 * {@code onDetach()} methods, allowing nodes to manage their behavior lifecycle cleanly.
 */
public interface Component {

  /**
   * Sets the owning {@link SceneNode} for this component.
   *
   * <p>This is called when the component is added to a {@link SceneNode}. The owning node serves as
   * the context within which this component operates, allowing it to interact with other components
   * or node transformations.
   *
   * @param owner The {@link SceneNode} that owns this component; cannot be null.
   */
  void setOwner(SceneNode owner);

  /**
   * Updates the component's logic every frame.
   *
   * <p>Called once per frame during the scene's update cycle. The time-per-frame (tpf) is passed in
   * to allow for time-based animations or logic that depends on frame timing.
   *
   * @param tpf The time per frame in seconds (time delta) since the last update.
   */
  void update(float tpf);

  /**
   * Called when the component is attached to a {@link SceneNode}.
   *
   * <p>This allows the component to set up necessary resources, state, or perform other preparatory
   * work specific to being added to a node.
   */
  void onAttach();

  /**
   * Called when the component is detached from a {@link SceneNode}.
   *
   * <p>This ensures no memory is leaked, threads are terminated, or other resources are left
   * hanging by cleaning up internal state and releasing references.
   */
  void onDetach();
}
