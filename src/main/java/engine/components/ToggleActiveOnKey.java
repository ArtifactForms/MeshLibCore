package engine.components;

import engine.input.Input;
import engine.input.Key;

/**
 * Toggles the active state of the owning SceneNode when a specific key is pressed.
 *
 * <p>This component is useful for enabling or disabling debug features, lights, cameras, effects,
 * or entire subtrees during runtime.
 *
 * <p>The toggle is edge-triggered, meaning it reacts only once per key press (press → release),
 * avoiding rapid toggling while the key is held down.
 */
public class ToggleActiveOnKey extends AbstractComponent {

  private final Input input;
  private final Key toggleKey;

  private boolean lastPressed;

  /**
   * Creates a new ToggleActiveOnKey component.
   *
   * @param input The input system used to query key states.
   * @param toggleKey The key that triggers toggling the active state.
   */
  public ToggleActiveOnKey(Input input, Key toggleKey) {
    this.input = input;
    this.toggleKey = toggleKey;
  }

  @Override
  public void onUpdate(float tpf) {
    boolean pressed = input.isKeyPressed(toggleKey);

    // Edge-triggered toggle
    if (pressed && !lastPressed) {
      getOwner().setActive(!getOwner().isActive());
    }

    lastPressed = pressed;
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
