package client.ui;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;

public class UiComponent extends AbstractComponent implements RenderableComponent {

  private boolean lastPressed;
  private UiElement uiElement;
  private Input input;

  public UiComponent(Input input, UiElement uiElement) {
    this.input = input;
    this.uiElement = uiElement;
  }

  @Override
  public void onUpdate(float tpf) {
    boolean pressed = input.isMousePressed(Input.LEFT);
    if (!pressed && lastPressed) {
      dispatchClicked(input.getMouseX(), input.getMouseY());
    }
    lastPressed = pressed;
  }

  @Override
  public void render(Graphics g) {
    uiElement.render(g);
  }

  private void dispatchClicked(float mouseX, float mouseY) {
    uiElement.onMouseClicked(mouseX, mouseY);
  }
}
