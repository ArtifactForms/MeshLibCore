package workspace.ui.layout;

/**
 * Enum representing different anchor points for UI layout alignment. These anchor points determine
 * how a UI element will align within its parent container or layout system.
 */
public enum Anchor {

  /** No specific anchoring; element remains free or defaults to its own positioning logic. */
  NONE,

  /** Anchors element to the top-left corner of the parent container. */
  TOP_LEFT,

  /** Anchors element to the top-right corner of the parent container. */
  TOP_RIGHT,

  /** Anchors element to the bottom-right corner of the parent container. */
  BOTTOM_RIGHT,

  /** Anchors element to the bottom-left corner of the parent container. */
  BOTTOM_LEFT,

  /** Anchors element to the center left of the parent container. */
  CENTER_LEFT,

  /** Anchors element to the center top of the parent container. */
  CENTER_TOP,

  /** Anchors element to the center right of the parent container. */
  CENTER_RIGHT,

  /** Anchors element to the center bottom of the parent container. */
  CENTER_BOTTOM,

  /** Anchors element to the exact center of the parent container. */
  CENTER,

  /** Makes the element extend horizontally across a wide area starting from the left edge. */
  LEFT_WIDE,

  /** Makes the element extend horizontally across a wide area starting from the top edge. */
  TOP_WIDE,

  /** Makes the element extend horizontally across a wide area starting from the right edge. */
  RIGHT_WIDE,

  /** Makes the element extend horizontally across a wide area starting from the bottom edge. */
  BOTTOM_WIDE,

  /** Anchors the element in a vertical-centered wide area relative to its container. */
  VERTICAL_CENTER_WIDE,

  /** Anchors the element in a horizontally-centered wide area relative to its container. */
  HORIZONTAL_CENTER_WIDE,

  /** Extends the element to occupy the full rectangle of its parent container. */
  FULL_RECTANGLE,

  /** Makes the element extend vertically across a wide area starting from the center. */
  VERTICAL_WIDE,

  /** Makes the element extend horizontally across a wide area starting from the center. */
  HORIZONTAL_WIDE;
}
