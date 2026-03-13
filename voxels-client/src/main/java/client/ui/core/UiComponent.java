package client.ui.core;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import engine.runtime.input.KeyEvent;
import engine.runtime.input.KeyListener;

public class UiComponent extends AbstractComponent implements RenderableComponent, KeyListener {

  private boolean lastPressed;
  private UiElement uiElement;
  private Input input;

  public UiComponent(Input input, UiElement uiElement) {
    this.input = input;
    this.uiElement = uiElement;
    input.addKeyListener(this);
  }

  @Override
  public void onUpdate(float tpf) {

    float mouseX = input.getMouseX();
    float mouseY = input.getMouseY();

    uiElement.onMouse(mouseX, mouseY);

    boolean pressed = input.isMousePressed(Input.LEFT);

    if (!pressed && lastPressed) {
      uiElement.onMouseClicked(mouseX, mouseY);
    }

    if (pressed && !lastPressed) {
      uiElement.onMousePressed(mouseX, mouseY);
    }

    lastPressed = pressed;
  }

  @Override
  public void render(Graphics g) {
    uiElement.render(g);
  }

  @Override
  public void onKeyPressed(KeyEvent e) {
    uiElement.onKeyPressed(e);
  }

  @Override
  public void onKeyReleased(KeyEvent e) {}

  @Override
  public void onKeyTyped(KeyEvent e) {
    uiElement.onKeyTyped(e);
  }
}
