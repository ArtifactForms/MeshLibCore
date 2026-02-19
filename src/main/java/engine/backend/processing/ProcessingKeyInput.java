package engine.backend.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import engine.runtime.input.InputState;
import engine.runtime.input.Key;
import engine.runtime.input.KeyCharacterMapper;
import engine.runtime.input.KeyInput;
import engine.runtime.input.KeyListener;
import processing.core.PApplet;
import processing.event.KeyEvent;

/**
 * Processing-specific implementation of {@link KeyInput}.
 *
 * <p>This class bridges Processing's {@link processing.event.KeyEvent} system with the engine's
 * internal, framework-agnostic input model.
 *
 * <p>Key states are tracked using an {@link InputState}, which provides deterministic, frame-based
 * input handling (down / pressed / released).
 *
 * <p>The implementation supports:
 *
 * <ul>
 *   <li>Coded keys (SHIFT, CTRL, ALT, arrow keys)
 *   <li>Alphanumeric keys (A–Z, 0–9) via keyCode mapping
 *   <li>Special character keys (SPACE, ENTER, TAB, etc.) via character mapping
 * </ul>
 *
 * <p>{@link #updateKeyState()} must be called exactly once per frame to advance the internal input
 * state.
 */
public class ProcessingKeyInput implements KeyInput {

  /**
   * Mapping of Processing "coded" key codes to engine {@link Key} values.
   *
   * <p>Coded keys are reported by Processing using {@link PApplet#CODED} and must be resolved via
   * {@link KeyEvent#getKeyCode()}.
   */
  private static final HashMap<Integer, Key> codedKeysMap = new HashMap<>();

  static {
    codedKeysMap.put(PApplet.SHIFT, Key.SHIFT);
    codedKeysMap.put(PApplet.ALT, Key.ALT);
    codedKeysMap.put(PApplet.CONTROL, Key.CTRL);
    codedKeysMap.put(PApplet.UP, Key.ARROW_UP);
    codedKeysMap.put(PApplet.DOWN, Key.ARROW_DOWN);
    codedKeysMap.put(PApplet.LEFT, Key.ARROW_LEFT);
    codedKeysMap.put(PApplet.RIGHT, Key.ARROW_RIGHT);
  }

  /** Reference to the Processing applet used for focus checks. */
  private PApplet applet;

  /** Centralized frame-based input state. */
  private final InputState inputState = new InputState();

  /** Registered key listeners. */
  private final List<KeyListener> listeners = new ArrayList<>();

  /**
   * Creates a new Processing key input handler and registers it with the given {@link PApplet}.
   *
   * @param applet the Processing applet to receive key events from
   */
  public ProcessingKeyInput(PApplet applet) {
    this.applet = applet;
    applet.registerMethod("keyEvent", this);
  }

  @Override
  public boolean isKeyPressed(Key key) {
    return inputState.isDown(key);
  }

  @Override
  public Collection<Key> getPressedKeys() {
    return new ArrayList<>(inputState.getDownKeys());
  }

  @Override
  public boolean wasKeyPressed(Key key) {
    return inputState.wasPressed(key);
  }

  @Override
  public boolean wasKeyReleased(Key key) {
    return inputState.wasReleased(key);
  }

  /**
   * Updates the internal input state.
   *
   * <p>This method must be called exactly once per frame.
   *
   * <p>If the Processing window is not focused, the input state is cleared to prevent stuck keys
   * (e.g. after Alt-Tab).
   */
  @Override
  public void updateKeyState() {
    if (!applet.focused) {
      inputState.clear();
    }

    inputState.nextFrame();
  }

  /**
   * Receives Processing key events and translates them into engine-level key events.
   *
   * @param e the Processing key event
   */
  public void keyEvent(KeyEvent e) {
    Key key = Key.UNKNOWN;

    // SPACE explicit (important!)
    if (e.getKey() == ' ') {
      key = Key.SPACE;
    }
    // CODED keys (SHIFT, CTRL, ARROWS, ...)
    else if (e.getKey() == PApplet.CODED) {
      key = codedKeysMap.getOrDefault(e.getKeyCode(), Key.UNKNOWN);
    }
    // Normal Keys → keyCode (A–Z, 0–9)
    else {
      key = KeyCharacterMapper.getMappedKey(e.getKeyCode());

      // Fallback → char (ENTER, TAB, etc.)
      if (key == Key.UNKNOWN) {
        key = KeyCharacterMapper.getMappedKey(e.getKey());
      }
    }

    if (key == Key.UNKNOWN) return;

    switch (e.getAction()) {
      case KeyEvent.PRESS:
        handlePress(key, e);
        break;
      case KeyEvent.RELEASE:
        handleRelease(key, e);
        break;
      case KeyEvent.TYPE:
        handleTyped(key, e);
        break;
    }
  }

  private void handlePress(Key key, KeyEvent e) {
    inputState.onKeyPressed(key);
    fireKeyPressed(new engine.runtime.input.KeyEvent(key, e.getKey()));
  }

  private void handleRelease(Key key, KeyEvent e) {
    inputState.onKeyReleased(key);
    fireKeyReleased(new engine.runtime.input.KeyEvent(key, e.getKey()));
  }

  private void handleTyped(Key key, KeyEvent e) {
    fireKeyTyped(new engine.runtime.input.KeyEvent(key, e.getKey()));
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    if (listener != null) listeners.add(listener);
  }

  @Override
  public void removeKeyListener(KeyListener listener) {
    listeners.remove(listener);
  }

  protected void fireKeyPressed(engine.runtime.input.KeyEvent e) {
    for (KeyListener l : listeners) l.onKeyPressed(e);
  }

  protected void fireKeyReleased(engine.runtime.input.KeyEvent e) {
    for (KeyListener l : listeners) l.onKeyReleased(e);
  }

  protected void fireKeyTyped(engine.runtime.input.KeyEvent e) {
    for (KeyListener l : listeners) l.onKeyTyped(e);
  }
}
