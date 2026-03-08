package common.game;

/**
 * Represents a specific quantity of an item occupying a single inventory slot. This class tracks
 * the item type via its ID and the current stack size.
 */
public class ItemStack {

  /** The unique identifier for the item type. */
  private final short itemId;

  /** The current number of items in this stack. */
  private int amount;

  /**
   * Creates a new item stack. * @param itemId The unique ID of the item.
   *
   * @param amount The initial quantity of items in the stack.
   */
  public ItemStack(short itemId, int amount) {
    this.itemId = itemId;
    this.amount = amount;
  }

  /**
   * Attempts to add a quantity to the current stack without exceeding the maximum limit. * @param
   * amountToAdd The quantity to be added to this stack.
   *
   * @param maxStackSize The maximum capacity allowed for this specific item type.
   * @return The remainder that could not fit into the stack (0 if everything fit).
   */
  public int add(int amountToAdd, int maxStackSize) {
    int space = maxStackSize - this.amount;
    int canAdd = Math.min(space, amountToAdd);

    this.amount += canAdd;

    // Return the "overflow" value
    return amountToAdd - canAdd;
  }

  /** Gets the unique ID of the item in this stack. * @return The item ID. */
  public short getItemId() {
    return itemId;
  }

  /** Gets the current quantity of items in this stack. * @return The amount of items. */
  public int getAmount() {
    return amount;
  }
}
