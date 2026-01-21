package engine.input;

/**
 * Represents an event related to a key input, including the specific key and the associated
 * character input (if applicable) in the event. This class is used to pass key information to
 * listeners when key events such as key presses or key releases occur.
 */
public class KeyEvent {

  private final char character;
  
  private final Key key;

  /**
   * Constructs a new {@link KeyEvent} with the specified key and associated character.
   *
   * @param key The {@link Key} associated with this event.
   * @param character The character associated with the key press, if applicable.
   */
  public KeyEvent(Key key, char character) {
    this.key = key;
    this.character = character;
  }

  /**
   * Retrieves the {@link Key} associated with this key event.
   *
   * @return The {@link Key} that was involved in the event.
   */
  public Key getKey() {
    return key;
  }

  /**
   * Retrieves the character associated with this key event. This is useful for cases where the key
   * input results in a character being typed (e.g., typing letters).
   *
   * @return The character input associated with this key event, or a default value (e.g., '\0') if
   *     no character is applicable.
   */
  public char getChar() {
    return character;
  }
}
