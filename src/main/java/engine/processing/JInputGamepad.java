package engine.processing;

import java.util.EnumSet;
import java.util.Set;

import engine.input.Gamepad;
import engine.input.GamepadAxis;
import engine.input.GamepadButton;
import net.java.games.input.Component;
import net.java.games.input.Controller;

public class JInputGamepad implements Gamepad {

  private static final float DEADZONE = 0.15f;

  private final Controller controller;

  private final Set<GamepadButton> pressedButtons =
      EnumSet.noneOf(GamepadButton.class);

  public JInputGamepad(Controller controller) {
    this.controller = controller;
  }

  void poll() {
    pressedButtons.clear();
    controller.poll();

    for (Component component : controller.getComponents()) {
      float value = component.getPollData();

      if (component.isAnalog()) {
        handleAxis(component, value);
      } else {
        handleButton(component, value);
      }
    }
  }

  private void handleButton(Component component, float value) {
    if (value < 0.5f) return;

    GamepadButton button = JInputMapping.mapButton(component);
    if (button != null) {
      pressedButtons.add(button);
    }
  }

  private void handleAxis(Component component, float value) {
    if (Math.abs(value) < DEADZONE) return;

    // Axes werden direkt beim Abfragen gelesen
  }

  @Override
  public String getName() {
    return controller.getName();
  }

  @Override
  public boolean isButtonPressed(GamepadButton button) {
    return pressedButtons.contains(button);
  }

  @Override
  public float getAxis(GamepadAxis axis) {
    Component component = JInputMapping.mapAxis(controller, axis);
    if (component == null) return 0f;

    float value = component.getPollData();
    return Math.abs(value) < DEADZONE ? 0f : value;
  }

  @Override
  public Set<GamepadButton> getPressedButtons() {
    return Set.copyOf(pressedButtons);
  }
}
