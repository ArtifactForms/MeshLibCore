package engine.backend.processing;

import engine.runtime.input.GamepadAxis;
import engine.runtime.input.GamepadButton;
import net.java.games.input.Component;
import net.java.games.input.Controller;

public final class JInputMapping {

  private JInputMapping() {}

  public static GamepadButton mapButton(Component component) {
    if (!(component.getIdentifier() instanceof Component.Identifier.Button)) {
      return null;
    }

    Component.Identifier.Button button = (Component.Identifier.Button) component.getIdentifier();

    if (button == Component.Identifier.Button._0) return GamepadButton.A;
    if (button == Component.Identifier.Button._1) return GamepadButton.B;
    if (button == Component.Identifier.Button._2) return GamepadButton.X;
    if (button == Component.Identifier.Button._3) return GamepadButton.Y;

    if (button == Component.Identifier.Button._4) return GamepadButton.LEFT_BUMPER;
    if (button == Component.Identifier.Button._5) return GamepadButton.RIGHT_BUMPER;

    if (button == Component.Identifier.Button._6) return GamepadButton.BACK;
    if (button == Component.Identifier.Button._7) return GamepadButton.START;

    if (button == Component.Identifier.Button._8) return GamepadButton.LEFT_STICK;
    if (button == Component.Identifier.Button._9) return GamepadButton.RIGHT_STICK;

    return null;
  }

  public static Component mapAxis(Controller controller, GamepadAxis axis) {
    switch (axis) {
      case LEFT_X:
        return controller.getComponent(Component.Identifier.Axis.X);
      case LEFT_Y:
        return controller.getComponent(Component.Identifier.Axis.Y);

      case RIGHT_X:
        return controller.getComponent(Component.Identifier.Axis.Z);
      case RIGHT_Y:
        return controller.getComponent(Component.Identifier.Axis.RZ);

      case LEFT_TRIGGER:
      case RIGHT_TRIGGER:
        return controller.getComponent(Component.Identifier.Axis.Z);

      default:
        return null;
    }
  }
}
