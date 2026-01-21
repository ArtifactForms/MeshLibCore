package engine.input.action;

import engine.input.Key;

/** Maps one or more physical keys to a button action. */
public class ButtonBinding {

  public final Action action;
  public final Key key;

  public ButtonBinding(Action action, Key key) {
    this.action = action;
    this.key = key;
  }
}
