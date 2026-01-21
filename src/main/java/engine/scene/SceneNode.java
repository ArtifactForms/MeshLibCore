package engine.scene;

import java.util.ArrayList;
import java.util.List;

import engine.components.Component;
import engine.components.RenderableComponent;
import engine.components.Transform;
import engine.scene.audio.AudioSource;
import engine.scene.audio.AudioSystem;
import math.Vector3f;
import workspace.ui.Graphics;

/**
 * Represents a single node within the scene graph.
 *
 * <p>A {@code SceneNode} is a fundamental building block in a hierarchical scene graph structure,
 * managing child nodes, components, and transformations. Scene nodes are organized in a
 * parent-child relationship, where a parent node can have multiple children, and each child can
 * have its own components and transformations.
 *
 * <p>The {@code SceneNode} manages its own transformation through a {@link Transform} object,
 * handles rendering, updates logic for itself and its children, and provides methods for managing
 * components like {@link RenderableComponent}.
 *
 * <p>Example use cases include:
 *
 * <ul>
 *   <li>Modeling a hierarchy of objects (e.g., parts of a character or modular environment pieces).
 *   <li>Managing rendering logic and transformations in a scene graph.
 *   <li>Composing behavior with reusable components for modular design.
 * </ul>
 *
 * @see Transform
 * @see Component
 * @see RenderComponents
 */
public class SceneNode {

  /** The default name assigned to a scene node if no name is provided. */
  private static final String DEFAULT_NAME = "Untitled-Node";

  /** Whether this node is active and participates in update/render cycles. */
  private boolean active;

  /** The name of this node, primarily intended for debugging and identification purposes. */
  private String name;

  /** The scene this node belongs to, or {@code null} if not attached to a scene. */
  private Scene scene;

  /** The parent node in the scene graph hierarchy, or {@code null} if this is a root node. */
  private SceneNode parent;

  /** The local transform of this node (position, rotation, scale). */
  private Transform transform;

  /** List of child nodes attached to this node. */
  private List<SceneNode> children;

  /** List of components (logic/rendering behavior) attached to this node. */
  private List<Component> components;

  /**
   * Creates a new {@code SceneNode} with the specified name.
   *
   * @param name The name to assign to the scene node.
   * @throws IllegalArgumentException if the name is {@code null}.
   */
  public SceneNode(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    this.active = true;
    this.name = name;
    this.transform = new Transform();
    this.children = new ArrayList<SceneNode>();
    this.components = new ArrayList<Component>();
    this.components.add(transform);
  }

  /**
   * Creates a new {@code SceneNode} with the specified name and initial components.
   *
   * <p>This constructor initializes the node with the given name and attaches the provided
   * components to it. The node's transformation and hierarchy management are also initialized. Any
   * {@code null} components in the array are ignored.
   *
   * @param name The name to assign to the scene node. Must not be {@code null}.
   * @param components An optional array of components to attach to the node. {@code null} elements
   *     are ignored.
   * @throws IllegalArgumentException if the name is {@code null}.
   * @see Component
   */
  public SceneNode(String name, Component... components) {
    this(name);
    for (Component component : components) {
      if (component != null) {
        addComponent(component);
      }
    }
  }

  /**
   * Constructs a new, empty {@code SceneNode} with default transformations, an empty list of
   * children, and an empty list of components.
   */
  public SceneNode() {
    this(DEFAULT_NAME);
  }

  /**
   * Renders this node and all its children recursively.
   *
   * <p>This method applies the local transformation, renders components, and traverses through all
   * child nodes to render them as well. This ensures the entire subtree rooted at this node is
   * rendered properly.
   *
   * @param g The graphics context used for rendering this node and its children.
   */
  public void render(Graphics g) {
    if (!active) return;

    g.pushMatrix();

    applyLocalTransform(g);
    renderComponents(g);

    for (SceneNode child : children) {
      child.render(g);
    }

    g.popMatrix();
  }

  /** Applies the local transformation to the graphics context. */
  private void applyLocalTransform(Graphics g) {
    getTransform().apply(g);
  }

  /**
   * Renders all associated {@link RenderableComponent} instances attached to this node.
   *
   * <p>This method iterates through all render components and calls their respective rendering
   * logic.
   *
   * @param g The graphics context used for rendering.
   */
  protected void renderComponents(Graphics g) {
    if (!active) return;
    for (RenderableComponent renderer : getRenderComponents()) {
      if (renderer.isActive()) renderer.render(g);
    }
  }

  /**
   * Updates this node's logic and propagates updates to children nodes.
   *
   * @param tpf The time per frame in seconds (delta time).
   */
  public void update(float tpf) {
    if (!active) return;
    updateComponents(tpf);
    updateChildren(tpf);
  }

  /**
   * Updates all {@link AudioSource} components attached to this node and its children.
   *
   * <p>This method propagates audio updates through the scene graph hierarchy, allowing spatial
   * audio sources to be updated relative to the listener and scene state.
   *
   * @param audioSystem The {@link AudioSystem} responsible for processing audio sources.
   */
  public void updateAudio(AudioSystem audioSystem) {
    if (!active) return;
    audioSystem.update(getComponents(AudioSource.class));
    for (SceneNode child : children) {
      child.updateAudio(audioSystem);
    }
  }

