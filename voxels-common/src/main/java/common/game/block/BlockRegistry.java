package common.game.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class BlockRegistry {

  private static int maxId = 0;
  private static final int MAX_BLOCKS = 4096;

  private static final BlockType[] BY_ID = new BlockType[MAX_BLOCKS];
  private static final Map<String, BlockType> BY_NAME = new HashMap<>();

  private static boolean frozen = false;

  public static BlockType register(short id, String name) {
    if (frozen) {
      throw new IllegalStateException("BlockRegistry is frozen.");
    }

    // Safety check: Is ID already taken?
    if (BY_ID[id] != null) {
      throw new IllegalStateException(
          "Block ID " + id + " is already occupied by " + BY_ID[id].getName());
    }

    BlockType block = new BlockType(id, name);
    BY_ID[id] = block;
    BY_NAME.put(name, block);

    // Track the highest ID for iteration boundaries
    if (id > maxId) {
      maxId = id;
    }

    return block;
  }

  public static BlockType get(short id) {
    if (id < 0 || id >= MAX_BLOCKS) return null;
    return BY_ID[id];
  }

  /**
   * Retrieves the {@link BlockType} for the given ID without performing range or null checks.
   *
   * <p><b>Performance Note:</b> This is the fastest way to access block data, intended for use in
   * performance-critical loops (e.g., mesh generation). <b>Warning:</b> Passing an ID that is
   * negative or greater than {@link #MAX_BLOCKS} will result in an {@link
   * ArrayIndexOutOfBoundsException}.
   *
   * @param id the short ID of the block to retrieve.
   * @return the BlockType associated with the ID.
   * @throws ArrayIndexOutOfBoundsException if the ID is out of range.
   */
  public static BlockType getTypeUnsafe(short id) {
    return BY_ID[id];
  }

  public static BlockType get(String name) {
    return BY_NAME.get(name);
  }

  /** * Returns an iterable of all non-null blocks. Handles potential gaps in the ID array. */
  public static Iterable<BlockType> getAll() {
    return () ->
        new Iterator<>() {
          private int index = 0;

          @Override
          public boolean hasNext() {
            // Skip null entries until we find a block or reach the end
            while (index <= maxId && BY_ID[index] == null) {
              index++;
            }
            return index <= maxId;
          }

          @Override
          public BlockType next() {
            if (!hasNext()) {
              throw new NoSuchElementException();
            }
            return BY_ID[index++];
          }
        };
  }

  public static void freeze() {
    frozen = true;
  }

  public static int size() {
    // Returns the actual count of registered blocks
    return BY_NAME.size();
  }

  public static int getMaxId() {
    return maxId;
  }
}
