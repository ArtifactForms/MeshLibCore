package common.game;

public class Item {

  private final int id;
  private final String name;
  private final int maxStackSize;

  public Item(int id, String name, int maxStackSize) {
    this.id = (short) id;
    this.name = name;
    this.maxStackSize = maxStackSize;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getMaxStackSize() {
    return maxStackSize;
  }
}