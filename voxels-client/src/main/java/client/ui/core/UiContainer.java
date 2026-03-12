package client.ui.core;

import java.util.ArrayList;
import java.util.List;

import engine.rendering.Graphics;

public class UiContainer implements UiElement {

  private List<UiElement> children = new ArrayList<>();

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
  public void render(Graphics g) {
    for (UiElement child : children) {
      child.render(g);
    }
  }
}
