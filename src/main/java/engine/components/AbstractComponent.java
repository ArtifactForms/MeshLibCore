package engine.components;

import engine.scene.SceneNode;

/**
 * Abstract base class for all components in the scene graph.
 * <p>
 * This class provides a shared implementation of common functionality across
 * all components, reducing boilerplate and centralizing shared logic for ease
 * of maintenance.
 * </p>
 */
public abstract class AbstractComponent implements Component {

	/** Reference to the owning SceneNode */
	protected SceneNode owner;

	/**
	 * Sets the owner (parent node) of this component.
	 * <p>
	 * This is common logic provided by the abstract class to ensure consistency
	 * among all components.
	 * </p>
	 *
	 * @param owner The SceneNode that owns this component.
	 */
	@Override
	public void setOwner(SceneNode owner) {
		this.owner = owner;
	}

	/**
	 * Retrieves the owning node for convenience.
	 * 
	 * @return The owning SceneNode instance.
	 */
	public SceneNode getOwner() {
		return owner;
	}

}