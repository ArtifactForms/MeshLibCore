package engine.backend.processing;

import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;

import engine.input.MouseEvent;
import engine.input.MouseInput;
import engine.input.MouseListener;
import engine.input.MouseMode;
import processing.core.PApplet;

public class ProcessingMouseInput implements MouseInput {

  private final PApplet applet;

  private float mouseWheelDelta = 0;

  private float mouseX;
  private float mouseY;
  private float pMouseX;
  private float pMouseY;

  private MouseMode mouseMode = MouseMode.ABSOLUTE;

  private Robot robot;

  private List<MouseListener> listeners;

  public ProcessingMouseInput(PApplet applet) {
    this.applet = applet;
    applet.registerMethod("mouseEvent", this);
    try {
      robot = new Robot();
    } catch (Throwable e) {
    }
    listeners = new ArrayList<MouseListener>();
  }

  public void mouseEvent(processing.event.MouseEvent event) {
    MouseEvent e = new MouseEvent(event.getX(), event.getY(), mapButton(event.getButton()));

    int action = event.getAction();

    switch (action) {
      case processing.event.MouseEvent.CLICK:
        fireMouseClicked(e);
        break;
      case processing.event.MouseEvent.PRESS:
        fireMousePressed(e);
        break;
      case processing.event.MouseEvent.MOVE:
        fireMouseMoved(e);
        break;
      case processing.event.MouseEvent.DRAG:
        fireMouseDragged(e);
        break;
      case processing.event.MouseEvent.RELEASE:
        fireMouseReleased(e);
        break;
      case processing.event.MouseEvent.WHEEL:
        mouseWheelDelta = event.getCount();
        break;
    }
  }

  private int mapButton(int button) {
    switch (button) {
      case PApplet.LEFT:
        return LEFT;
      case PApplet.RIGHT:
        return RIGHT;
      case PApplet.CENTER:
        return CENTER;
      default:
        return -1;
    }
  }

  @Override
  public boolean isMousePressed(int button) {
    int pButton;
    switch (button) {
      case LEFT:
        pButton = PApplet.LEFT;
        break;
      case RIGHT:
        pButton = PApplet.RIGHT;
        break;
      case CENTER:
        pButton = PApplet.CENTER;
        break;
      default:
        return false;
    }
    return applet.mousePressed && applet.mouseButton == pButton;
  }

  protected void fireMouseClicked(MouseEvent e) {
    for (MouseListener listener : listeners) {
      listener.onMouseClicked(e);
    }
  }

  protected void fireMousePressed(MouseEvent e) {
    for (MouseListener listener : listeners) {
      listener.onMousePressed(e);
    }
  }

  protected void fireMouseMoved(MouseEvent e) {
    for (MouseListener listener : listeners) {
      listener.onMouseMoved(e);
    }
  }

  protected void fireMouseDragged(MouseEvent e) {
    for (MouseListener listener : listeners) {
      listener.onMouseDragged(e);
    }
  }

  protected void fireMouseReleased(MouseEvent e) {
    for (MouseListener listener : listeners) {
      listener.onMouseReleased(e);
    }
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    if (listener != null) listeners.add(listener);
  }

  @Override
  public void removeMouseListener(MouseListener listener) {
    if (listener != null) listeners.add(listener);
  }

  @Override
  public boolean isMouseReleased(int button) {
    // Custom state tracking needed
    return false;
  }

  @Override
  public float getScreenWidth() {
    return applet.width;
  }

  @Override
  public float getScreenHeight() {
    return applet.height;
  }

  @Override
  public float getMouseX() {
    return mouseX;
  }

  @Override
  public float getMouseY() {
    return mouseY;
  }

  @Override
  public float getLastMouseX() {
    return pMouseX;
  }

  @Override
  public float getLastMouseY() {
    return pMouseY;
  }

  @Override
  public float getMouseDeltaX() {
    return mouseX - pMouseX;
  }

  @Override
  public float getMouseDeltaY() {
    return mouseY - pMouseY;
  }

  @Override
  public float getMouseWheelDelta() {
    float delta = mouseWheelDelta;
    mouseWheelDelta = 0; // Reset after read
    return delta;
  }

  @Override
  public void setMouseMode(MouseMode mode) {
    this.mouseMode = mode;
  }

  @Override
  public MouseMode getMouseMode() {
    return mouseMode;
  }

  @Override
  public void updateMouseState() {
    this.mouseX = applet.mouseX;
    this.mouseY = applet.mouseY;
    this.pMouseX = applet.pmouseX;
    this.pMouseY = applet.pmouseY;

    if (mouseMode == MouseMode.LOCKED) center();
  }

  private void center() {
    if (!applet.focused) return;

    applet.mouseX = applet.width / 2;
    applet.mouseY = applet.height / 2;
    applet.pmouseX = applet.width / 2;
    applet.pmouseY = applet.height / 2;

    robot.mouseMove((int) getScreenWidth() / 2, (int) getScreenHeight() / 2);
  }
}
