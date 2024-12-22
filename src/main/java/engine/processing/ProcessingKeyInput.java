package engine.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import engine.input.Key;
import engine.input.KeyCharacterMapper;
import engine.input.KeyInput;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class ProcessingKeyInput implements KeyInput {

  private final PApplet applet;

  private HashSet<Key> keysDown;

  public ProcessingKeyInput(PApplet applet) {
    this.applet = applet;
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

  public void keyReleased() {}

  @Override
  public void updateKeyState() {
    // Handle frame-specific key state updates if necessary
  }

  private Key getCodedKey(int keyCode) {
    if (keyCode == PApplet.SHIFT) return Key.SHIFT;
    if (keyCode == PApplet.ALT) return Key.ALT;
    if (keyCode == PApplet.CONTROL) return Key.CTRL;
    return Key.UNKNOWN;
  }

  private void map(KeyEvent e, Key key) {
    if (key == Key.UNKNOWN) return;
    if (e.getAction() == KeyEvent.PRESS) {
      keysDown.add(key);
    }
    if (e.getAction() == KeyEvent.RELEASE) {
      keysDown.remove(key);
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
