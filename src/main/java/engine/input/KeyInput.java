package engine.input;

import java.util.Collection;

/**
 * Interface for managing key input in the engine. Provides methods to track the state of keys,
 * handle listeners for key events, and retrieve pressed keys.
 */
public interface KeyInput {

  /**
   * Updates the state of keys, typically called in each game loop iteration. This method should
   * track the current state of each key (pressed, released, etc.).
   */
  void updateKeyState();

  /**
   * Checks if a specific key is currently pressed.
   *
   * @param key The key to check.
   * @return {@code true} if the key is pressed, otherwise {@code false}.
   */
  boolean isKeyPressed(Key key);

  /**
   * Retrieves a collection of all currently pressed keys.
   *
   * @return A collection of {@link Key} objects representing all currently pressed keys.
   */
  Collection<Key> getPressedKeys();

  /**
   * Adds a listener that will be notified when key events occur (e.g., key pressed, key released).
   *
   * @param listener The {@link KeyListener} to add.
   */
  void addKeyListener(KeyListener listener);

  /**
   * Removes a previously added key listener.
   *
   * @param listener The {@link KeyListener} to remove.
   */
  void removeKeyListener(KeyListener listener);
}
