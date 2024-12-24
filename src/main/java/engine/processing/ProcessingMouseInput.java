package engine.processing;

import java.awt.Robot;

import engine.input.MouseInput;
import processing.core.PApplet;

public class ProcessingMouseInput implements MouseInput {

  private final PApplet applet;

  private float mouseWheelDelta = 0;

  private Robot robot;

  public ProcessingMouseInput(PApplet applet) {
    this.applet = applet;
    applet.registerMethod("mouseEvent", this);
    try {
      robot = new Robot();
    } catch (Throwable e) {
    }
  }

  public void mouseEvent(processing.event.MouseEvent event) {
    if (event.getAction() == processing.event.MouseEvent.WHEEL) {
      mouseWheelDelta = event.getCount();
    }
  }

  @Override
  public boolean isMousePressed(int button) {
    return applet.mousePressed && applet.mouseButton == button;
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
    return applet.mouseX;
  }

  @Override
  public float getMouseY() {
    return applet.mouseY;
  }

  @Override
  public float getLastMouseX() {
    return applet.pmouseX;
  }

  @Override
  public float getLastMouseY() {
    return applet.pmouseY;
  }

  @Override
  public float getMouseDeltaX() {
    return applet.mouseX - applet.pmouseX;
  }

  @Override
  public float getMouseDeltaY() {
    return applet.mouseY - applet.pmouseY;
  }

  @Override
  public float getMouseWheelDelta() {
    float delta = mouseWheelDelta;
    mouseWheelDelta = 0; // Reset after read
    return delta;
  }

  @Override
  public void updateMouseState() {}

  @Override
  public void center() {
    applet.mouseX = applet.width / 2;
    applet.mouseY = applet.height / 2;
    applet.pmouseX = applet.width / 2;
    applet.pmouseY = applet.height / 2;
    robot.mouseMove((int) getScreenWidth() / 2, (int) getScreenHeight() / 2);
  }
}
