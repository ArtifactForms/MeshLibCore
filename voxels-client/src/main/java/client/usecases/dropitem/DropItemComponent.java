package client.usecases.dropitem;

import client.app.GameClient;
import client.settings.KeyBinds;
import common.game.Hotbar;
import common.network.packets.PlayerDropItemPacket;
import engine.components.AbstractComponent;
import engine.runtime.input.Input;
import input.InputMode;
import input.InputSettings;

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
    if (InputSettings.inputMode != InputMode.GAMEPLAY) {
      return;
    }

    if (!input.wasKeyPressed(KeyBinds.dropItem)) {
      return;
    }

    int selectedSlot = hotbar.getSelectedSlot();

    PlayerDropItemPacket packet = new PlayerDropItemPacket(selectedSlot);
    client.getNetwork().send(packet);
  }
}
