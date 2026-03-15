package client.usecases.openinventory;

import client.settings.KeyBinds;
import client.ui.cursor.SimpleCursorComponent;
import engine.components.AbstractComponent;
import engine.runtime.input.Input;
import engine.runtime.input.MouseMode;
import engine.scene.SceneNode;

public class OpenInventoryComponent extends AbstractComponent {

  private boolean inventoryOpen;
  private boolean lastPressed;
  private Input input;
  private OpenInventoryController controller;
  private SceneNode cursor;

  public OpenInventoryComponent(Input input, OpenInventoryController controller) {
    this.input = input;
    this.controller = controller;
    this.cursor = new SceneNode("Cursor", new SimpleCursorComponent(input));
  }

  @Override
  public void onUpdate(float tpf) {
    boolean pressed = input.isKeyPressed(KeyBinds.openCloseInventory);

    if (!lastPressed && pressed) {
      inventoryOpen = !inventoryOpen;
      if (inventoryOpen) {
        controller.onInventoryOpen();
        input.setMouseMode(MouseMode.ABSOLUTE);
        getOwner().getScene().getUIRoot().addChild(cursor);
      } else {
        controller.onInventoryClose();
        input.setMouseMode(MouseMode.LOCKED);
        getOwner().getScene().getUIRoot().removeChild(cursor);
      }
    }

    lastPressed = pressed;
  }
}
