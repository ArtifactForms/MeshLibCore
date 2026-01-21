package engine.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import engine.input.Gamepad;
import engine.input.GamepadInput;
import engine.input.GamepadListener;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class JInputGamepadInput implements GamepadInput {

  private final List<JInputGamepad> gamepads = new ArrayList<>();

  @Override
  public void updateGamepadState() {
    detectGamepads();
    for (JInputGamepad gamepad : gamepads) {
      gamepad.poll();
    }
  }

  private void detectGamepads() {
    gamepads.clear();

    Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

    for (Controller controller : controllers) {
      if (controller.getType() == Controller.Type.GAMEPAD
          || controller.getType() == Controller.Type.STICK) {

        gamepads.add(new JInputGamepad(controller));
      }
    }
  }

  @Override
  public Collection<Gamepad> getGamepads() {
    return new ArrayList<>(gamepads);
  }

  @Override
  public Gamepad getGamepad(int index) {
    if (index < 0 || index >= gamepads.size()) {
      return null;
    }
    return gamepads.get(index);
  }

  @Override
  public void addGamepadListener(GamepadListener listener) { // TODO Auto-generated method stub
  }

  @Override
  public void removeGamepadListener(GamepadListener listener) { // TODO Auto-generated method stub
  }
}
