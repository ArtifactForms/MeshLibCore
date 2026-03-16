package common.game;

/**
 * Represents a fixed-size inventory containing {@link ItemStack} slots. Provides logic for adding,
 * removing and querying items.
 */
public class Inventory {

  private final Slot[] slots;

  public Inventory(int size) {

    if (size <= 0) {
      throw new IllegalArgumentException("Inventory size must be > 0");
    }

    slots = new Slot[size];

    for (int i = 0; i < size; i++) {
      slots[i] = new Slot();
    }
  }

  public boolean addItem(short itemId, int amount) {

    if (amount <= 0) return false;

    Item itemDef = ItemRegistry.getItem(itemId);
    int maxStack = (itemDef != null) ? itemDef.getMaxStackSize() : 64;

    int remaining = amount;

    // Phase 1: stack into existing stacks
    for (Slot slot : slots) {

      ItemStack stack = slot.getStack();

      if (stack != null && stack.getItemId() == itemId) {
        remaining = stack.add(remaining, maxStack);
      }

      if (remaining <= 0) return true;
    }

    // Phase 2: place into empty slots
    for (Slot slot : slots) {

      if (slot.isEmpty()) {

        int toAdd = Math.min(maxStack, remaining);

        slot.setStack(new ItemStack(itemId, toAdd));

        remaining -= toAdd;
      }

      if (remaining <= 0) return true;
    }

    return remaining < amount;
  }

  public ItemStack getSlot(int index) {
    checkIndex(index);
    return slots[index].getStack();
  }

  public void setSlot(int index, ItemStack stack) {
    checkIndex(index);
    slots[index].setStack(stack == null ? null : stack.copy());
  }

  public ItemStack remove(int slotIndex, int amount) {

    checkIndex(slotIndex);

    Slot slot = slots[slotIndex];
    ItemStack stack = slot.getStack();

    if (stack == null) return null;

    int current = stack.getAmount();
    int toRemove = Math.min(amount, current);

    ItemStack result = new ItemStack(stack.getItemId(), toRemove);

    stack.setAmount(current - toRemove);

    if (stack.getAmount() <= 0) {
      slot.clear();
    }

    return result;
  }

  public ItemStack removeOne(int index) {

    checkIndex(index);

    Slot slot = slots[index];
    ItemStack stack = slot.getStack();

    if (stack == null) return null;

    ItemStack result = new ItemStack(stack.getItemId(), 1);

    stack.setAmount(stack.getAmount() - 1);

    if (stack.getAmount() <= 0) {
      slot.clear();
    }

    return result;
  }

  public boolean hasItem(short itemId, int amount) {

    for (Slot slot : slots) {

      ItemStack stack = slot.getStack();

      if (stack == null) continue;

      if (stack.getItemId() == itemId && stack.getAmount() >= amount) {
        return true;
      }
    }

    return false;
  }

  public ItemStack[] getItems() {

    ItemStack[] items = new ItemStack[slots.length];

    for (int i = 0; i < slots.length; i++) {
      items[i] = slots[i].getStack();
    }

    return items;
  }

  /**
   * Replaces the contents of the inventory with the provided items. If the provided array is
   * smaller than the inventory, the remaining slots are cleared. If it is larger, extra items are
   * ignored.
   *
   * @param items the new items to set
   */
  public void setItems(ItemStack[] items) {

    if (items == null) {
      throw new IllegalArgumentException("Items array cannot be null");
    }

    for (int i = 0; i < slots.length; i++) {
      if (i < items.length) {
        // Use setSlot to leverage defensive copying and index checks
        setSlot(i, items[i]);
      } else {
        // Clear remaining slots if the input array is shorter
        slots[i].clear();
      }
    }
  }

  public int getSize() {
    return slots.length;
  }

  public void validateInventory() {

    for (int i = 0; i < slots.length; i++) {

      ItemStack stack = slots[i].getStack();

      if (stack == null) continue;

      if (stack.getAmount() <= 0) {
        throw new IllegalStateException(
            "Invalid stack size in slot " + i + ": " + stack.getAmount());
      }

      Item item = ItemRegistry.getItem(stack.getItemId());
      int maxStack = item != null ? item.getMaxStackSize() : 64;

      if (stack.getAmount() > maxStack) {
        throw new IllegalStateException("Stack overflow in slot " + i + ": " + stack.getAmount());
      }

      // Reference check
      for (int j = i + 1; j < slots.length; j++) {

        ItemStack other = slots[j].getStack();

        if (stack == other && stack != null) {
          throw new IllegalStateException(
              "Duplicate ItemStack reference between slots " + i + " and " + j);
        }
      }
    }
  }

  private void checkIndex(int index) {

    if (index < 0 || index >= slots.length) {
      throw new IndexOutOfBoundsException("Invalid slot index: " + index);
    }
  }
}
