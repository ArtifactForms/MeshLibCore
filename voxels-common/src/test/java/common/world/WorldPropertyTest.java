package common.world;

import org.junit.jupiter.api.Assertions;

import common.game.block.Blocks;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

class WorldPropertyTest {

  /**
   * Property: A block set at any world coordinate must be retrievable from the corresponding
   * ChunkData at the correct local coordinate.
   */
  @Property(tries = 500)
  void blockPlacementMustMapToCorrectChunk(@ForAll("worldCoords") WorldCoord coord) {
    World world = new World();

    // Calculate expected chunk indices
    int expectedCx = Math.floorDiv(coord.x, ChunkData.WIDTH);
    int expectedCz = Math.floorDiv(coord.z, ChunkData.DEPTH);

    // Calculate expected local coordinates
    int expectedLx = Math.floorMod(coord.x, ChunkData.WIDTH);
    int expectedLz = Math.floorMod(coord.z, ChunkData.DEPTH);

    // Setup: Add the chunk if it doesn't exist
    ChunkData chunk = new ChunkData(expectedCx, expectedCz);
    world.addChunk(chunk);

    // Action: Set block via world API
    short testId = (short) 1; // Stone
    world.setBlock(coord.x, coord.y, coord.z, testId);

    // Verification 1: Can we get it back via World API?
    Assertions.assertEquals(
        testId,
        world.getBlock(coord.x, coord.y, coord.z).getId(),
        "World API should return the set block");

    // Verification 2: Is it physically in the correct local position in the chunk?
    Assertions.assertEquals(
        testId,
        chunk.getBlockId(expectedLx, coord.y, expectedLz),
        String.format(
            "Block at world(%d, %d) must be at chunk[%d, %d] local(%d, %d)",
            coord.x, coord.z, expectedCx, expectedCz, expectedLx, expectedLz));
  }

  /**
   * Property: Accessing world coordinates where no chunk exists must safely return AIR and not
   * crash.
   */
  @Property
  void accessingEmptyWorldReturnsAir(@ForAll("worldCoords") WorldCoord coord) {
    World emptyWorld = new World();
    Assertions.assertEquals(Blocks.AIR, emptyWorld.getBlock(coord.x, coord.y, coord.z));
    Assertions.assertFalse(emptyWorld.isSolid(coord.x, coord.y, coord.z));
  }

  @Provide
  Arbitrary<WorldCoord> worldCoords() {
    // We test a large range of coordinates including very negative and very positive
    Arbitrary<Integer> xz = Arbitraries.integers().between(-1000000, 1000000);
    Arbitrary<Integer> y = Arbitraries.integers().between(0, ChunkData.HEIGHT - 1);

    return Combinators.combine(xz, y, xz).as(WorldCoord::new);
  }

  static class WorldCoord {

    int x;

    int y;

    int z;

    WorldCoord(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }
}
