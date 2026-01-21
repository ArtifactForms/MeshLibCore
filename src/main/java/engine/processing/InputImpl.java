package engine.processing;

import java.util.Collection;

import engine.input.Gamepad;
import engine.input.GamepadInput;
import engine.input.GamepadListener;
import engine.input.Input;
import engine.input.Key;
import engine.input.KeyInput;
import engine.input.KeyListener;
import engine.input.MouseInput;
import engine.input.MouseListener;

public class InputImpl implements Input {

  private final KeyInput keyInput;

  private final MouseInput mouseInput;

  private final GamepadInput gamepadInput;

  public InputImpl(KeyInput keyInput, MouseInput mouseInput, GamepadInput gamepadInput) {
    this.keyInput = keyInput;
    this.mouseInput = mouseInput;
    this.gamepadInput = gamepadInput;
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

  @Override
  public void addKeyListener(KeyListener listener) {
    keyInput.addKeyListener(listener);
  }

  @Override
  public void removeKeyListener(KeyListener listener) {
    keyInput.removeKeyListener(listener);
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    mouseInput.addMouseListener(listener);
  }

  @Override
  public void removeMouseListener(MouseListener listener) {
    mouseInput.removeMouseListener(listener);
  }

  @Override
  public void updateGamepadState() {
    gamepadInput.updateGamepadState();
  }

  @Override
  public Collection<Gamepad> getGamepads() {
    return gamepadInput.getGamepads();
  }

  @Override
  public Gamepad getGamepad(int index) {
    return gamepadInput.getGamepad(index);
  }

  @Override
  public void addGamepadListener(GamepadListener listener) {
    gamepadInput.addGamepadListener(listener);
  }

  @Override
  public void removeGamepadListener(GamepadListener listener) {
    gamepadInput.removeGamepadListener(listener);
  }
}
