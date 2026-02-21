package workspace.ui.elements;

import workspace.ui.Graphics;
import workspace.ui.UiComponent;
import workspace.ui.event.IActionListener;
import workspace.ui.renderer.CheckBoxRenderer;

/**
 * A UI CheckBox component that can be toggled (selected/deselected) and supports click handling.
 *
 * <p>This class represents a checkbox UI element with a label, click interaction, and customizable
 * rendering.
 */
public class UiCheckBox extends UiComponent {

  /** Checkbox state. */
  private boolean selected;

  /** Label text for the checkbox. */
  private String text;

  /** Listener for click events. */
  private IActionListener actionListener;

  private final CheckBoxRenderer renderer;

  /**
   * Constructs a new {@code UiCheckBox} with the provided label text.
   *
   * <p>Initializes default dimensions and sets up the checkbox renderer.
   *
   * @param text The label text to display next to the checkbox.
   */
  public UiCheckBox(String text) {
    this.text = text;
    this.width = 15;
    this.height = 15;
    this.renderer = new CheckBoxRenderer();
  }

  /**
   * Handles rendering for this checkbox component.
   *
   * @param g The {@link Graphics} context to draw on.
   */
  @Override
  public void renderSelf(Graphics g) {
    renderer.render(g, this);
  }

  /**
   * Handles mouse click interactions. Toggles the selection state when clicked.
   *
   * @param x X-coordinate of the mouse click.
   * @param y Y-coordinate of the mouse click.
   */
  @Override
  public void onMouseClicked(int x, int y) {
    super.onMouseClicked(x, y);
    toggleSelection();
  }

  /** Toggles the selection state of this checkbox and invokes the action listener, if any. */
  private void toggleSelection() {
    setSelected(!selected);
  }

  /**
   * Checks if the checkbox is currently selected.
   *
   * @return {@code true} if the checkbox is selected; {@code false} otherwise.
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * Updates the selection state of the checkbox and notifies listeners about the change.
   *
   * @param selected The new selection state to set.
   */
  public void setSelected(boolean selected) {
    if (this.selected == selected) {
      return; // No state change
    }
    this.selected = selected;
    if (actionListener != null) {
      actionListener.onActionPerformed();
    }
  }

  /**
   * Gets the text label associated with the checkbox.
   *
   * @return The current text label.
   */
  public String getText() {
    return text;
  }

  /**
   * Updates the text label of the checkbox.
   *
   * @param text The new text label.
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Sets an action listener for handling click events on this checkbox.
   *
   * @param listener The action listener to set.
   */
  public void setActionListener(IActionListener listener) {
    this.actionListener = listener;
  }

  /**
   * Retrieves the currently set action listener.
   *
   * @return The action listener currently assigned to this checkbox.
   */
  public IActionListener getActionListener() {
    return actionListener;
  }
}
