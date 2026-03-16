package client.ui;

import common.game.ItemStack;

public interface InventoryView {

  void display();

  void hide();

  void setCursorStack(ItemStack stack);
  
  void setInventoryVersion(int inventoryVersion);
}
