package common.game;

public interface InventoryListener {

  void onSlotChanged(int slot, short itemId, int amount);
}
