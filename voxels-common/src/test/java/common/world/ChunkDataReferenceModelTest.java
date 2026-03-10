package common.world;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

import common.game.block.Blocks;

public class ChunkDataReferenceModelTest {

  @Test
  void referenceModelTest() {

    ChunkData fastChunk = new ChunkData(0, 0);
    ReferenceChunk refChunk = new ReferenceChunk();

    Random random = new Random();

    short[] ids = {Blocks.AIR.getId(), Blocks.STONE.getId(), Blocks.DIRT.getId()};

    for (int i = 0; i < 200000; i++) {

      int x = random.nextInt(ChunkData.WIDTH);
      int y = random.nextInt(ChunkData.HEIGHT);
      int z = random.nextInt(ChunkData.DEPTH);

      short id = ids[random.nextInt(ids.length)];

      fastChunk.setBlockId(id, x, y, z);
      refChunk.set(id, x, y, z);

      // check block correctness
      assertEquals(refChunk.get(x, y, z), fastChunk.getBlockId(x, y, z));

      // check heightmap correctness
      assertEquals(refChunk.height(x, z), fastChunk.getHeightValue(x, z));
    }

    // full world verification
    for (int x = 0; x < ChunkData.WIDTH; x++) {
      for (int z = 0; z < ChunkData.DEPTH; z++) {

        assertEquals(refChunk.height(x, z), fastChunk.getHeightValue(x, z));

        for (int y = 0; y < ChunkData.HEIGHT; y++) {

          assertEquals(refChunk.get(x, y, z), fastChunk.getBlockId(x, y, z));
        }
      }
    }
  }
}
