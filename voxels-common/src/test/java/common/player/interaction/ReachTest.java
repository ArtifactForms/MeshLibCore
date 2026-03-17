package common.player.interaction;

import static org.junit.jupiter.api.Assertions.*;

import math.Vector3f;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReachTest {

  private static final float EPSILON = 1e-5f;

  @Test
  @DisplayName("Distance should be zero when eye is inside block")
  void testDistanceInsideBlock() {
    Vector3f pos = new Vector3f(0f, 0f, 0f);
    float eyeHeight = 0.5f;

    float dist = Reach.distanceToBlock(pos, eyeHeight, 0, 0, 0);

    assertEquals(0f, dist, EPSILON);
  }

  @Test
  @DisplayName("Distance should be zero when eye is exactly on block surface")
  void testDistanceOnSurface() {
    Vector3f pos = new Vector3f(0.5f, 0f, 0f); // exactly at edge
    float eyeHeight = 0f;

    float dist = Reach.distanceToBlock(pos, eyeHeight, 0, 0, 0);

    assertEquals(0f, dist, EPSILON);
  }

  @Test
  @DisplayName("Distance straight along X axis")
  void testDistanceAlongX() {
    Vector3f pos = new Vector3f(2f, 0f, 0f);
    float eyeHeight = 0f;

    float dist = Reach.distanceToBlock(pos, eyeHeight, 0, 0, 0);

    // closest point is at x = 0.5 → distance = 1.5
    assertEquals(1.5f, dist, EPSILON);
  }

  @Test
  @DisplayName("Distance diagonal (3D)")
  void testDistanceDiagonal() {
    Vector3f pos = new Vector3f(2f, 2f, 2f);
    float eyeHeight = 0f;

    float dist = Reach.distanceToBlock(pos, eyeHeight, 0, 0, 0);

    // closest point = (0.5, 0.5, 0.5)
    float expected = (float) Math.sqrt(1.5f * 1.5f * 3);

    assertEquals(expected, dist, EPSILON);
  }

  @Test
  @DisplayName("Eye height should affect distance")
  void testEyeHeightEffect() {
    Vector3f pos = new Vector3f(0f, 0f, 0f);

    float distLowEye = Reach.distanceToBlock(pos, 0f, 0, 2, 0);
    float distHighEye = Reach.distanceToBlock(pos, 1.5f, 0, 2, 0);

    assertTrue(distHighEye < distLowEye);
  }

  @Test
  @DisplayName("Within reach when exactly at max distance")
  void testWithinReachExactBoundary() {
    Vector3f pos = new Vector3f(2f, 0f, 0f);
    float eyeHeight = 0f;

    float dist = Reach.distanceToBlock(pos, eyeHeight, 0, 0, 0);

    assertTrue(Reach.isWithinReach(pos, eyeHeight, 0, 0, 0, dist));
  }

  @Test
  @DisplayName("Outside reach when slightly above max distance")
  void testOutsideReach() {
    Vector3f pos = new Vector3f(2f, 0f, 0f);
    float eyeHeight = 0f;

    float dist = Reach.distanceToBlock(pos, eyeHeight, 0, 0, 0);

    assertFalse(Reach.isWithinReach(pos, eyeHeight, 0, 0, 0, dist - 0.01f));
  }

  @Test
  @DisplayName("Within reach when slightly below max distance")
  void testWithinReach() {
    Vector3f pos = new Vector3f(2f, 0f, 0f);
    float eyeHeight = 0f;

    float dist = Reach.distanceToBlock(pos, eyeHeight, 0, 0, 0);

    assertTrue(Reach.isWithinReach(pos, eyeHeight, 0, 0, 0, dist + 0.01f));
  }

  @Test
  @DisplayName("Symmetry: distance should be same from opposite sides")
  void testSymmetry() {
    float eyeHeight = 0f;

    float d1 = Reach.distanceToBlock(new Vector3f(2f, 0f, 0f), eyeHeight, 0, 0, 0);
    float d2 = Reach.distanceToBlock(new Vector3f(-2f, 0f, 0f), eyeHeight, 0, 0, 0);

    assertEquals(d1, d2, EPSILON);
  }

  @Test
  @DisplayName("Distance should be zero when inside block volume (not just center)")
  void testInsideVolume() {
    Vector3f pos = new Vector3f(0.2f, 0.2f, 0.2f);
    float eyeHeight = 0f;

    float dist = Reach.distanceToBlock(pos, eyeHeight, 0, 0, 0);

    assertEquals(0f, dist, EPSILON);
  }

  @Test
  @DisplayName("Distance to block at negative coordinates (Grid Symmetry)")
  void testNegativeCoordinates() {
    // Player is at x = -2.0
    Vector3f pos = new Vector3f(-2f, 0f, 0f);
    float eyeHeight = 0f;

    // Block at (-1, 0, 0)
    // In a standard 1x1x1 grid, the block's X-bounds are [-1.5, -0.5]
    // The distance from -2.0 to the closest edge (-1.5) should be 0.5
    float dist = Reach.distanceToBlock(pos, eyeHeight, -1, 0, 0);

    assertEquals(
        0.5f, dist, EPSILON, "Should calculate distance correctly in the negative quadrant");
  }

  @Test
  @DisplayName("Eye height moving the viewpoint out of reach range vertically")
  void testExtremeEyeHeight() {
    // Player feet are at (0, 0, 0)
    Vector3f pos = new Vector3f(0f, 0f, 0f);

    // Eye height is 2.0, so the actual eye position is at Y = 2.0
    // Target block is at (0, 0, 0), with Y-bounds [-0.5, 0.5]
    // The vertical distance from Y=2.0 to the top edge Y=0.5 is 1.5
    float dist = Reach.distanceToBlock(pos, 2.0f, 0, 0, 0);

    assertEquals(
        1.5f, dist, EPSILON, "Distance should account for vertical offset from eye height");
  }

  @Test
  @DisplayName("Distance should be relative to the eye position, not the foot position")
  void testEyePositionShift() {
    // Player feet at (0, 0, 0), but eye is at y=1.6
    Vector3f playerPos = new Vector3f(0f, 0f, 0f);
    float eyeHeight = 1.6f;

    // Target block is at (0, 2, 0) -> Y-bounds [1.5, 2.5]
    // Eye is at 1.6, Block starts at 1.5. Eye is INSIDE the Y-range of the block.
    // If x and z are also within range, distance should be 0.
    float dist = Reach.distanceToBlock(playerPos, eyeHeight, 0, 2, 0);

    assertEquals(0f, dist, EPSILON, "Eye is vertically within the block's bounds (1.5 to 2.5)");
  }
}
