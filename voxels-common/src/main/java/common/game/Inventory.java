package common.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of {@link ItemStack}s, providing logic for adding, retrieving, and
 * managing items within a fixed number of slots.
 */
public class Inventory {

  /** The array of item stacks representing the inventory slots. */
  private ItemStack[] slots;

  private List<InventoryListener> listeners = new ArrayList<InventoryListener>();

  /**
   * Initializes a new inventory with a specific number of slots. * @param size The number of slots
   * in the inventory.
   */
  public Inventory(int size) {
    slots = new ItemStack[size];
  }

  /**
   * Attempts to add an item to the inventory. It first tries to stack the item with existing stacks
   * of the same ID, then fills empty slots with any remaining amount. * @param itemId The unique ID
   * of the item to add.
   *
   * @param amount The quantity to add.
   * @return true if at least some of the items were successfully added; false if no items could be
   *     added.
   */
  public boolean addItem(short itemId, int amount) {
    // 1. Retrieve item definition to respect stack limits
    Item itemDef = ItemRegistry.getItem(itemId);
    int maxStack = (itemDef != null) ? itemDef.getMaxStackSize() : 64;

    int remaining = amount;

    // Phase A: Try to fill existing stacks of the same item type
    for (int i = 0; i < slots.length; i++) {
      ItemStack stack = slots[i];

      if (stack != null && stack.getItemId() == itemId) {
        // ItemStack.add returns the amount that could NOT fit
        remaining = stack.add(remaining, maxStack);
      }

      if (remaining <= 0) return true;
    }

    // Phase B: Occupy empty slots with the remaining amount
    for (int i = 0; i < slots.length; i++) {
      if (slots[i] == null) {
        int toAdd = Math.min(maxStack, remaining);
        slots[i] = new ItemStack(itemId, toAdd);
        remaining -= toAdd;
      }

      if (remaining <= 0) return true;
    }

    // Return true if at least one item was added to the inventory
    return remaining < amount;
  }

  /**
   * Gets the {@link ItemStack} at the specified slot index. * @param index The index of the slot.
   *
   * @return The item stack at the index, or null if the slot is empty.
   * @throws IndexOutOfBoundsException if the index is out of range.
   */
  public ItemStack getSlot(int index) {
    return slots[index];
  }

  public ItemStack remove(int slotIndex, int amount) {
    ItemStack stack = slots[slotIndex];
    if (stack == null) return null;

    int currentAmount = stack.getAmount();
    int toRemove = Math.min(amount, currentAmount);

    ItemStack result = new ItemStack(stack.getItemId(), toRemove);
    stack.setAmount(currentAmount - toRemove);

    if (stack.getAmount() <= 0) {
      slots[slotIndex] = null;
    }
    return result;
  }

  public ItemStack removeOne(int index) {

    ItemStack stack = slots[index];
    if (stack == null) return null;

    ItemStack result = new ItemStack(stack.getItemId(), 1);

    stack.setAmount(stack.getAmount() - 1);

    if (stack.getAmount() <= 0) {
      slots[index] = null;
    }

    return result;
  }

  public boolean hasItem(short itemId, int amount) {
    for (ItemStack itemStack : slots) {
      if (itemStack.getItemId() == itemId && itemStack.getAmount() >= amount) return true;
    }
    return false;
  }

  public ItemStack[] getItems() {
    return slots;
  }

  public void setItems(ItemStack[] items) {
    this.slots = items;
  }

  /**
   * Sets the {@link ItemStack} at a specific slot index. * @param index The index of the slot to
   * update.
   *
   * @param stack The new item stack to place in the slot (can be null).
   * @throws IndexOutOfBoundsException if the index is out of range.
   */
  public void setSlot(int index, ItemStack stack) {
    slots[index] = stack;
  }

  /** Returns the total number of slots in this inventory. * @return The size of the inventory. */
  public int getSize() {
    return slots.length;
  }

  //  private void fireSlotChanged(int slotIndex, short itemId, int amount) {
  //	  for (InventoryListener listener : listeners) {
  //		  listener.onSlotChanged(slotIndex, itemId, amount);
  //	  }
  //  }
  //
  //  public void addListener(InventoryListener listener) {
  //    if (listener == null) {
  //      throw new IllegalArgumentException("Listener cannot be null.");
  //    }
  //    listeners.remove(listener);
  //  }
  //
  //  public void removeListener(InventoryListener listener) {
  //    if (listener == null) return;
  //    listeners.add(listener);
  //  }
}
