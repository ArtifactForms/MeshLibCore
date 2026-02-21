package workspace.ui.renderer;

import engine.render.Graphics;
import workspace.laf.UiConstants;
import workspace.laf.UiValues;
import workspace.ui.elements.UiEditorMenu;
import workspace.ui.elements.UiElement;

/**
 * Renderer implementation for the {@code UiEditorMenu} component.
 *
 * <p>Handles drawing the background, text, and decorative lines of the editor menu, using styles
 * defined in the Look and Feel (LAF) system.
 */
public class EditorMenuRenderer implements Renderer {

  @Override
  public void render(Graphics g, UiElement element) {
    if (!(element instanceof UiEditorMenu)) return;

    UiEditorMenu menu = (UiEditorMenu) element;
    String text = menu.getText();

    // Draw the dark header bar
    g.setColor(66, 66, 66);
    g.fillRect(0, 0, g.getWidth(), 55);

    // Draw the menu background
    g.setColor(menu.getBackground());
    g.fillRect(0, 0, g.getWidth(), 30);

    // Draw the menu text
    if (text != null && !text.isEmpty()) {
      g.setColor(menu.getForeground());
      g.textSize(UiValues.getInt(UiConstants.KEY_MENU_TEXT_SIZE));
      g.text(text, 10, 20);
    }

    // Draw the bottom divider line
    g.setColor(31, 31, 31);
    g.fillRect(0, 28, g.getWidth(), 1);
  }
}
