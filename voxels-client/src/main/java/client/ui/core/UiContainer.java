package client.ui.core;

import java.util.ArrayList;
import java.util.List;

import engine.rendering.Graphics;
import engine.runtime.input.KeyEvent;

public class UiContainer extends AbstractUiElement {

  private List<UiElement> children = new ArrayList<>();
  private Layout layout;

  public UiContainer(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  public void add(UiElement element) {
    children.add(element);
  }

  @Override
  public void onMouse(float x, float y) {
    for (UiElement child : children) {
      child.onMouse(x, y);
    }
  }

  @Override
  public void onMouseClicked(float x, float y) {
    for (UiElement child : children) {
      child.onMouseClicked(x, y);
    }
  }

  @Override
  public void onMousePressed(float x, float y) {
    for (UiElement child : children) {
      child.onMousePressed(x, y);
    }
  }

  @Override
  public void onKeyPressed(KeyEvent e) {
    for (UiElement child : children) {
      child.onKeyPressed(e);
    }
  }

  @Override
  public void onKeyTyped(KeyEvent e) {
    for (UiElement child : children) {
      child.onKeyTyped(e);
    }
  }

  @Override
  public void render(Graphics g) {
    if (layout != null) layout.layout(children, x, y, width, height);

    for (UiElement child : children) {
      child.render(g);
    }
  }
}
