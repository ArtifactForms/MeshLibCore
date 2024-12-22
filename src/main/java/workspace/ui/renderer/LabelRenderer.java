package workspace.ui.renderer;

import workspace.ui.Graphics;
import workspace.ui.border.Insets;
import workspace.ui.elements.UiElement;
import workspace.ui.elements.UiLabel;

/**
 * Renderer implementation for rendering {@code UiLabel} components.
 *
 * <p>Handles the drawing of the label's background, text, and ensures proper alignment.
 */
public class LabelRenderer implements Renderer {

  @Override
  public void render(Graphics g, UiElement element) {
    if (!(element instanceof UiLabel)) return;

    UiLabel label = (UiLabel) element;
    String title = label.getTitle();
    if (title == null || title.isEmpty()) return;

    Insets insets = element.getInsets();
    int x = insets.getLeft();
    int y = insets.getTop();

    // Calculate text dimensions
    float textWidth = g.textWidth(title);
    float textHeight = g.textAscent() + g.textDescent();

    // Render background
    g.setColor(label.getBackground());
    g.fillRect(x, y, textWidth, textHeight);

    // Render text
    g.setColor(label.getForeground());
    g.text(title, x, y + g.textAscent());
  }
}
