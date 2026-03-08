import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import common.game.ItemStack;
import common.world.BlockType;

public class BlockTypeTest {

  @Test
  void testStone() {
    int id = 1;
    assertEquals(id, BlockType.STONE.getId());
  }

  @Test
  void testItemStack() {
    ItemStack itemStack = new ItemStack(BlockType.STONE.getId(), 0);
    assertEquals(1, itemStack.getItemId());
  }
}
