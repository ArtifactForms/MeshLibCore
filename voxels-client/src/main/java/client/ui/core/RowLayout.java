package client.ui.core;

import java.util.List;

public class RowLayout implements Layout {

  private int spacing;

  public RowLayout(int spacing) {
    this.spacing = spacing;
  }

  @Override
  public void layout(List<UiElement> elements, int x, int y, int width, int height) {

    int currentX = x;

    for (UiElement element : elements) {

      int w = element.getWidth();

      element.setPosition(currentX, y);
      element.setHeight(height);

      currentX += w + spacing;
    }
  }
}
