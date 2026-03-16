package common.game;

public class Item {

  private final short id;
  private final String name;
  private final int maxStackSize;

  public Item(short id, String name, int maxStackSize) {
    this.id = id;
    this.name = name;
    this.maxStackSize = maxStackSize;
  }

  public short getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getMaxStackSize() {
    return maxStackSize;
  }
}
