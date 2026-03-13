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

      if (element instanceof Layoutable layoutable) {

        int h = layoutable.getHeight();

        layoutable.setPosition(x, currentY);
        layoutable.setWidth(width);

        currentY += h + spacing;
      }
    }
  }
}