package workspace.ui.renderer;

import engine.rendering.Graphics;
import workspace.ui.border.Insets;
import workspace.ui.elements.UiElement;

/**
 * Default implementation of the {@link Renderer} interface for rendering a standard panel.
 *
 * <p>This class provides a simple rendering strategy by drawing a solid background rectangle for a
 * given {@code UiElement} based on its background color and insets. It ensures that rendering
 * respects the defined padding or margins (insets) and only draws if a background color is set.
 */
public class PanelRenderer implements Renderer {

  /**
   * Renders the panel's background onto the given graphics context.
   *
   * <p>The rendering accounts for the insets (margins or padding) defined by the element's layout
   * and only fills the visible area with the panel's background color if it is set.
   *
   * @param g The {@link Graphics} object used for rendering.
   * @param element The {@link UiElement} representing the UI component to render.
   */
  @Override
  public void render(Graphics g, UiElement element) {
    Insets insets = element.getInsets();
    int x = insets.getLeft();
    int y = insets.getTop();
    int width = element.getWidth() - insets.getHorizontalInsets();
    int height = element.getHeight() - insets.getVerticalInsets();

    if (element.getBackground() != null) {
      g.setColor(element.getBackground());
      g.fillRect(x, y, width, height);
    }
  }
}
