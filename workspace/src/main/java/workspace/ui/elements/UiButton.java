package workspace.ui.elements;

import workspace.ui.UiComponent;
import workspace.ui.event.IActionListener;
import workspace.ui.renderer.ButtonRenderer;

/**
 * Represents a button component in the UI.
 *
 * <p>A button can display text and execute an associated action when clicked. Rendering is handled
 * by a dedicated renderer, allowing for customizable visual styles.
 */
public class UiButton extends UiComponent {

  /** The text displayed on the button. */
  private String text;

  /** The action listener for click events. */
  private IActionListener actionListener;

  /**
   * Constructs a new {@code UiButton} with the specified text.
   *
   * @param text The text to display on the button.
   */
  public UiButton(String text) {
    this(text, 0, 0, 0, 0);
  }

  /**
   * Constructs a new {@code UiButton} with the specified text and dimensions.
   *
   * @param text The text to display on the button.
   * @param x The x-coordinate of the button.
   * @param y The y-coordinate of the button.
   * @param width The width of the button.
   * @param height The height of the button.
   */
  public UiButton(String text, int x, int y, int width, int height) {
    this.text = text;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    setRenderer(new ButtonRenderer());
  }

  /**
   * Handles mouse click events for the button.
   *
   * <p>If the button has an associated action listener, its {@link
   * IActionListener#onActionPerformed()} method is invoked.
   *
   * @param x The x-coordinate of the mouse click.
   * @param y The y-coordinate of the mouse click.
   */
  @Override
  public void onMouseClicked(int x, int y) {
    super.onMouseClicked(x, y);
    if (actionListener != null) {
      actionListener.onActionPerformed();
    }
  }

  /**
   * Gets the text displayed on the button.
   *
   * @return The current text of the button.
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the text to display on the button.
   *
   * @param text The new text to display.
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Gets the action listener associated with the button.
   *
   * @return The current action listener, or {@code null} if none is set.
   */
  public IActionListener getActionListener() {
    return actionListener;
  }

  /**
   * Sets the action listener for the button.
   *
   * @param actionListener The action listener to associate with the button.
   */
  public void setActionListener(IActionListener actionListener) {
    this.actionListener = actionListener;
  }
}
