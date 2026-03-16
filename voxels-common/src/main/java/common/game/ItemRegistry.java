package common.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.logging.Log;

public final class ItemRegistry {

  private static final Map<Short, Item> ITEMS = new HashMap<>();

  private static final String AIR_BLOCK = "core:air";

  private static boolean initialized = false;

  private ItemRegistry() {}

  public static void init() {

    if (initialized) {
      Log.warn("[ItemRegistry] Already initialized.");
      return;
    }

    for (BlockType block : BlockRegistry.getAll()) {

      if (block == null) {
        continue;
      }

      String blockName = block.getName();

      if (AIR_BLOCK.equals(blockName)) {
        continue;
      }

      String displayName = formatName(blockName);

      register(new Item(block.getId(), displayName, 64));
    }

    initialized = true;

    Log.info("[ItemRegistry] Registered " + ITEMS.size() + " items.");
  }

  private static void register(Item item) {

    Objects.requireNonNull(item, "Item cannot be null");

    short id = item.getId();

    if (ITEMS.containsKey(id)) {
      Log.warn("[ItemRegistry] Duplicate item id: " + id);
      return;
    }

    ITEMS.put(id, item);
  }

  public static Item getItem(short id) {
    return ITEMS.get(id);
  }

  public static boolean exists(short id) {
    return ITEMS.containsKey(id);
  }

  public static Map<Short, Item> getAll() {
    return Collections.unmodifiableMap(ITEMS);
  }

  private static String formatName(String name) {

    String clean = name.contains(":") ? name.substring(name.indexOf(':') + 1) : name;

    String[] words = clean.split("_");

    StringBuilder sb = new StringBuilder();

    for (String word : words) {

      if (word.isEmpty()) continue;

      sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(' ');
    }

    return sb.toString().trim();
  }
}
