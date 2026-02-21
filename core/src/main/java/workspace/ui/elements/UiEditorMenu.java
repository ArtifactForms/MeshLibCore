package workspace.ui.elements;

import workspace.laf.UiConstants;
import workspace.laf.UiValues;
import workspace.ui.UiComponent;
import workspace.ui.renderer.EditorMenuRenderer;

/**
 * Represents an editor menu component in the UI.
 *
 * <p>The menu displays a background and customizable text, styled using values from the Look and
 * Feel (LAF) system. Rendering is delegated to a dedicated renderer for modularity.
 */
public class UiEditorMenu extends UiComponent {

  private String text; // The menu's display text.

  /** Constructs a new {@code UiEditorMenu} with default styles. */
  public UiEditorMenu() {
//    setText("");
//    setForeground(UiValues.getColor(UiConstants.KEY_MENU_FOREGROUND_COLOR));
//    setBackground(UiValues.getColor(UiConstants.KEY_MENU_BACKGROUND_COLOR));
//    setRenderer(new EditorMenuRenderer());
  }

  /**
   * Gets the text displayed in this menu.
   *
   * @return The current text of the menu.
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the text displayed in this menu.
   *
   * @param text The new text to display.
   */
  public void setText(String text) {
    this.text = text;
  }
}
