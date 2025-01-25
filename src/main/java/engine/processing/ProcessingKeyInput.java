package engine.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import engine.input.Key;
import engine.input.KeyCharacterMapper;
import engine.input.KeyInput;
import engine.input.KeyListener;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class ProcessingKeyInput implements KeyInput {

  private static final HashMap<Integer, Key> codedKeysMap;

  private List<KeyListener> listeners;

  static {
    codedKeysMap = new HashMap<Integer, Key>();
    codedKeysMap.put(PApplet.SHIFT, Key.SHIFT);
    codedKeysMap.put(PApplet.ALT, Key.ALT);
    codedKeysMap.put(PApplet.CONTROL, Key.CTRL);
    codedKeysMap.put(PApplet.UP, Key.ARROW_UP);
    codedKeysMap.put(PApplet.DOWN, Key.ARROW_DOWN);
    codedKeysMap.put(PApplet.LEFT, Key.ARROW_LEFT);
    codedKeysMap.put(PApplet.RIGHT, Key.ARROW_RIGHT);
  }

  private HashSet<Key> keysDown;

  public ProcessingKeyInput(PApplet applet) {
    applet.registerMethod("keyEvent", this);
    this.keysDown = new HashSet<Key>();
    this.listeners = new ArrayList<KeyListener>();
  }

  @Override
  public boolean isKeyPressed(Key key) {
    return keysDown.contains(key);
  }

  @Override
  public Collection<Key> getPressedKeys() {
    synchronized (keysDown) {
      return new ArrayList<Key>(keysDown);
    }
  }

  @Override
  public void updateKeyState() {
    // Handle frame-specific key state updates if necessary
  }

  private Key getCodedKey(int keyCode) {
    return codedKeysMap.getOrDefault(keyCode, Key.UNKNOWN);
  }

  private void map(KeyEvent e, Key key) {
    if (key == Key.UNKNOWN) return;

    synchronized (keysDown) {
      if (e.getAction() == KeyEvent.PRESS) {
        keysDown.add(key);
        fireKeyPressed(new engine.input.KeyEvent(key, e.getKey()));
      }
      if (e.getAction() == KeyEvent.RELEASE) {
        keysDown.remove(key);
        fireKeyReleased(new engine.input.KeyEvent(key, e.getKey()));
      }
      if (e.getAction() == KeyEvent.TYPE) {
        fireKeyTyped(new engine.input.KeyEvent(key, e.getKey()));
      }
    }
  }

  public void keyEvent(KeyEvent e) {
    if (e.getKey() == PApplet.CODED) {
      Key key = getCodedKey(e.getKeyCode());
      map(e, key);
    } else {
      Key key = KeyCharacterMapper.getMappedKey(e.getKey());
      map(e, key);
    }
  }

  protected void fireKeyPressed(engine.input.KeyEvent e) {
    for (KeyListener listener : listeners) {
      listener.onKeyPressed(e);
    }
  }

  protected void fireKeyReleased(engine.input.KeyEvent e) {
    for (KeyListener listener : listeners) {
      listener.onKeyReleased(e);
    }
  }

  protected void fireKeyTyped(engine.input.KeyEvent e) {
    for (KeyListener listener : listeners) {
      listener.onKeyTyped(e);
    }
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    if (listener != null) listeners.add(listener);
  }

  @Override
  public void removeKeyListener(KeyListener listener) {
    if (listener != null) listeners.remove(listener);
  }
}
