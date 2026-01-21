package engine.input.action;

/** High-level, semantic input actions. Actions represent player intent, not physical input. */
public enum Action {
  MOVE(ActionType.AXIS_2D),
  LOOK(ActionType.AXIS_2D),

  JUMP(ActionType.BUTTON),
  SPRINT(ActionType.BUTTON),
  FIRE(ActionType.BUTTON);

  public final ActionType type;

  Action(ActionType type) {
    this.type = type;
  }
}
