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
 * parent-child relationship, where a parent node can have multiple children.
 *
 * <p>The node lifecycle is explicitly divided into:
 *
 * <ul>
 *   <li><b>Active state</b> – controlled via {@link #setActive(boolean)}
 *   <li><b>Detachment</b> – removing the node from the hierarchy
 *   <li><b>Destruction</b> – logical removal from the scene graph
 *   <li><b>Cleanup</b> – final resource release
 * </ul>
 *
 * <p>This separation allows safe multithreaded updates, editor tooling, and predictable scene
 * management.
 *
 * @see Transform
 * @see Component
 */
public class SceneNode {

  /** Default name assigned to a scene node if no name is provided. */
  private static final String DEFAULT_NAME = "Untitled-Node";

  /** Whether this node is active and participates in update/render cycles. */
  private boolean active;

  /** Whether this node has been destroyed and is no longer valid. */
  private boolean destroyed;

  /** The name of this node, primarily used for debugging and identification. */
  private String name;

  /** The scene this node belongs to, or {@code null} if detached. */
  private Scene scene;

  /** The parent node in the hierarchy, or {@code null} if this is a root node. */
  private SceneNode parent;

  /** The local transform of this node. */
  private final Transform transform;

  /** Child nodes attached to this node. */
  private final List<SceneNode> children;

  /** Components attached to this node. */
  private final List<Component> components;

  /**
   * Creates a new {@code SceneNode} with the specified name.
   *
   * @param name the name of the node
   * @throws IllegalArgumentException if {@code name} is {@code null}
   */
  public SceneNode(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    this.active = true;
    this.destroyed = false;
    this.name = name;
    this.transform = new Transform();
    this.children = new ArrayList<>();
    this.components = new ArrayList<>();
    this.components.add(transform);
  }

  /** Creates a new {@code SceneNode} with a default name. */
  public SceneNode() {
    this(DEFAULT_NAME);
  }

  /**
   * Creates a new {@code SceneNode} with the specified name and initial components.
   *
   * @param name the node name
   * @param components optional components to attach
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
   * Renders this node and all its children recursively.
   *
   * @param g the graphics context
   */
  public void render(Graphics g) {
    if (!active || destroyed) return;

    g.pushMatrix();
    transform.apply(g);
    renderComponents(g);

    for (SceneNode child : children) {
      child.render(g);
    }

    g.popMatrix();
  }
  
  /**
   * Renders all {@link RenderableComponent}s attached to this node.
   *
   * @param g the graphics context
   */
  protected void renderComponents(Graphics g) {
    if (!active || destroyed) return;
    for (RenderableComponent renderer : getRenderComponents()) {
      if (renderer.isActive()) {
        renderer.render(g);
      }
    }
  }

  /**
   * Updates this node and all child nodes.
   *
   * @param tpf time per frame in seconds
   */
  public void update(float tpf) {
    if (!active || destroyed) return;
    updateComponents(tpf);
    for (SceneNode child : children) {
      child.update(tpf);
    }
  }

  /**
   * Updates all {@link AudioSource} components in this subtree.
   *
   * @param audioSystem the audio system
   */
  public void updateAudio(AudioSystem audioSystem) {
    if (!active || destroyed) return;
    audioSystem.update(getComponents(AudioSource.class));
    for (SceneNode child : children) {
      child.updateAudio(audioSystem);
    }
  }

  /**
   * Updates all components attached to this node.
   *
   * @param tpf time per frame
   */
  protected void updateComponents(float tpf) {
    for (Component component : new ArrayList<>(components)) {
      component.update(tpf);
    }
  }

  /**
   * Performs final cleanup of this node and all descendants.
   *
   * <p>This method releases resources and detaches all components and children. After cleanup, the
   * node must not be used again.
   */
  public void cleanup() {
    destroyed = true;

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
   * Destroys this {@code SceneNode} and all of its descendants.
   *
   * <p>The destruction order is:
   *
   * <ol>
   *   <li>Destroy child nodes
   *   <li>Detach components
   *   <li>Remove from hierarchy
   * </ol>
   *
   * <p>This method performs logical removal only and does not trigger {@link #cleanup()}.
   */
  public void destroy() {
    if (destroyed) return;
    destroyed = true;

    for (SceneNode child : new ArrayList<>(children)) {
      child.destroy();
    }

    for (Component component : new ArrayList<>(components)) {
      try {
        component.onDetach();
        component.setOwner(null);
      } catch (Exception e) {
        System.err.println("Error destroying component: " + e.getMessage());
      }
    }
    components.clear();

    if (parent != null) {
      parent.children.remove(this);
      parent = null;
    } else if (scene != null) {
      scene.removeNode(this);
    }

    scene = null;
  }

  /** Detaches this node from its parent or scene without destroying it. */
  public void detach() {
    if (parent != null) {
      parent.children.remove(this);
      parent = null;
    } else if (scene != null) {
      scene.removeNode(this);
      scene = null;
    }
  }

  /**
   * Accepts a {@link SceneNodeVisitor} and traverses this node and its children in depth-first
   * order.
   *
   * @param visitor the visitor to apply
   * @throws IllegalArgumentException if visitor is {@code null}
   */
  public void accept(SceneNodeVisitor visitor) {
    if (visitor == null) {
      throw new IllegalArgumentException("Visitor cannot be null.");
    }

    visitor.visit(this);
    for (SceneNode child : children) {
      child.accept(visitor);
    }
  }

  /**
   * Adds a child node to this node.
   *
   * @param child the child node
   */
  public void addChild(SceneNode child) {
    if (child == null) {
      throw new IllegalArgumentException("Child node cannot be null.");
    }
    if (children.contains(child)) return;
    child.parent = this;
    children.add(child);
  }

  /**
   * Removes a child node from this node without destroying it.
   *
   * @param child the child node
   */
  public void removeChild(SceneNode child) {
    if (child == null) return;
    if (!children.contains(child)) return;
    child.parent = null;
    children.remove(child);
  }

  /**
   * Adds a component to this node.
   *
   * @param component the component to add
   */
  public void addComponent(Component component) {
    if (component == null) {
      throw new IllegalArgumentException("Component cannot be null.");
    }
    if (components.contains(component)) return;
    components.add(component);
    component.setOwner(this);
    component.onAttach();
  }

  /**
   * Removes a component from this node.
   *
   * @param component the component to remove
   */
  public void removeComponent(Component component) {
    if (component == null) return;
    if (!components.remove(component)) return;
    component.onDetach();
    component.setOwner(null);
  }

  /**
   * Returns the first component of the given type.
   *
   * @param componentClass the component type
   * @return the component or {@code null}
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
   * Returns all components of the given type.
   *
   * @param componentClass the component type
   * @return list of matching components
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

  /** @return all renderable components attached to this node */
  public List<RenderableComponent> getRenderComponents() {
    return getComponents(RenderableComponent.class);
  }

  /** @return the local transform of this node */
  public Transform getTransform() {
    return transform;
  }

  /**
   * Computes the world-space position of this node.
   *
   * @return the world position
   */
  public Vector3f getWorldPosition() {
    if (parent == null) {
      return transform.getPosition();
    }
    return parent.getWorldPosition().add(transform.getPosition());
  }

  /** @return the node name */
  public String getName() {
    return name;
  }

  /** @param name the new node name */
  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    this.name = name;
  }

  /** @return whether this node is active */
  public boolean isActive() {
    return active;
  }

  /**
   * Sets the active state of this node and all descendants.
   *
   * @param active whether the node should be active
   */
  public void setActive(boolean active) {
    if (this.active == active) return;
    this.active = active;

    for (Component component : components) {
      component.setActive(active);
    }
    for (SceneNode child : children) {
      child.setActive(active);
    }
  }

  /** @return {@code true} if this node has no parent */
  public boolean isRoot() {
    return parent == null;
  }

  /** @return {@code true} if this node has no children */
  public boolean isLeaf() {
    return children.isEmpty();
  }

  /**
   * Sets the scene reference for this node.
   *
   * @param scene the owning scene
   */
  protected void setScene(Scene scene) {
    this.scene = scene;
  }

  /**
   * Returns the scene this node belongs to.
   *
   * @return the scene or {@code null}
   */
  public Scene getScene() {
    if (scene != null) return scene;
    if (parent == null) return null;
    return parent.getScene();
  }
}
