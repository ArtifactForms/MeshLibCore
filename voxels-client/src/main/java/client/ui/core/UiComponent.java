package client.ui.core;

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

    float mouseX = input.getMouseX();
    float mouseY = input.getMouseY();

    uiElement.onMouse(mouseX, mouseY);

    boolean pressed = input.isMousePressed(Input.LEFT);

    if (!pressed && lastPressed) {
      uiElement.onMouseClicked(mouseX, mouseY);
    }

    lastPressed = pressed;
  }

  @Override
  public void render(Graphics g) {
    uiElement.render(g);
  }
}
