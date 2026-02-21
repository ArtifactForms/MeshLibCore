package workspace.ui.renderer;

import engine.render.Graphics;
import workspace.ui.border.Insets;
import workspace.ui.elements.UiElement;
import workspace.ui.elements.UiSlider;

/** Renderer for the slider component. */
public class SliderRenderer implements Renderer {

  @Override
  public void render(Graphics g, UiElement element) {
    UiSlider slider = (UiSlider) element;
    Insets insets = slider.getInsets();

    int trackStartX = insets.getLeft();
    int trackStartY = insets.getTop();
    int trackWidth = slider.getWidth() - insets.getHorizontalInsets();
    int trackHeight = slider.getHeight() - insets.getVerticalInsets();

    // Draw track
    g.setColor(slider.getBackground());
    g.fillRect(trackStartX, trackStartY, trackWidth, trackHeight);

    // Draw filled portion
    //			g.setColor(slider.getForeground());
    g.setColor(60, 60, 60);
    g.fillRect(trackStartX, trackStartY, (int) slider.getHandlePosition(), trackHeight);

    // Draw handle
    g.setColor(slider.getForeground());
    int handleWidth = 5;
    int handleX = (int) (trackStartX + slider.getHandlePosition() - handleWidth / 2);
    g.fillRect(handleX, trackStartY, handleWidth, trackHeight);

    // Draw label and value
    g.setColor(slider.getForeground());
    g.text(
        slider.getText() + ":" + slider.getValue(),
        insets.getLeft() + slider.getWidth() + 10,
        g.getTextSize() + g.textDescent());
  }
}
