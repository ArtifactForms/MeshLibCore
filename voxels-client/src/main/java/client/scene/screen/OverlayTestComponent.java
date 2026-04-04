package client.scene.screen;

import client.app.GameClient;
import engine.components.AbstractComponent;
import engine.runtime.input.Input;
import engine.runtime.input.Key;

public class OverlayTestComponent extends AbstractComponent {

  private Input input;

  private GameClient client;

  public OverlayTestComponent(Input input, GameClient client) {
    this.input = input;
    this.client = client;
  }

  @Override
  public void onUpdate(float tpf) {
    if (getOwner().getScene().getTopScreen() instanceof OverlayTestScreen) {
      return;
    }

    if (input.wasKeyPressed(Key.O)) {
      getOwner().getScene().pushScreen(new OverlayTestScreen(client));
    }
  }
}
