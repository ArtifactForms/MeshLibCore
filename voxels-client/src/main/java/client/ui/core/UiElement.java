package client.ui.core;

import engine.rendering.Graphics;

public interface UiElement {

  void onMouseClicked(float x, float y);
  
  void onMouse(float x, float y);

  void render(Graphics graphics);
}
