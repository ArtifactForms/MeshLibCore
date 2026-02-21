package workspace.ui.event;

/**
 * Represents a listener for action events in the UI framework.
 *
 * <p>Implementations of this interface can be used to handle user interactions, such as button
 * clicks, menu selections, or other actions triggered within the user interface.
 *
 * <p>To use this interface, implement the {@code onActionPerformed()} method and associate the
 * listener with a UI component that supports actions, such as a button or menu item.
 *
 * <pre>
 * Example usage:
 *
 * UiButton button = new UiButton("Click Me");
 * button.setActionListener(new IActionListener() {
 *     &#64;Override
 *     public void onActionPerformed() {
 *         System.out.println("Button clicked!");
 *     }
 * });
 * </pre>
 */
public interface IActionListener {

  /**
   * Called when an action is performed.
   *
   * <p>This method is invoked in response to a user interaction with a UI component, such as
   * clicking a button or selecting a menu item. Implementations should define the behavior to
   * execute when the action occurs.
   */
  void onActionPerformed();
}
