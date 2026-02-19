package engine.runtime.input;

/**
 * Interface for receiving key input events, such as when a key is pressed, released, or typed.
 * Implement this interface to handle specific key events in the engine.
 */
public interface KeyListener {

  /**
   * Called when a key is pressed.
   *
   * @param e The {@link KeyEvent} containing information about the key event.
   */
  void onKeyPressed(KeyEvent e);

  /**
   * Called when a key is released.
   *
   * @param e The {@link KeyEvent} containing information about the key event.
   */
  void onKeyReleased(KeyEvent e);

  /**
   * Called when a key is typed (usually when a character is inputted, for example, on a keyboard).
   *
   * @param e The {@link KeyEvent} containing information about the key event.
   */
  void onKeyTyped(KeyEvent e);
}
