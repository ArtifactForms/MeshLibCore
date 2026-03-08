package common.game;

import common.world.BlockType;
import java.util.HashMap;
import java.util.Map;

/**
 * A central registry for managing and retrieving {@link Item} definitions. This class handles the
 * automatic mapping between world blocks and their corresponding item forms.
 */
public class ItemRegistry {

  /** Internal storage mapping item IDs to their respective {@link Item} definitions. */
  private static final Map<Short, Item> ITEMS = new HashMap<>();

  /**
   * Initializes the registry by iterating through all available {@link BlockType}s. Each valid
   * block (excluding {@code AIR}) is converted into a collectable item with a default stack size.
   */
  public static void init() {
    for (BlockType type : BlockType.values()) {
      // Air cannot be held as an item in the inventory
      if (type == BlockType.AIR) continue;

      // Create an item for each block with a default stack size of 64.
      // The display name is automatically formatted from the Enum name (e.g., "GRASS_BLOCK").
      String displayName = formatName(type.name());
      register(new Item(type.getId(), displayName, 64));
    }

    System.out.println("[ItemRegistry] Automatically registered " + ITEMS.size() + " items.");
  }

  /**
   * Internal method to add an item definition to the registry. * @param item The item definition to
   * store.
   */
  private static void register(Item item) {
    ITEMS.put((short) item.getId(), item);
  }

  /**
   * Retrieves an {@link Item} definition by its unique ID. * @param id The short ID of the item.
   *
   * @return The corresponding Item definition, or null if no item is registered with that ID.
   */
  public static Item getItem(short id) {
    return ITEMS.get(id);
  }

  /**
   * Formats an internal Enum name into a user-friendly display name. Example: "GRASS_BLOCK" becomes
   * "Grass Block". * @param enumName The raw name from the BlockType enum.
   *
   * @return A capitalized, space-separated string.
   */
  private static String formatName(String enumName) {
    if (enumName == null || enumName.isEmpty()) return "";

    String[] words = enumName.split("_");
    StringBuilder sb = new StringBuilder();
    for (String word : words) {
      if (word.isEmpty()) continue;
      sb.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
    }
    return sb.toString().trim();
  }
}
