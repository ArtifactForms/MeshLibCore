package common.player.interaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import common.game.block.Blocks;
import common.interaction.BlockTarget;
import common.interaction.InteractionTarget;
import common.world.BlockFace;

class BlockTargetTest {

  @Test
  void testConstructorAssignment() {
    // Given values for the targeted block
    int targetX = 10, targetY = 64, targetZ = -5;
    // Given values for the placement position (e.g., one block above)
    int placeX = 10, placeY = 65, placeZ = -5;
    BlockFace face = BlockFace.UP;

    BlockTarget target =
        new BlockTarget(targetX, targetY, targetZ, placeX, placeY, placeZ, face, Blocks.BEDROCK);

    // Verify target coordinates
    assertEquals(targetX, target.x, "Target block X-coordinate is incorrect");
    assertEquals(targetY, target.y, "Target block Y-coordinate is incorrect");
    assertEquals(targetZ, target.z, "Target block Z-coordinate is incorrect");

    // Verify placement coordinates
    assertEquals(placeX, target.placeX, "Placement X-coordinate is incorrect");
    assertEquals(placeY, target.placeY, "Placement Y-coordinate is incorrect");
    assertEquals(placeZ, target.placeZ, "Placement Z-coordinate is incorrect");

    assertEquals(Blocks.BEDROCK, target.type);

    // Verify the face
    assertEquals(BlockFace.UP, target.face, "BlockFace was not stored correctly");
  }

  @Test
  void testInteractionTargetInterface() {
    // Ensure BlockTarget correctly implements the interface (type check)
    BlockTarget target = new BlockTarget(0, 0, 0, 0, 1, 0, BlockFace.UP, Blocks.AIR);

    assertNotNull(target, "Instance should not be null");
    assertTrue(target instanceof InteractionTarget, "BlockTarget must implement InteractionTarget");
  }

  @Test
  void testNegativeCoordinates() {
    // Test for coordinates in the negative range (crucial for voxel worlds)
    BlockTarget target = new BlockTarget(-16, 10, -1, -17, 10, -1, BlockFace.WEST, Blocks.AIR);

    assertEquals(-16, target.x, "Negative target X should be allowed");
    assertEquals(-17, target.placeX, "Negative placement X should be allowed");
    assertEquals(BlockFace.WEST, target.face);
  }
}
