package client.player;

import engine.components.AbstractComponent;
import engine.runtime.input.Input;
import engine.runtime.input.Key;

public class OpenInventoryControl extends AbstractComponent {

  private Input input;
  private Key openInventoryKey = Key.E;

  public OpenInventoryControl(Input input) {
    this.input = input;
  }

  @Override
  public void onUpdate(float tpf) {
    if (!input.wasKeyReleased(openInventoryKey)) return;
  }
}
