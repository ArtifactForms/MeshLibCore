package engine.components;

import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.scene.SceneNode;

/**
 * Toggles the active state of a SceneNode when a specific key was pressed.
 *
 * <p>This component is useful for enabling or disabling debug features, lights, cameras, effects,
 * or entire subtrees during runtime.
 *
 * <p>The toggle is edge-triggered, meaning it reacts only once per key press (press → release),
 * avoiding rapid toggling while the key is held down.
 */
public class ToggleActiveOnKey extends AbstractComponent {

  private final Input input;

  private SceneNode node;

  private final Key toggleKey;

  private boolean lastPressed;

  /**
   * Creates a new ToggleActiveOnKey component.
   *
   * @param input The input system used to query key states.
   * @param toggleKey The key that triggers toggling the active state.
   */
  public ToggleActiveOnKey(Input input, SceneNode node, Key toggleKey) {
    this.input = input;
    this.node = node;
    this.toggleKey = toggleKey;
  }

  @Override
  public void onUpdate(float tpf) {
    // Edge-triggered toggle
    boolean pressed = input.isKeyPressed(toggleKey);
    if (pressed && !lastPressed) {
      node.setActive(!node.isActive());
    }
    lastPressed = pressed;
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
