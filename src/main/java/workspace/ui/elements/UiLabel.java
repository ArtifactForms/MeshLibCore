package workspace.ui.elements;

import workspace.ui.UiComponent;
import workspace.ui.renderer.LabelRenderer;

/**
 * A simple UI label component for displaying text.
 * <p>
 * The label supports customizable text, background, and foreground colors.
 * Rendering logic is delegated to a dedicated renderer for consistency with the
 * framework's modular design.
 * </p>
 */
public class UiLabel extends UiComponent {

	/**
	 * The text displayed by the label.
	 */
	private String title;

	/**
	 * Constructs a new {@code UiLabel} with the given title.
	 *
	 * @param title The text to display on the label.
	 */
	public UiLabel(String title) {
		this.title = title;
		setRenderer(new LabelRenderer());
	}

	/**
	 * Gets the title (text) displayed by this label.
	 *
	 * @return The current title of the label.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title (text) of this label.
	 *
	 * @param title The new text to display on the label.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}