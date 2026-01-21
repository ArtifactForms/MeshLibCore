package engine.input.action;

/** Defines how an action produces input values. */
public enum ActionType {
  /** Digital on/off action (e.g. Jump, Sprint) */
  BUTTON,

  /** One-dimensional axis (-1 .. +1) */
  AXIS_1D,

  /** Two-dimensional axis (x/y), e.g. movement or look */
  AXIS_2D
}
