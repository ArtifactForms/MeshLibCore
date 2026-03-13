package client.ui.core;

import java.util.List;

public class ColumnLayout implements Layout {

  private int spacing;

  public ColumnLayout(int spacing) {
    this.spacing = spacing;
  }

  @Override
  public void layout(List<UiElement> elements, int x, int y, int width, int height) {

    int currentY = y;

    for (UiElement element : elements) {

      int h = element.getHeight();

      element.setPosition(x, currentY);
      element.setWidth(width);

      currentY += h + spacing;
    }
  }
}
