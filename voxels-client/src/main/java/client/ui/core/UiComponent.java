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
    if (input != null) input.addKeyListener(this);
  }

  @Override
  public void onUpdate(float tpf) {
    if (input == null) return;

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

  public void onMouseClicked(float mouseX, float mouseY) {
    uiElement.onMouseClicked(mouseX, mouseY);
  }

  public void onMouseMoved(float mouseX, float mouseY) {
    uiElement.onMouse(mouseX, mouseY);
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
  public void onKeyReleased(KeyEvent e) {
    uiElement.onKeyReleased(e);
  }

  @Override
  public void onKeyTyped(KeyEvent e) {
    uiElement.onKeyTyped(e);
  }
}