  /**
   * Updates all components attached to this node.
   *
   * @param tpf The time per frame in seconds.
   */
  protected void updateComponents(float tpf) {
    if (!active) return;
    for (Component component : new ArrayList<>(components)) {
      component.update(tpf);
    }
  }

  /**
   * Updates all child nodes of this node recursively.
   *
   * @param tpf The time per frame in seconds.
   */
  protected void updateChildren(float tpf) {
    if (!active) return;
    for (SceneNode child : children) {
      child.update(tpf);
    }
  }

  /**
   * Cleans up this node's resources, components, and children recursively.
   *
   * <p>Each component and child is cleaned up to ensure no resources are left hanging, preventing
   * memory leaks or unwanted behavior.
   */
  public void cleanup() {
    for (Component component : components) {
      try {
        component.onDetach();
        component.setOwner(null);
      } catch (Exception e) {
        System.err.println("Error cleaning up component: " + e.getMessage());
      }
    }

    for (SceneNode child : children) {
      child.cleanup();
    }

    components.clear();
    children.clear();
  }

  /**
   * Accepts a {@link SceneNodeVisitor} and traverses this node and all of its children in
   * depth-first order.
   *
   * <p>This method implements the Visitor pattern for the scene graph. It allows external systems
   * (such as raycasting, culling, or editor queries) to traverse the scene hierarchy without
   * coupling those systems directly to the {@code SceneNode} implementation.
   *
   * <p>The visitor is first applied to this node, then recursively to all child nodes.
   *
   * @param visitor the visitor to apply to this node and its descendants
   * @throws IllegalArgumentException if {@code visitor} is {@code null}
   */
  public void accept(SceneNodeVisitor visitor) {
    if (visitor == null) {
      throw new IllegalArgumentException("Visitorc cannot be null.");
    }

    visitor.visit(this);
    for (SceneNode child : children) {
      child.accept(visitor);
    }
  }

  /**
   * Adds a child node to this node's hierarchy.
   *
   * <p>Prevents the addition of null nodes and ensures no duplicate child is added.
   *
   * @param child The child {@code SceneNode} to add.
   */
  public void addChild(SceneNode child) {
    if (child == null) {
      throw new IllegalArgumentException("Child node cannot be null.");
    }
    if (children.contains(child)) {
      return;
    }
    child.parent = this;
    children.add(child);
  }

  /**
   * Removes a child node from this node's hierarchy.
   *
   * <p>Cleans up the child before removing it to ensure no resources are leaked.
   *
   * @param child The child {@code SceneNode} to remove.
   */
  public void removeChild(SceneNode child) {
    if (child == null) {
      return;
    }
    if (!children.contains(child)) {
      return;
    }
    child.cleanup();
    child.parent = null;
    children.remove(child);
  }

  /**
   * Adds a component to this node.
   *
   * <p>Ensures that duplicate components of the same instance are not added.
   *
   * @param component The {@link Component} to add.
   * @throws IllegalArgumentException if the component is null.
   */
  public void addComponent(Component component) {
    if (component == null) {
      throw new IllegalArgumentException("Component cannot be null.");
    }
    if (!components.contains(component)) {
      components.add(component);
      component.setOwner(this);
      component.onAttach();
    }
  }

  /**
   * Removes a component from this node.
   *
   * <p>If the component is found, it is cleaned up before removal.
   *
   * @param component The {@link Component} to remove.
   * @throws IllegalArgumentException if the component is null.
   */
  public void removeComponent(Component component) {
    if (component == null) {
      throw new IllegalArgumentException("Component cannot be null.");
    }
    if (components.contains(component)) {
      components.remove(component);
      component.onDetach();
      component.setOwner(null);
    }
  }

  /**
   * Retrieves the first component of the specified type attached to this node.
   *
   * @param componentClass The class type of the component to retrieve.
   * @param <T> The type of the component.
   * @return The first matching component, or {@code null} if none exists.
   */
  public <T extends Component> T getComponent(Class<T> componentClass) {
    for (Component component : components) {
      if (componentClass.isInstance(component)) {
        return componentClass.cast(component);
      }
    }
    return null;
  }

  /**
   * Retrieves a list of components of a specific type attached to this node.
   *
   * <p>Enables querying for specific types of behavior or functionality attached to a node.
   *
   * @param componentClass The class type of the component to retrieve.
   * @param <T> The type of component to search for.
   * @return A list of components matching the specified type.
   */
  public <T extends Component> List<T> getComponents(Class<T> componentClass) {
    List<T> result = new ArrayList<>();
    for (Component component : components) {
      if (componentClass.isInstance(component)) {
        result.add(componentClass.cast(component));
      }
    }
    return result;
  }

  /**
   * Retrieves all render components for this node.
   *
   * @return A list of {@link RenderableComponent} instances associated with this node.
   */
  public List<RenderableComponent> getRenderComponents() {
    return getComponents(RenderableComponent.class);
  }

