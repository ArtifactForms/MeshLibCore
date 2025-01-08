package engine.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import engine.input.Key;
import engine.input.KeyCharacterMapper;
import engine.input.KeyInput;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class ProcessingKeyInput implements KeyInput {

  private static final HashMap<Integer, Key> codedKeysMap;

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
    keysDown = new HashSet<Key>();
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
      }
      if (e.getAction() == KeyEvent.RELEASE) {
        keysDown.remove(key);
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
}
