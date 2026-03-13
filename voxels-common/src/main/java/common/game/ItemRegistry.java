package common.game;

import java.util.HashMap;
import java.util.Map;

import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.logging.Log;

public class ItemRegistry {

  private static final Map<Short, Item> ITEMS = new HashMap<>();

  public static void init() {

    for (BlockType block : BlockRegistry.getAll()) {

      if (block == null) continue;

      if (block.getName().equals("core:air")) continue;

      String displayName = formatName(block.getName());

      register(new Item(block.getId(), displayName, 64));
    }

    Log.info("[ItemRegistry] Registered " + ITEMS.size() + " items.");
  }

  private static void register(Item item) {
    ITEMS.put((short) item.getId(), item);
  }

  public static Item getItem(short id) {
    return ITEMS.get(id);
  }

  private static String formatName(String name) {

    String clean = name.contains(":") ? name.split(":")[1] : name;

    String[] words = clean.split("_");

    StringBuilder sb = new StringBuilder();

    for (String word : words) {

      if (word.isEmpty()) continue;

      sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
    }

    return sb.toString().trim();
  }
}
