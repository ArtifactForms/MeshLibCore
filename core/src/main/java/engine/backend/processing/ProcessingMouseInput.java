package engine.backend.processing;

import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;

import engine.runtime.input.MouseEvent;
import engine.runtime.input.MouseInput;
import engine.runtime.input.MouseListener;
import engine.runtime.input.MouseMode;
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

  private final List<MouseListener> listeners;

  // Button state tracking
  private final boolean[] currentButtons = new boolean[3];
  private final boolean[] lastButtons = new boolean[3];
  private final boolean[] pressedButtons = new boolean[3];
  private final boolean[] releasedButtons = new boolean[3];

  public ProcessingMouseInput(PApplet applet) {

    this.applet = applet;

    applet.registerMethod("mouseEvent", this);

    try {
      robot = new Robot();
    } catch (Throwable e) {
      robot = null;
    }

    listeners = new ArrayList<>();
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

  private boolean readButtonState(int button) {

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

  @Override
  public boolean isMouseDown(int button) {
    return currentButtons[button];
  }

  @Override
  public boolean isMousePressed(int button) {
    return pressedButtons[button];
  }

  @Override
  public boolean isMouseReleased(int button) {
    return releasedButtons[button];
  }

  protected void fireMouseClicked(MouseEvent e) {
    for (MouseListener listener : listeners) listener.onMouseClicked(e);
  }

  protected void fireMousePressed(MouseEvent e) {
    for (MouseListener listener : listeners) listener.onMousePressed(e);
  }

  protected void fireMouseMoved(MouseEvent e) {
    for (MouseListener listener : listeners) listener.onMouseMoved(e);
  }

  protected void fireMouseDragged(MouseEvent e) {
    for (MouseListener listener : listeners) listener.onMouseDragged(e);
  }

  protected void fireMouseReleased(MouseEvent e) {
    for (MouseListener listener : listeners) listener.onMouseReleased(e);
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    if (listener != null) listeners.add(listener);
  }

  @Override
  public void removeMouseListener(MouseListener listener) {
    if (listener != null) listeners.remove(listener);
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

    mouseWheelDelta = 0;

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

    for (int i = 0; i < currentButtons.length; i++) {

      boolean now = readButtonState(i);

      pressedButtons[i] = !lastButtons[i] && now;
      releasedButtons[i] = lastButtons[i] && !now;

      currentButtons[i] = now;
      lastButtons[i] = now;
    }

    if (mouseMode == MouseMode.LOCKED) center();
  }

  private void center() {

    if (!applet.focused) return;

    int cx = applet.width / 2;
    int cy = applet.height / 2;

    applet.mouseX = cx;
    applet.mouseY = cy;
    applet.pmouseX = cx;
    applet.pmouseY = cy;

    if (robot != null) robot.mouseMove(cx, cy);
  }
}
