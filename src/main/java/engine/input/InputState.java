package engine.input;

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents the current keyboard input state in a frame-based manner.
 *
 * <p>{@code InputState} decouples low-level key events (PRESS / RELEASE) from high-level input
 * queries used by game logic.
 *
 * <p>It tracks three distinct states per key:
 *
 * <ul>
 *   <li><b>Down</b> – the key is currently held down
 *   <li><b>Pressed</b> – the key was pressed during the current frame
 *   <li><b>Released</b> – the key was released during the current frame
 * </ul>
 *
 * <p>The {@link #nextFrame()} method must be called exactly once per frame to advance the input
 * state and clear transient flags.
 *
 * <p>This class is engine-agnostic and does not depend on any specific windowing or input backend
 * (e.g. Processing, GLFW, AWT).
 */
public class InputState {

  /** Keys that are currently held down. */
  private final Set<Key> down = EnumSet.noneOf(Key.class);

  /** Keys that were pressed during the current frame. */
  private final Set<Key> pressed = EnumSet.noneOf(Key.class);

  /** Keys that were released during the current frame. */
  private final Set<Key> released = EnumSet.noneOf(Key.class);

  /**
   * Notifies the input state that a key press event occurred.
   *
   * <p>If the key was not previously held down, it will be marked as {@code pressed} for the
   * current frame.
   *
   * @param key the logical key that was pressed
   */
  public void onKeyPressed(Key key) {
    if (key == Key.UNKNOWN) return;

    if (down.add(key)) {
      pressed.add(key);
    }
  }

  /**
   * Notifies the input state that a key release event occurred.
   *
   * <p>If the key was previously held down, it will be marked as {@code released} for the current
   * frame.
   *
   * @param key the logical key that was released
   */
  public void onKeyReleased(Key key) {
    if (key == Key.UNKNOWN) return;

    if (down.remove(key)) {
      released.add(key);
    }
  }

  /**
   * Advances the input state to the next frame.
   *
   * <p>This method <b>must</b> be called exactly once per frame (typically at the beginning or end
   * of the update loop).
   *
   * <p>It clears all transient per-frame states such as {@code pressed} and {@code released}.
   */
  public void nextFrame() {
    pressed.clear();
    released.clear();
  }

  /**
   * Clears all input state immediately.
   *
   * <p>This should be called on focus loss, window minimization, or any situation where key release
   * events may have been lost (e.g. Alt-Tab).
   */
  public void clear() {
    down.clear();
    pressed.clear();
    released.clear();
  }

  /**
   * Checks whether a key is currently held down.
   *
   * @param key the key to query
   * @return {@code true} if the key is currently down
   */
  public boolean isDown(Key key) {
    return down.contains(key);
  }

  /**
   * Checks whether a key was pressed during the current frame.
   *
   * @param key the key to query
   * @return {@code true} if the key was pressed this frame
   */
  public boolean wasPressed(Key key) {
    return pressed.contains(key);
  }

  /**
   * Checks whether a key was released during the current frame.
   *
   * @param key the key to query
   * @return {@code true} if the key was released this frame
   */
  public boolean wasReleased(Key key) {
    return released.contains(key);
  }

  /**
   * Checks whether any of the given keys are currently held down.
   *
   * @param keys the keys to test
   * @return {@code true} if at least one key is down
   */
  public boolean isAnyDown(Key... keys) {
    for (Key k : keys) {
      if (down.contains(k)) return true;
    }
    return false;
  }

  /**
   * Returns an immutable snapshot of all currently held keys.
   *
   * @return a read-only set of keys that are currently down
   */
  public Set<Key> getDownKeys() {
    return Set.copyOf(down);
  }

  /**
   * Returns an immutable snapshot of all currently released keys.
   *
   * @return a read-only set of keys that are currently released
   */
  public Set<Key> getReleasedKeys() {
    return Set.copyOf(released);
  }
}
