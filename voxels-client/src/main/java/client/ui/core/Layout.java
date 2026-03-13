package client.ui.core;

import java.util.List;

public interface Layout {

  void layout(List<UiElement> elements, int x, int y, int width, int height);
}
