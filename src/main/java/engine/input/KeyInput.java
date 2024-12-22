package engine.input;

import java.util.Collection;

public interface KeyInput {

  void updateKeyState(); // To track key-specific states

  boolean isKeyPressed(Key key);

  Collection<Key> getPressedKeys();
}