  /**
   * Returns the {@link Scene} this node belongs to.
   *
   * <p>The scene reference is resolved by traversing to the root {@code SceneNode} of the
   * hierarchy. All nodes within the same scene graph share the same {@link Scene} instance.
   *
   * <p>This method is useful for components or systems that need access to global scene
   * functionality such as spawning nodes, querying lights, or interacting with the active camera.
   *
   * @return the {@link Scene} this node is part of, or {@code null} if the node is not attached to
   *     a scene
   */
  public Scene getScene() {
    if (scene != null) return scene;
    if (isRoot() && scene == null) return null;
    return getRoot().getScene();
  }

  /**
   * Assigns the scene this node belongs to.
   *
   * <p>This method is intended for internal use by the {@link Scene} when nodes are added or
   * removed.
   *
   * @param scene The scene this node is part of.
   */
  protected void setScene(Scene scene) {
    this.scene = scene;
  }

  /**
   * Detaches this node from its parent or scene.
   *
   * <p>If the node has a parent, it will be removed from the parent's children list. If the node is
   * a root-level node, it will be removed from the scene entirely. In both cases, cleanup is
   * performed as needed.
   */
  public void detach() {
    if (parent != null) {
      parent.removeChild(this);
    } else if (scene != null) {
      scene.removeNode(this);
      cleanup();
    }
  }

  /**
   * Retrieves the root node in the scene graph hierarchy.
   *
   * @return The root {@code SceneNode} in the hierarchy.
   */
  public SceneNode getRoot() {
    if (parent == null) {
      return this;
    }
    return parent.getRoot();
  }

  /**
   * Checks whether this node is the root node in the hierarchy.
   *
   * @return {@code true} if this node is the root; {@code false} otherwise.
   */
  public boolean isRoot() {
    return parent == null;
  }

  /**
   * Checks whether this node is a leaf node (has no children).
   *
   * @return {@code true} if this node has no children; {@code false} otherwise.
   */
  public boolean isLeaf() {
    return children.isEmpty();
  }

  /**
   * Retrieves the {@link Transform} component associated with this node.
   *
   * <p>The transform defines the node's position, rotation, and scale in the scene graph.
   *
   * @return The transform component.
   */
  public Transform getTransform() {
    return transform;
  }

  /**
   * Computes and returns the world-space position of this {@link SceneNode}.
   *
   * <p>The world position is derived by recursively accumulating the local positions of this node
   * and all of its parent nodes up the scene graph hierarchy.
   *
   * <p>If this node has no parent, its world position is identical to its local position.
   * Otherwise, the world position is computed as:
   *
   * <pre>
   * worldPosition = parent.worldPosition + localPosition
   * </pre>
   *
   * <p>This method currently accounts for translation only. Rotation and scaling are not applied
   * and may be incorporated in a future world-transform implementation.
   *
   * <p>A new {@link Vector3f} instance is returned on each call to avoid leaking internal state.
   *
   * @return the world-space position of this node
   */
  public Vector3f getWorldPosition() {
    if (parent == null) {
      return transform.getPosition();
    }
    return parent.getWorldPosition().add(transform.getPosition());
  }

  /**
   * Retrieves the name of this {@code SceneNode}.
   *
   * @return The name of the node.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this {@code SceneNode}.
   *
   * @param name The new name to assign to the node.
   * @throws IllegalArgumentException if the name is {@code null}.
   */
  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    this.name = name;
  }

  /**
   * Retrieves the active state of this node.
   *
   * <p>This method returns whether the node is active or not. An active node is typically involved
   * in updates, rendering, and other game logic, whereas an inactive node may be ignored during
   * these processes.
   *
   * @return {@code true} if the node is active, {@code false} if it is inactive.
   */
  public boolean isActive() {
    return active;
  }

  /**
   * Sets the active state of this node.
   *
   * <p>This method changes the active state of the node. If the node's active state is already in
   * the desired state, it does nothing to avoid unnecessary changes. When the active state is
   * updated, it also propagates the change to the node's components and child nodes.
   *
   * @param active The desired active state for this node. {@code true} sets the node as active,
   *     {@code false} deactivates it.
   */
  public void setActive(boolean active) {
    // Skip if already in the desired state
    if (active == this.active) return;
    this.active = active;
    setComponentsActive();
    setChildrenActive();
  }

  /**
   * Sets the active state for all child nodes of this node.
   *
   * <p>This method recursively sets the active state of each child node in the hierarchy. If the
   * parent node is set to active, all of its child nodes will also be activated, and vice versa.
   * This ensures that all child nodes are consistently updated with the parent node's active state.
   */
  protected void setChildrenActive() {
    for (SceneNode child : children) {
      child.setActive(active);
    }
  }

  /**
   * Sets the active state for all components attached to this node.
   *
   * <p>This method updates the active state of each component attached to the node. This ensures
   * that all components are either activated or deactivated in sync with the node itself, depending
   * on the current active state of the node.
   */
  protected void setComponentsActive() {
    for (Component component : components) {
      component.setActive(active);
    }
  }
}
