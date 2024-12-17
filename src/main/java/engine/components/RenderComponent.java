package engine.components;

import engine.scene.SceneNode;
import workspace.ui.Graphics;

/**
 * Represents a renderable component within the scene graph architecture.
 * <p>
 * RenderComponents are modular pieces of rendering behavior that can be added
 * to {@link SceneNode} instances. They follow the component-based design
 * pattern, encapsulating rendering logic and enabling flexible and reusable
 * visual behavior within a scene.
 * </p>
 * <p>
 * A RenderComponent should implement the rendering logic in its
 * {@code render()} method, which is invoked during the rendering pass of a
 * scene graph. It is designed to work with a graphics context (such as that
 * represented by the {@link Graphics} class) to issue drawing commands.
 * </p>
 * <p>
 * Example use cases include rendering meshes, particles, UI overlays, or other
 * visual elements as part of a node's rendering pipeline.
 * </p>
 * 
 * @see Component
 * @see SceneNode
 */
public interface RenderComponent extends Component {

	/**
	 * Renders this component using the provided graphics context.
	 * <p>
	 * This method is called during the rendering phase of the scene graph
	 * traversal. Implementations of this method should issue drawing commands
	 * using the provided {@link Graphics} instance. Components should encapsulate
	 * the logic necessary to visualize themselves or their state.
	 * </p>
	 * 
	 * @param g The graphics context to use for rendering. This context provides
	 *          methods for transformations, shading, drawing primitives, and
	 *          other rendering operations.
	 */
	void render(Graphics g);

}