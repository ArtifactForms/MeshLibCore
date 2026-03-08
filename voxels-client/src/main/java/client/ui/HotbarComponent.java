package client.ui;

import common.game.Hotbar;
import engine.components.AbstractComponent;
import engine.runtime.input.Input;
import engine.runtime.input.Key;

public class HotbarComponent extends AbstractComponent {

  private Input input;
  private Hotbar hotbar;

  public HotbarComponent(Input input, Hotbar hotbar) {
    this.input = input;
    this.hotbar = hotbar;
  }

  @Override
  public void onUpdate(float tpf) {
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
    if (input.isKeyPressed(Key.NUM_1)) hotbar.setSelectedSlot(0);
    if (input.isKeyPressed(Key.NUM_2)) hotbar.setSelectedSlot(1);
    if (input.isKeyPressed(Key.NUM_3)) hotbar.setSelectedSlot(2);
    if (input.isKeyPressed(Key.NUM_4)) hotbar.setSelectedSlot(3);
    if (input.isKeyPressed(Key.NUM_5)) hotbar.setSelectedSlot(4);
    if (input.isKeyPressed(Key.NUM_6)) hotbar.setSelectedSlot(5);
    if (input.isKeyPressed(Key.NUM_7)) hotbar.setSelectedSlot(6);
    if (input.isKeyPressed(Key.NUM_8)) hotbar.setSelectedSlot(7);
    if (input.isKeyPressed(Key.NUM_9)) hotbar.setSelectedSlot(8);
  }
}
