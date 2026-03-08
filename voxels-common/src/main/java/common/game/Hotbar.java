package common.game;

import java.util.ArrayList;

public class Hotbar {

  public static final int SIZE = 9;

  private final ItemStack[] slots = new ItemStack[SIZE];
  private int selectedSlot = 0;
  private ArrayList<HotbarListener> listeners = new ArrayList<HotbarListener>();

  public ItemStack getSlot(int index) {
    return slots[index];
  }

  public void setSlot(int index, ItemStack stack) {
    slots[index] = stack;
  }

  public ItemStack getSelected() {
    return slots[selectedSlot];
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
