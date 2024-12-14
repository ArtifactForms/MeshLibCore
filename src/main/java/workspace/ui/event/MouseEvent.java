package workspace.ui.event;

/**
 * Represents a mouse event, containing information about the current and
 * previous mouse positions.
 * <p>
 * This class is used to encapsulate the state of the mouse during an event,
 * such as mouse movement or mouse clicks. It provides details about the mouse's
 * current coordinates as well as its previous position.
 * </p>
 * 
 * <pre>
 * Example usage:
 * 
 * MouseEvent event = new MouseEvent(100, 200, 90, 180);
 * int currentX = event.getMouseX();
 * int previousX = event.getPreviousMouseX();
 * </pre>
 */
public class MouseEvent {

	/** The current X-coordinate of the mouse. */
	private final int mouseX;

	/** The current Y-coordinate of the mouse. */
	private final int mouseY;

	/** The previous X-coordinate of the mouse. */
	private final int previousMouseX;

	/** The previous Y-coordinate of the mouse. */
	private final int previousMouseY;

	/**
	 * Constructs a new {@code MouseEvent} with the specified current and previous
	 * mouse coordinates.
	 * 
	 * @param mouseX         The current X-coordinate of the mouse.
	 * @param mouseY         The current Y-coordinate of the mouse.
	 * @param previousMouseX The previous X-coordinate of the mouse.
	 * @param previousMouseY The previous Y-coordinate of the mouse.
	 */
	public MouseEvent(int mouseX, int mouseY, int previousMouseX,
	    int previousMouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.previousMouseX = previousMouseX;
		this.previousMouseY = previousMouseY;
	}

	/**
	 * Gets the current X-coordinate of the mouse.
	 * 
	 * @return The current X-coordinate.
	 */
	public int getMouseX() {
		return mouseX;
	}

	/**
	 * Gets the current Y-coordinate of the mouse.
	 * 
	 * @return The current Y-coordinate.
	 */
	public int getMouseY() {
		return mouseY;
	}

	/**
	 * Gets the previous X-coordinate of the mouse.
	 * 
	 * @return The previous X-coordinate.
	 */
	public int getPreviousMouseX() {
		return previousMouseX;
	}

	/**
	 * Gets the previous Y-coordinate of the mouse.
	 * 
	 * @return The previous Y-coordinate.
	 */
	public int getPreviousMouseY() {
		return previousMouseY;
	}

}