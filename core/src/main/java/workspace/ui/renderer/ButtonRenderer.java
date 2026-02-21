package workspace.ui.renderer;

import engine.render.Graphics;
import workspace.ui.elements.UiButton;
import workspace.ui.elements.UiElement;

/**
 * Renderer implementation for the {@code UiButton} component.
 *
 * <p>Handles the visual representation of the button, including its background, text, and
 * dimensions.
 */
public class ButtonRenderer implements Renderer {

  @Override
  public void render(Graphics g, UiElement component) {
    if (!(component instanceof UiButton)) return;

    UiButton button = (UiButton) component;
    String text = button.getText();

    // Measure text width and height
    int textWidth = (int) g.textWidth(text);
    int textHeight = (int) (g.textAscent() + g.textDescent());

    // Adjust button dimensions to fit text if necessary
    button.setWidth(Math.max(button.getWidth(), textWidth));
    button.setHeight(Math.max(button.getHeight(), textHeight));

    // Draw button background
    g.setColor(button.getBackground());
    g.fillRect(0, 0, button.getWidth(), button.getHeight());

    // Draw button text
    if (text != null && !text.isEmpty()) {
      g.setColor(button.getForeground());
      g.text(
          text,
          (button.getWidth() - textWidth) / 2,
          (button.getHeight() + textHeight) / 2 - g.textDescent());
    }
  }
}
