package common.game;

import java.util.ArrayList;

public class Hotbar {

  public static final int SIZE = 9;

  private final Inventory inventory;

  private int selectedSlot = 0;

  private ArrayList<HotbarListener> listeners = new ArrayList<>();

  public Hotbar(Inventory inventory) {
    this.inventory = inventory;
  }

  public ItemStack getSlot(int index) {
    return inventory.getSlot(index);
  }

  public void setSlot(int index, ItemStack stack) {
    inventory.setSlot(index, stack);
  }

  public ItemStack getSelected() {
    return inventory.getSlot(selectedSlot);
  }

  public int getSelectedSlot() {
    return selectedSlot;
  }

  public void setSelectedSlot(int slot) {

    if (slot < 0 || slot >= SIZE) return;
    if (slot == selectedSlot) return;

    selectedSlot = slot;
    fireSelectionChanged();
  }

  public void next() {
    selectedSlot = (selectedSlot + 1) % SIZE;
    fireSelectionChanged();
  }

  public void previous() {
    selectedSlot = (selectedSlot - 1 + SIZE) % SIZE;
    fireSelectionChanged();
  }

  private void fireSelectionChanged() {
    for (HotbarListener listener : listeners) {
      listener.onSlotChanged(this);
    }
  }

  public void addListener(HotbarListener listener) {
    if (listener == null) return;
    listeners.add(listener);
  }

  public void removeHotbarListener(HotbarListener listener) {
    listeners.remove(listener);
  }
}
