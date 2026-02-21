package workspace.ui.renderer;

import engine.render.Graphics;
import workspace.ui.border.Insets;
import workspace.ui.elements.UiCheckBox;
import workspace.ui.elements.UiElement;

/**
 * Renderer implementation for rendering the {@code UiCheckBox}.
 *
 * <p>Responsible for drawing the checkbox, its state (checked/unchecked), and its label text.
 */
public class CheckBoxRenderer implements Renderer {

  @Override
  public void render(Graphics g, UiElement element) {
    if (!(element instanceof UiCheckBox)) return;

    UiCheckBox checkBox = (UiCheckBox) element;
    Insets insets = checkBox.getInsets();
    int offsetX = insets.getLeft();
    int offsetY = insets.getTop();
    int boxWidth = checkBox.getWidth() - insets.getHorizontalInsets();
    int boxHeight = checkBox.getHeight() - insets.getVerticalInsets();

    // Render checkbox box
    g.setColor(checkBox.getBackground());
    g.fillRect(offsetX, offsetY, boxWidth, boxHeight);

    // If checked, fill the inner box with checkmark
    if (checkBox.isSelected()) {
      g.setColor(checkBox.getForeground());
      g.fillRect(offsetX + 4, offsetY + 4, boxWidth - 8, boxHeight - 8);
    }

    // Render text
    g.setColor(checkBox.getForeground());
    g.text(checkBox.getText(), offsetX + boxWidth + 4, offsetY + (boxHeight / 2));
  }
}
