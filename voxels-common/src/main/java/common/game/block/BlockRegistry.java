package common.game.block;

import java.util.HashMap;
import java.util.Map;

public final class BlockRegistry {

  private static final int MAX_BLOCKS = 4096;

  private static final BlockType[] BY_ID = new BlockType[MAX_BLOCKS];
  private static final Map<String, BlockType> BY_NAME = new HashMap<>();

  private static short nextId = 0;
  private static boolean frozen = false;

  public static BlockType register(String name) {

    if (frozen) {
      throw new IllegalStateException("BlockRegistry is frozen. No more blocks can be registered.");
    }

    short id = nextId++;

    BlockType block = new BlockType(id, name);

    BY_ID[id] = block;
    BY_NAME.put(name, block);

    return block;
  }

  public static BlockType get(short id) {
    if (id < 0 || id >= MAX_BLOCKS) return null;
    return BY_ID[id];
  }

  public static BlockType get(String name) {
    return BY_NAME.get(name);
  }

  /** Returns all registered blocks. Useful for iteration (items, rendering systems, etc). */
  public static Iterable<BlockType> getAll() {
    return () ->
        new java.util.Iterator<>() {

          private int index = 0;

          @Override
          public boolean hasNext() {
            while (index < nextId && BY_ID[index] == null) {
              index++;
            }
            return index < nextId;
          }

          @Override
          public BlockType next() {
            return BY_ID[index++];
          }
        };
  }

  public static void freeze() {
    frozen = true;
  }

  public static boolean isFrozen() {
    return frozen;
  }

  public static int size() {
    return nextId;
  }
}
