package engine.scene;

import java.util.ArrayList;
import java.util.List;

import engine.components.Component;
import engine.components.RenderComponent;
import engine.components.Transform;
import workspace.ui.Graphics;

/**
 * Represents a single node within the scene graph.
 * <p>
 * A {@code SceneNode} is a fundamental building block in a hierarchical scene
 * graph structure, managing child nodes, components, and transformations. Scene
 * nodes are organized in a parent-child relationship, where a parent node can
 * have multiple children, and each child can have its own components and
 * transformations.
 * </p>
 * <p>
 * The {@code SceneNode} manages its own transformation through a
 * {@link Transform} object, handles rendering, updates logic for itself and its
 * children, and provides methods for managing components like
 * {@link RenderComponent}.
 * </p>
 * <p>
 * Example use cases include:
 * <ul>
 * <li>Modeling a hierarchy of objects (e.g., parts of a character or modular
 * environment pieces).</li>
 * <li>Managing rendering logic and transformations in a scene graph.</li>
 * <li>Composing behavior with reusable components for modular design.</li>
 * </ul>
 * </p>
 * 
 * @see Transform
 * @see Component
 * @see RenderComponent
 */
public class SceneNode {

	/**
	 * The default name assigned to a scene node if no name is provided.
	 */
	private static final String DEFAULT_NAME = "Untitled-Node";

	/**
	 * The name of this node, primarily intended for debugging and identification
	 * purposes.
	 */
	private String name;

	/** The parent node in the scene graph hierarchy. */
	private SceneNode parent;

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
		this.name = name;
		this.children = new ArrayList<SceneNode>();
		this.components = new ArrayList<Component>();
		// Add a default Transform component
		this.components.add(new Transform());
	}

	/**
	 * Constructs a new, empty {@code SceneNode} with default transformations, an
	 * empty list of children, and an empty list of components.
	 */
	public SceneNode() {
		this(DEFAULT_NAME);
	}

	/**
	 * Renders this node and all its children recursively.
	 * <p>
	 * This method applies the local transformation, renders components, and
	 * traverses through all child nodes to render them as well. This ensures the
	 * entire subtree rooted at this node is rendered properly.
	 * </p>
	 * 
	 * @param g The graphics context used for rendering this node and its
	 *          children.
	 */
	public void render(Graphics g) {
		g.pushMatrix();

		applyLocalTransform(g);
		renderComponents(g);

		for (SceneNode child : children) {
			child.render(g);
		}

		g.popMatrix();
	}

	/**
	 * Applies the local transformation to the graphics context.
	 */
	private void applyLocalTransform(Graphics g) {
		getTransform().apply(g);
	}

	/**
	 * Renders all associated {@link RenderComponent} instances attached to this
	 * node.
	 * <p>
	 * This method iterates through all render components and calls their
	 * respective rendering logic.
	 * </p>
	 * 
	 * @param g The graphics context used for rendering.
	 */
	protected void renderComponents(Graphics g) {
		for (RenderComponent renderer : getRenderComponents()) {
			renderer.render(g);
		}
	}

	/**
	 * Updates this node's logic and propagates updates to children nodes.
	 * 
	 * @param tpf The time per frame in seconds (delta time).
	 */
	public void update(float tpf) {
		updateComponents(tpf);
		updateChildren(tpf);
	}

	/**
	 * Updates all components attached to this node.
	 * 
	 * @param tpf The time per frame in seconds.
	 */
	protected void updateComponents(float tpf) {
		for (Component component : components) {
			component.update(tpf);
		}
	}

	/**
	 * Updates all child nodes of this node recursively.
	 * 
	 * @param tpf The time per frame in seconds.
	 */
	protected void updateChildren(float tpf) {
		for (SceneNode child : children) {
			child.update(tpf);
		}
	}

	/**
	 * Cleans up this node's resources, components, and children recursively.
	 * <p>
	 * Each component and child is cleaned up to ensure no resources are left
	 * hanging, preventing memory leaks or unwanted behavior.
	 * </p>
	 */
	public void cleanup() {
		for (Component component : components) {
			try {
				component.cleanup();
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
	 * Adds a child node to this node's hierarchy.
	 * <p>
	 * Prevents the addition of null nodes and ensures no duplicate child is
	 * added.
	 * </p>
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
	 * <p>
	 * Cleans up the child before removing it to ensure no resources are leaked.
	 * </p>
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
	 * <p>
	 * Ensures that duplicate components of the same instance are not added.
	 * </p>
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
		}
	}

	/**
	 * Removes a component from this node.
	 * <p>
	 * If the component is found, it is cleaned up before removal.
	 * </p>
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
			component.cleanup();
			component.setOwner(null);
		}
	}

	/**
	 * Retrieves the first component of the specified type attached to this node.
	 * 
	 * @param componentClass The class type of the component to retrieve.
	 * @param <T>            The type of the component.
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
	 * <p>
	 * Enables querying for specific types of behavior or functionality attached
	 * to a node.
	 * </p>
	 * 
	 * @param componentClass The class type of the component to retrieve.
	 * @param <T>            The type of component to search for.
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
	 * @return A list of {@link RenderComponent} instances associated with this
	 *         node.
	 */
	public List<RenderComponent> getRenderComponents() {
		return getComponents(RenderComponent.class);
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

	/** Retrieves the Transform component associated with this node. */
	public Transform getTransform() {
		return getComponents(Transform.class).stream().findFirst().orElseThrow(
		    () -> new IllegalStateException("Transform component is missing."));
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

}