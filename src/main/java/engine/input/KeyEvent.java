package engine.input;

/**
 * Represents an event related to a key input, including the specific key involved in the event.
 * This class is used to pass key information to listeners when key events occur.
 */
public class KeyEvent {

  private final Key key;

  /**
   * Constructs a new {@link KeyEvent} with the specified key.
   *
   * @param key The {@link Key} associated with this event.
   */
  public KeyEvent(Key key) {
    this.key = key;
  }

  /**
   * Retrieves the {@link Key} associated with this key event.
   *
   * @return The {@link Key} that was involved in the event.
   */
  public Key getKey() {
    return key;
  }
}
