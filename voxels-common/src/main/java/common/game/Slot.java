package common.game;

public class Slot {

  private ItemStack stack;

  public ItemStack getStack() {
    return stack;
  }

  public void setStack(ItemStack stack) {
    this.stack = stack;
  }

  public boolean isEmpty() {
    return stack == null;
  }

  public void clear() {
    stack = null;
  }
}
