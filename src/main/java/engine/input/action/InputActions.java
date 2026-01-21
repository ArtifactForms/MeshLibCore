package engine.input.action;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import engine.input.Input;
import engine.input.Key;
import math.Vector2f;

/**
 * Resolves high-level input actions from the engine Input abstraction.
 * Similar in spirit to Unity's Input Actions system, but polling-based
 * and fully engine-controlled.
 */
public class InputActions {

  private Input input;

  private final Map<Action, List<ButtonBinding>> buttonBindings =
      new EnumMap<>(Action.class);

  private final Map<Action, List<Axis2DBinding>> axis2DBindings =
      new EnumMap<>(Action.class);

  /** Called by the Input implementation once per frame */
  public void update(Input input) {
    this.input = input;
  }

  public void bindButton(Action action, Key key) {
    buttonBindings
        .computeIfAbsent(action, a -> new ArrayList<>())
        .add(new ButtonBinding(action, key));
  }

  public void bindAxis2D(Action action, Key key, Vector2f value) {
    axis2DBindings
        .computeIfAbsent(action, a -> new ArrayList<>())
        .add(new Axis2DBinding(action, key, value));
  }

  public boolean isDown(Action action) {
    return getButtonBindings(action).stream()
        .anyMatch(b -> input.isKeyPressed(b.key));
  }

  public boolean wasPressed(Action action) {
    return getButtonBindings(action).stream()
        .anyMatch(b -> input.wasKeyPressed(b.key));
  }

  public boolean wasReleased(Action action) {
    return getButtonBindings(action).stream()
        .anyMatch(b -> input.wasKeyReleased(b.key));
  }

  public Vector2f getVector(Action action) {
    Vector2f result = Vector2f.ZERO;

    for (Axis2DBinding b : getAxis2DBindings(action)) {
      if (input.isKeyPressed(b.key)) {
        result = result.add(b.value);
      }
    }

    return result.clamp(-1f, 1f);
  }

  private List<ButtonBinding> getButtonBindings(Action action) {
    return buttonBindings.getOrDefault(action, List.of());
  }

  private List<Axis2DBinding> getAxis2DBindings(Action action) {
    return axis2DBindings.getOrDefault(action, List.of());
  }
}
