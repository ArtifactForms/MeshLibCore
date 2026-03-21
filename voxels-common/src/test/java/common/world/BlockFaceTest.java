package common.world;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BlockFaceTest {

  @Test
  void testOpposites() {
    assertEquals(BlockFace.DOWN, BlockFace.UP.opposite());
    assertEquals(BlockFace.UP, BlockFace.DOWN.opposite());

    assertEquals(BlockFace.SOUTH, BlockFace.NORTH.opposite());
    assertEquals(BlockFace.NORTH, BlockFace.SOUTH.opposite());

    assertEquals(BlockFace.EAST, BlockFace.WEST.opposite());
    assertEquals(BlockFace.WEST, BlockFace.EAST.opposite());

    assertEquals(BlockFace.NONE, BlockFace.NONE.opposite());
  }

  @Test
  void testDoubleOppositeReturnsOriginal() {
    for (BlockFace face : BlockFace.values()) {
      assertEquals(face, face.opposite().opposite(), "Double opposite failed for: " + face);
    }
  }

  @Test
  void testVectorOpposites() {
    for (BlockFace face : BlockFace.values()) {
      BlockFace opposite = face.opposite();

      assertEquals(-face.x, opposite.x, "X mismatch for " + face);
      assertEquals(-face.y, opposite.y, "Y mismatch for " + face);
      assertEquals(-face.z, opposite.z, "Z mismatch for " + face);
    }
  }

  @Test
  void testNoneIsZeroVector() {
    assertEquals(0, BlockFace.NONE.x);
    assertEquals(0, BlockFace.NONE.y);
    assertEquals(0, BlockFace.NONE.z);
  }
}
