package client.ui.core;

import engine.rendering.Graphics;
import engine.runtime.input.KeyEvent;

public interface UiElement extends Layoutable {

  void onMouseClicked(float mouseX, float mouseY);

  void onMousePressed(float mouseX, float mouseY);

  void onMouse(float mouseX, float mouseY);

  void render(Graphics graphics);

  void onKeyPressed(KeyEvent e);

  void onKeyTyped(KeyEvent e);

  void setLayout(Layout layout);
}
