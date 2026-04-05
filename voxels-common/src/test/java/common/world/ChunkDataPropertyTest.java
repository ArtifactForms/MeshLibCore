package common.world;

import org.junit.jupiter.api.Assertions;

import common.game.block.Blocks;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

/**
 * Property-based tests for {@link ChunkData}. * This test ensures that the incremental heightmap
 * update logic is mathematically consistent with a full top-down scan of the chunk data. *
 * Invariants tested: 1. Heightmap(x, z) == max(y) where block(x, y, z) != AIR. 2. If all blocks in
 * a column are AIR, Heightmap(x, z) == 0.
 */
class ChunkDataPropertyTest {

  @Property(tries = 200)
  void heightmapMustAlwaysBeCorrect(@ForAll("blockOperations") java.util.List<BlockOp> ops) {

    ChunkData chunk = new ChunkData(0, 0);

    for (BlockOp op : ops) {
      chunk.setBlockId(op.id, op.x, op.y, op.z);
    }

    // verify invariant
    for (int x = 0; x < ChunkData.WIDTH; x++) {
      for (int z = 0; z < ChunkData.DEPTH; z++) {

        int expected = 0;

        for (int y = ChunkData.HEIGHT - 1; y >= 0; y--) {
          if (chunk.getBlockId(x, y, z) != Blocks.AIR.getId()) {
            expected = y;
            break;
          }
        }

        int actual = chunk.getHeightValue(x, z);

        Assertions.assertEquals(expected, actual);
      }
    }
  }

  @Provide
  Arbitrary<java.util.List<BlockOp>> blockOperations() {

    Arbitrary<Integer> x = Arbitraries.integers().between(0, ChunkData.WIDTH - 1);
    Arbitrary<Integer> y = Arbitraries.integers().between(0, ChunkData.HEIGHT - 1);
    Arbitrary<Integer> z = Arbitraries.integers().between(0, ChunkData.DEPTH - 1);

    Arbitrary<Short> id =
        Arbitraries.of(Blocks.AIR.getId(), Blocks.STONE.getId(), Blocks.DIRT.getId());

    return Combinators.combine(x, y, z, id).as(BlockOp::new).list().ofSize(1000);
  }

  static class BlockOp {

    int x;

    int y;

    int z;

    short id;

    BlockOp(int x, int y, int z, short id) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.id = id;
    }
  }
}
