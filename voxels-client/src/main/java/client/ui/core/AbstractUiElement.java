package client.ui.core;

import engine.rendering.Graphics;
import engine.runtime.input.KeyEvent;

public abstract class AbstractUiElement implements UiElement {

  protected int x;

  protected int y;

  protected int width;

  protected int height;

  protected Layout layout;

  public AbstractUiElement() {}

  public AbstractUiElement(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void setWidth(int width) {
    this.width = width;
  }

  @Override
  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public void setLayout(Layout layout) {
    this.layout = layout;
  }

  @Override
  public void onMouseClicked(float mouseX, float mouseY) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onMousePressed(float mouseX, float mouseY) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onMouse(float mouseX, float mouseY) {
    // TODO Auto-generated method stub

  }

  @Override
  public void render(Graphics graphics) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onKeyPressed(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onKeyTyped(KeyEvent e) {
    // TODO Auto-generated method stub

  }
}
