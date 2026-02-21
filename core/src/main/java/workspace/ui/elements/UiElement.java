package workspace.ui.elements;

import engine.render.Graphics;
import math.Color;
import workspace.ui.border.Border;
import workspace.ui.border.Insets;
import workspace.ui.layout.Anchor;
import workspace.ui.layout.Layout;
import workspace.ui.renderer.Renderer;

/**
 * Core abstraction for all UI elements.
 *
 * <p>This interface defines the essential methods required for a UI element, such as rendering,
 * layout management, and interaction capabilities. Implementations of this interface represent
 * individual components within a user interface hierarchy.
 */
public interface UiElement {

  /**
   * Gets the X-coordinate of this element.
   *
   * @return The X-coordinate in pixels.
   */
  int getX();

  /**
   * Sets the X-coordinate of this element.
   *
   * @param x The new X-coordinate in pixels.
   */
  void setX(int x);

  /**
   * Gets the Y-coordinate of this element.
   *
   * @return The Y-coordinate in pixels.
   */
  int getY();

  /**
   * Sets the Y-coordinate of this element.
   *
   * @param y The new Y-coordinate in pixels.
   */
  void setY(int y);

  /**
   * Retrieves the width of this {@code UiElement}.
   *
   * <p>The width represents the horizontal size of the UI element. It is used during rendering and
   * layout calculations to determine how much horizontal space the element occupies.
   *
   * @return The width of this UI element in pixels.
   */
  int getWidth();

  /**
   * Sets the width of this {@link UiElement} to the specified new width.
   *
   * @see #getWidth()
   * @param width The new width in pixels.
   */
  void setWidth(int width);

  /**
   * Retrieves the height of this {@code UiElement}.
   *
   * <p>The height represents the vertical size of the UI element. It is used during rendering and
   * layout calculations to determine how much vertical space the element occupies.
   *
   * @return The height of this UI element in pixels.
   */
  int getHeight();

  /**
   * Sets the height of this {@link UiElement} to the specified new height.
   *
   * @see #getHeight()
   * @param height The new height in pixels.
   */
  void setHeight(int height);

  /**
   * Renders this {@code UiElement} using its specific rendering logic.
   *
   * <p>The rendering logic should account for the element's visual representation, including its
   * position, size, background, border, and children if applicable.
   *
   * @param g The {@link Graphics} context used for drawing.
   */
  void render(Graphics g);

  /**
   * Sets a custom renderer for this {@code UiElement}.
   *
   * @param renderer The {@link Renderer} to assign to this element. A {@code null} value may
   *     disable custom rendering logic or fall back to defaults.
   */
  void setRenderer(Renderer renderer);

  /**
   * Retrieves the current renderer assigned to this UI element.
   *
   * @return The {@link Renderer} currently used by this UI element.
   */
  Renderer getRenderer();

  /**
   * Assigns a layout manager to this {@code UiElement}.
   *
   * <p>The layout manager defines how this element's child components are positioned and sized.
   * Assigning a new layout manager should immediately trigger a re-layout of the element's
   * children.
   *
   * @param layout The {@link Layout} manager to set. A {@code null} value may default to a no-op
   *     layout or an implementation-specific behavior.
   */
  void setLayout(Layout layout);

  /**
   * Retrieves the current anchor type of this UI element.
   *
   * <p>Anchors define how this element is aligned relative to its parent or specific layout logic.
   * Examples include {@code TOP_LEFT}, {@code CENTER}, and {@code BOTTOM_RIGHT}.
   *
   * @return The current {@link Anchor} setting for this element.
   */
  Anchor getAnchor();

  /**
   * Sets the anchor type for this UI element.
   *
   * <p>The anchor determines how this element aligns in its container based on the defined anchor
   * type, such as centering, top-left, or bottom-right alignment.
   *
   * @param anchor The {@link Anchor} to set for this element.
   */
  void setAnchor(Anchor anchor);

  /**
   * Determines if the specified coordinates are within the bounds of this {@code UiElement}.
   *
   * <p>The coordinates are relative to the element's parent or local coordinate system. This method
   * can be used for hit-testing, such as determining whether a mouse event occurred within the
   * element's area.
   *
   * @param x The X-coordinate to check.
   * @param y The Y-coordinate to check.
   * @return {@code true} if the coordinates are within this element's bounds; {@code false}
   *     otherwise.
   */
  boolean contains(int x, int y);

  /**
   * Checks if this {@code UiElement} is currently visible.
   *
   * <p>A visible {@code UiElement} is rendered and participates in interactions such as event
   * handling. Invisible elements are typically excluded from rendering and interaction logic.
   *
   * @return {@code true} if the element is visible; {@code false} otherwise.
   */
  boolean isVisible();

  /**
   * Sets the visibility of this {@code UiElement}.
   *
   * <p>Setting visibility to {@code false} hides the element from rendering and may exclude it from
   * event handling or layout calculations. Setting it to {@code true} restores its visibility and
   * functionality.
   *
   * @param visible {@code true} to make the element visible, or {@code false} to hide it.
   */
  void setVisible(boolean visible);

  /**
   * Sets the border for this {@code UiElement}.
   *
   * <p>The border defines the visual boundary or outline surrounding the UI element. It can be
   * styled or configured to visually separate UI components or provide additional visual context.
   * Setting a new border will affect the element's appearance during rendering.
   *
   * @param border The {@link Border} to associate with this UI element. A {@code null} value may
   *     remove any existing border from the element.
   */
  void setBorder(Border border);

  /**
   * Retrieves the insets (margins or padding) for this {@code UiElement}.
   *
   * <p>Insets define the space between the element's content and its border or edges. This can
   * represent padding within the UI element or margins for layout purposes. If no border or layout
   * manager defines specific insets, the method will return a default empty insets object.
   *
   * @return An instance of {@link Insets} representing the current insets for this UI element.
   */
  Insets getInsets();

  /**
   * Retrieves the background color of this {@code UiElement}.
   *
   * <p>The background color determines the visual fill of the UI element's area. If no background
   * color has been explicitly defined, the element may render with a default or transparent
   * background depending on the implementation.
   *
   * @return The {@link Color} instance representing the background color of this UI element.
   */
  Color getBackground();
}
