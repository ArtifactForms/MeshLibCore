package client.ui.hotbar;

import client.settings.KeyBinds;
import common.game.Hotbar;
import engine.components.AbstractComponent;
import engine.runtime.input.Input;
import input.InputMode;
import input.InputSettings;

public class HotbarComponent extends AbstractComponent {

  private Input input;
  private Hotbar hotbar;

  public HotbarComponent(Input input, Hotbar hotbar) {
    this.input = input;
    this.hotbar = hotbar;
  }

  @Override
  public void onUpdate(float tpf) {
    if (InputSettings.inputMode != InputMode.GAMEPLAY) {
      return;
    }
    handleNumberKeys();
    handleMouseWheel();
  }

  private void handleMouseWheel() {
    int steps = (int) input.getMouseWheelDelta();

    if (steps == 0) return;
    if (steps > 0) hotbar.next();
    if (steps < 0) hotbar.previous();
  }

  private void handleNumberKeys() {
    if (input.isKeyPressed(KeyBinds.hotbarSlot1)) hotbar.setSelectedSlot(0);
    if (input.isKeyPressed(KeyBinds.hotbarSlot2)) hotbar.setSelectedSlot(1);
    if (input.isKeyPressed(KeyBinds.hotbarSlot3)) hotbar.setSelectedSlot(2);
    if (input.isKeyPressed(KeyBinds.hotbarSlot4)) hotbar.setSelectedSlot(3);
    if (input.isKeyPressed(KeyBinds.hotbarSlot5)) hotbar.setSelectedSlot(4);
    if (input.isKeyPressed(KeyBinds.hotbarSlot6)) hotbar.setSelectedSlot(5);
    if (input.isKeyPressed(KeyBinds.hotbarSlot7)) hotbar.setSelectedSlot(6);
    if (input.isKeyPressed(KeyBinds.hotbarSlot8)) hotbar.setSelectedSlot(7);
    if (input.isKeyPressed(KeyBinds.hotbarSlot9)) hotbar.setSelectedSlot(8);
  }
}
