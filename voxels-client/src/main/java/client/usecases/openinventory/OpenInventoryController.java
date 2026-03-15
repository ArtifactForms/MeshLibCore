package client.usecases.openinventory;

import client.app.GameClient;
import client.ui.InventoryView;
import input.InputMode;
import input.InputSettings;

public class OpenInventoryController {

  private GameClient client;
  private InventoryView view;
  private InputMode oldInputMode;

  public OpenInventoryController(GameClient client) {
    this.client = client;
    this.view = client.getView().getInventoryView();
  }

  public void onInventoryOpen() {
    oldInputMode = InputSettings.inputMode;
    InputSettings.inputMode = InputMode.UI;
    view.display();
  }

  public void onInventoryClose() {
    view.hide();
    InputSettings.inputMode = oldInputMode;
  }
}
