package engine.input;

import java.util.Collection;

/**
 * Low-level interface for keyboard input handling.
 *
 * <p>Implementations are responsible for tracking key state across frames and providing both
 * continuous (held) and edge-based (pressed/released) queries.
 *
 * <p>This interface represents <b>raw key input</b>. Higher-level concepts such as actions or axes
 * should be resolved on top of this API.
 */
public interface KeyInput {

  /**
   * Updates the internal key state and advances the input system by one frame.
   *
   * <p>This method must be called exactly once per frame, typically from the main engine update
   * loop.
   *
   * <p>Implementations should clear transient states (pressed/released) during this call.
   */
  void updateKeyState();

  /**
   * Checks whether the given key is currently held down.
   *
   * @param key the key to query
   * @return {@code true} if the key is currently down, {@code false} otherwise
   */
  boolean isKeyPressed(Key key);

  /**
   * Checks whether the given key was pressed during the current frame.
   *
   * <p>This returns {@code true} only on the frame where the key transitions from an unpressed to a
   * pressed state.
   *
   * @param key the key to query
   * @return {@code true} if the key was pressed this frame
   */
  boolean wasKeyPressed(Key key);

  /**
   * Checks whether the given key was released during the current frame.
   *
   * <p>This returns {@code true} only on the frame where the key transitions from a pressed to an
   * unpressed state.
   *
   * @param key the key to query
   * @return {@code true} if the key was released this frame
   */
  boolean wasKeyReleased(Key key);

  /**
   * Returns all keys that are currently held down.
   *
   * <p>The returned collection represents the state of the current frame and should be treated as
   * read-only.
   *
   * @return a collection of currently pressed keys
   */
  Collection<Key> getPressedKeys();

  /**
   * Registers a listener that will receive key events.
   *
   * @param listener the listener to add
   */
  void addKeyListener(KeyListener listener);

  /**
   * Unregisters a previously added key listener.
   *
   * @param listener the listener to remove
   */
  void removeKeyListener(KeyListener listener);
}
