package workspace.ui.renderer;

import workspace.ui.Graphics;
import workspace.ui.elements.UiElement;

/**
 * Interface: Renderer
 * 
 * Defines the contract for rendering UI elements onto a provided Graphics
 * context. This abstraction allows different rendering strategies to be
 * implemented, promoting flexibility and loose coupling in the rendering logic.
 * Implementations of this interface are responsible for visually representing
 * {@link workspace.ui.elements.UiElement} instances, including their layout,
 * visual properties, and child components.
 * 
 * <p>
 * The Renderer allows for customizable rendering implementations, such as 2D,
 * 3D, or other rendering pipelines without requiring changes to the underlying
 * UI component logic.
 * </p>
 * 
 * @see workspace.ui.elements.UiElement
 * @see workspace.ui.Graphics
 */
public interface Renderer {

	/**
	 * Renders a given UI element on the specified Graphics context.
	 * 
	 * <p>
	 * Implementations of this method will handle drawing the provided
	 * {@code UiElement} onto the rendering surface represented by the given
	 * {@code Graphics} context. This includes handling UI styles, dimensions,
	 * borders, backgrounds, or any visual representation logic.
	 * </p>
	 * 
	 * @param g       The Graphics context where the rendering will occur.
	 * @param element The UiElement instance to render.
	 */
	void render(Graphics g, UiElement element);

}