package engine.processing;

import engine.input.MouseInput;
import processing.core.PApplet;

public class ProcessingMouseInput implements MouseInput {

  private final PApplet applet;

  private float mouseWheelDelta = 0;

  public ProcessingMouseInput(PApplet applet) {
    this.applet = applet;
    applet.registerMethod("mouseEvent", this);
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
  public void updateMouseState() {
    // Handle frame-specific mouse state updates if necessary
  }
}
