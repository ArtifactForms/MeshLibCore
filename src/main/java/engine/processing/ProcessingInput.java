package engine.processing;

import java.util.Collection;

import engine.input.Input;
import engine.input.Key;
import engine.input.KeyInput;
import engine.input.MouseInput;

public class ProcessingInput implements Input {

  private final KeyInput keyInput;

  private final MouseInput mouseInput;

  public ProcessingInput(KeyInput keyInput, MouseInput mouseInput) {
    this.keyInput = keyInput;
    this.mouseInput = mouseInput;
  }

  @Override
  public Collection<Key> getPressedKeys() {
    return keyInput.getPressedKeys();
  }

  @Override
  public boolean isKeyPressed(Key key) {
    return keyInput.isKeyPressed(key);
  }

  @Override
  public void updateKeyState() {
    keyInput.updateKeyState();
  }

  @Override
  public boolean isMousePressed(int button) {
    return mouseInput.isMousePressed(button);
  }

  @Override
  public boolean isMouseReleased(int button) {
    return mouseInput.isMouseReleased(button);
  }

  @Override
  public float getScreenWidth() {
    return mouseInput.getScreenWidth();
  }

  @Override
  public float getScreenHeight() {
    return mouseInput.getScreenHeight();
  }

  @Override
  public float getMouseX() {
    return mouseInput.getMouseX();
  }

  @Override
  public float getMouseY() {
    return mouseInput.getMouseY();
  }

  @Override
  public float getLastMouseX() {
    return mouseInput.getLastMouseX();
  }

  @Override
  public float getLastMouseY() {
    return mouseInput.getLastMouseY();
  }

  @Override
  public float getMouseDeltaX() {
    return mouseInput.getMouseDeltaX();
  }

  @Override
  public float getMouseDeltaY() {
    return mouseInput.getMouseDeltaY();
  }

  @Override
  public float getMouseWheelDelta() {
    return mouseInput.getMouseWheelDelta();
  }

  @Override
  public void updateMouseState() {
    mouseInput.updateMouseState();
  }

  @Override
  public void center() {
    mouseInput.center();
  }

  @Override
  public void update() {
    updateKeyState();
    updateMouseState();
  }
}
