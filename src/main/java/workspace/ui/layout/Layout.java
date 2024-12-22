package workspace.ui.layout;

import workspace.ui.elements.UiElement;

/**
 * Interface: Layout
 *
 * <p>Defines the contract for applying a layout strategy to a given {@link
 * workspace.ui.elements.UiElement}. The layout logic determines how a UI element's child components
 * are positioned, sized, and arranged within its allocated space based on specific layout rules.
 * This abstraction allows flexible and reusable layout behaviors to be implemented independently of
 * the actual UI element logic.
 *
 * <p>Implementations of this interface can represent various layout strategies such as grid-based
 * alignment, horizontal stacking, vertical stacking, or custom user-defined layouts.
 *
 * @see workspace.ui.elements.UiElement
 */
public interface Layout {

  /**
   * Applies the layout logic to the specified UI element and its children.
   *
   * <p>This method should compute and set the positions, sizes, and other layout properties of the
   * provided {@code UiElement} and its child elements according to the specific layout strategy
   * implemented by the concrete class.
   *
   * @param uiElement The UI element to which the layout logic should be applied.
   */
  void layout(UiElement uiElement);
}
