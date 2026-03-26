package client.usecases.dropitem;

import client.app.GameClient;
import client.settings.KeyBinds;
import common.game.Hotbar;
import common.network.packets.PlayerDropItemPacket;
import engine.components.AbstractComponent;
import engine.runtime.input.Input;
import engine.scene.screen.GameScreen;

public class DropItemComponent extends AbstractComponent {

  private Input input;
  private Hotbar hotbar;
  private GameClient client;

  public DropItemComponent(Input input, GameClient client, Hotbar hotbar) {
    this.input = input;
    this.client = client;
    this.hotbar = hotbar;
  }

  @Override
  public void onUpdate(float tpf) {
    if (isGameplayBlocked()) {
      return;
    }

    if (!input.wasKeyPressed(KeyBinds.dropItem)) {
      return;
    }

    int selectedSlot = hotbar.getSelectedSlot();

    PlayerDropItemPacket packet = new PlayerDropItemPacket(selectedSlot);
    client.getNetwork().send(packet);
  }

  private boolean isGameplayBlocked() {
    GameScreen screen = getOwner().getScene().getTopScreen();
    if (screen == null) {
      return false;
    } else {
      return screen.blocksGameplay();
    }
  }
}
