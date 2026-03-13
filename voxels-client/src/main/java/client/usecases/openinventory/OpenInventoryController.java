package client.usecases.openinventory;

import client.app.GameClient;
import client.ui.InventoryView;

public class OpenInventoryController {

  private GameClient client;
  private InventoryView view;

  public OpenInventoryController(GameClient client) {
    this.client = client;
    this.view = client.getView().getInventoryView();
  }

  public void onInventoryOpen() {
    view.display();
  }

  public void onInventoryClose() {
    view.hide();
  }
}
