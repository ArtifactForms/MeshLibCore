package common.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import common.game.block.BlockType;
import common.game.block.Blocks;
import math.Bounds;

class ChunkDataTest {

  private ChunkData chunk;

  @BeforeEach
  void setUp() {
    // Initialize a test chunk at coordinates (1, 1)
    chunk = new ChunkData(1, 1);
  }

  @Test
  void testInitialState() {
    assertEquals(1, chunk.getChunkX());
    assertEquals(1, chunk.getChunkZ());

    // By default, every block should be AIR and heightmap should be 0
    assertEquals(Blocks.AIR.getId(), chunk.getBlockId(0, 0, 0));
    assertEquals(0, chunk.getHeightValue(0, 0));
  }

  @Test
  void testSetAndGetBlockId() {
    short stoneId = 1; // Assuming 1 represents Stone
    chunk.setBlockId(stoneId, 5, 10, 5);

    assertEquals(stoneId, chunk.getBlockId(5, 10, 5));

    // Test Out-of-Bounds safety (should return AIR)
    assertEquals(Blocks.AIR.getId(), chunk.getBlockId(-1, 0, 0));
    assertEquals(Blocks.AIR.getId(), chunk.getBlockId(16, 0, 0));
  }

  @Test
  void testHeightMapUpdate_Placement() {
    short dirtId = 2;

    // Place a block at mid-height
    chunk.setBlockId(dirtId, 0, 10, 0);
    assertEquals(10, chunk.getHeightValue(0, 0), "Heightmap should increase to 10");

    // Place a higher block
    chunk.setBlockId(dirtId, 0, 20, 0);
    assertEquals(20, chunk.getHeightValue(0, 0), "Heightmap should increase to 20");

    // Place a lower block (should NOT change the heightmap)
    chunk.setBlockId(dirtId, 0, 5, 0);
    assertEquals(20, chunk.getHeightValue(0, 0), "Heightmap should remain at 20");
  }

  @Test
  void testHeightMapUpdate_Removal() {
    short dirtId = 2;
    chunk.setBlockId(dirtId, 0, 10, 0);
    chunk.setBlockId(dirtId, 0, 20, 0);

    // Remove the highest block (set to AIR)
    chunk.setBlockId(Blocks.AIR.getId(), 0, 20, 0);

    assertEquals(
        10,
        chunk.getHeightValue(0, 0),
        "Heightmap should fall back to the next highest block (10)");
  }

  @ParameterizedTest
  @CsvSource({
    "0, 0, 0, true", // Min corner
    "15, 383, 15, true", // Max corner
    "-1, 0, 0, false", // X underflow
    "16, 0, 0, false", // X overflow
    "0, -1, 0, false", // Y underflow
    "0, 384, 0, false", // Y overflow
    "0, 0, 16, false" // Z overflow
  })
  void testIsInside(int x, int y, int z, boolean expected) {
    assertEquals(expected, chunk.isInside(x, y, z));
  }

  @Test
  void testClear() {
    chunk.setBlockId((short) 1, 0, 0, 0);
    chunk.clear();

    assertEquals(Blocks.AIR.getId(), chunk.getBlockId(0, 0, 0));
    assertEquals(0, chunk.getHeightValue(0, 0));
  }

  @Test
  void testIsSolid() {
    short stoneId = 1;
    chunk.setBlockId(stoneId, 2, 2, 2);

    assertTrue(chunk.isSolid(2, 2, 2));
    assertFalse(chunk.isSolid(0, 0, 0));
    assertFalse(chunk.isSolid(-1, -1, -1)); // Out of bounds
  }

  @Test
  void testIndexCalculation() {
    // Since getIndex is protected, we test its logic indirectly via raw data access.
    // An incorrect indexing algorithm would write data to the wrong array positions.
    short id = 55;
    chunk.setBlockId(id, 1, 0, 0); // x=1
    chunk.setBlockId((short) 10, 0, 1, 0); // y=1

    // Index 1 corresponds to x=1, y=0, z=0
    assertEquals(id, chunk.getRawBlockData()[1]);

    // Index Formula: x + WIDTH * (y + HEIGHT * z)
    // x=0, y=1, z=0 -> 0 + 16 * (1 + 384 * 0) = 16
    assertEquals(10, chunk.getRawBlockData()[16]);
  }

  @Test
  void testHeightMapRemoveLastBlock() {
    short dirtId = 2;

    chunk.setBlockId(dirtId, 0, 10, 0);

    chunk.setBlockId(Blocks.AIR.getId(), 0, 10, 0);

    assertEquals(0, chunk.getHeightValue(0, 0));
  }

  @Test
  void testSettingSameBlockTwice() {
    short id = 3;

    chunk.setBlockId(id, 1, 5, 1);
    chunk.setBlockId(id, 1, 5, 1);

    assertEquals(id, chunk.getBlockId(1, 5, 1));
  }

  @Test
  void testSetBlockAt() {
    chunk.setBlockAt(Blocks.STONE, 2, 3, 4);

    assertEquals(Blocks.STONE.getId(), chunk.getBlockId(2, 3, 4));
  }

  @Test
  void testGetBlock() {
    chunk.setBlockAt(Blocks.STONE, 1, 2, 3);

    assertEquals(Blocks.STONE, chunk.getBlock(1, 2, 3));
  }

  @Test
  void testChunkBoundsCalculated() {

    Bounds bounds = chunk.getChunkBounds();

    assertEquals(chunk.getChunkX() * ChunkData.WIDTH, bounds.getMin().x);
    assertEquals(chunk.getChunkZ() * ChunkData.DEPTH, bounds.getMin().z);

    assertEquals(bounds.getMin().x + ChunkData.WIDTH, bounds.getMax().x);
    assertEquals(bounds.getMin().z + ChunkData.DEPTH, bounds.getMax().z);
    assertEquals(ChunkData.HEIGHT, bounds.getMax().y);
  }

  @Test
  void testChunkBoundsNumeric() {

    Bounds bounds = chunk.getChunkBounds();

    assertEquals(16, bounds.getMin().x);
    assertEquals(0, bounds.getMin().y);
    assertEquals(16, bounds.getMin().z);

    assertEquals(32, bounds.getMax().x);
    assertEquals(384, bounds.getMax().y);
    assertEquals(32, bounds.getMax().z);
  }

  @Test
  void testSetBlockData() {

    short[] data = new short[ChunkData.WIDTH * ChunkData.HEIGHT * ChunkData.DEPTH];
    data[5] = 99;

    chunk.setBlockData(data);

    assertEquals(99, chunk.getRawBlockData()[5]);
  }

  @Test
  void testGetBlockId_OutOfBoundsY() {
    assertEquals(Blocks.AIR.getId(), chunk.getBlockId(0, -1, 0));
    assertEquals(Blocks.AIR.getId(), chunk.getBlockId(0, 384, 0));
  }

  @Test
  void testHeightMapIndependentColumns() {

    short id = 5;

    chunk.setBlockId(id, 0, 10, 0);
    chunk.setBlockId(id, 1, 20, 0);

    assertEquals(10, chunk.getHeightValue(0, 0));
    assertEquals(20, chunk.getHeightValue(1, 0));
  }

  @Test
  void testHeightMapConsistency() {

    short stone = Blocks.STONE.getId();

    chunk.setBlockId(stone, 0, 5, 0);
    chunk.setBlockId(stone, 0, 20, 0);
    chunk.setBlockId(stone, 0, 10, 0);

    int expected = 20;
    int height = chunk.getHeightValue(0, 0);

    assertEquals(expected, height);

    // remove top block
    chunk.setBlockId(Blocks.AIR.getId(), 0, 20, 0);

    assertEquals(10, chunk.getHeightValue(0, 0));
  }

  @Test
  void testRandomBlockStress() {

    java.util.Random random = new java.util.Random();

    for (int i = 0; i < 10000; i++) {

      int x = random.nextInt(ChunkData.WIDTH);
      int y = random.nextInt(ChunkData.HEIGHT);
      int z = random.nextInt(ChunkData.DEPTH);

      short id = (short) random.nextInt(5);

      chunk.setBlockId(id, x, y, z);

      assertEquals(id, chunk.getBlockId(x, y, z));
    }
  }

  @Test
  void testFullColumnHeightMap() {

    short stone = Blocks.STONE.getId();

    for (int y = 0; y < ChunkData.HEIGHT; y++) {
      chunk.setBlockId(stone, 0, y, 0);
    }

    assertEquals(ChunkData.HEIGHT - 1, chunk.getHeightValue(0, 0));
  }

  @Test
  void testColumnRemoval() {

    short stone = Blocks.STONE.getId();

    chunk.setBlockId(stone, 0, 10, 0);
    chunk.setBlockId(stone, 0, 20, 0);

    chunk.setBlockId(Blocks.AIR.getId(), 0, 20, 0);
    assertEquals(10, chunk.getHeightValue(0, 0));

    chunk.setBlockId(Blocks.AIR.getId(), 0, 10, 0);
    assertEquals(0, chunk.getHeightValue(0, 0));
  }

  @Test
  void testChunkBorderBlockAccess() {

    short stone = Blocks.STONE.getId();

    int x = ChunkData.WIDTH - 1;
    int y = ChunkData.HEIGHT - 1;
    int z = ChunkData.DEPTH - 1;

    chunk.setBlockId(stone, x, y, z);

    assertEquals(stone, chunk.getBlockId(x, y, z));
  }

  @Test
  void testHeightMapAtBorder() {

    short stone = Blocks.STONE.getId();

    int x = ChunkData.WIDTH - 1;
    int z = ChunkData.DEPTH - 1;

    chunk.setBlockId(stone, x, 50, z);

    assertEquals(50, chunk.getHeightValue(x, z));
  }

  @Test
  void testChunkCorners() {

    short id = Blocks.STONE.getId();

    chunk.setBlockId(id, 0, 0, 0);
    chunk.setBlockId(id, 15, 0, 0);
    chunk.setBlockId(id, 0, 0, 15);
    chunk.setBlockId(id, 15, 0, 15);

    assertEquals(id, chunk.getBlockId(0, 0, 0));
    assertEquals(id, chunk.getBlockId(15, 0, 0));
    assertEquals(id, chunk.getBlockId(0, 0, 15));
    assertEquals(id, chunk.getBlockId(15, 0, 15));
  }

  @Test
  void testBorderOutOfBounds() {

    assertEquals(Blocks.AIR.getId(), chunk.getBlockId(16, 0, 0));
    assertEquals(Blocks.AIR.getId(), chunk.getBlockId(0, 0, 16));
    assertEquals(Blocks.AIR.getId(), chunk.getBlockId(-1, 0, 0));
    assertEquals(Blocks.AIR.getId(), chunk.getBlockId(0, 0, -1));
  }

  @Test
  void testAllChunkBorders() {

    short stone = Blocks.STONE.getId();

    for (int x = 0; x < ChunkData.WIDTH; x++) {
      chunk.setBlockId(stone, x, 10, 0);
      chunk.setBlockId(stone, x, 10, ChunkData.DEPTH - 1);
    }

    for (int z = 0; z < ChunkData.DEPTH; z++) {
      chunk.setBlockId(stone, 0, 10, z);
      chunk.setBlockId(stone, ChunkData.WIDTH - 1, 10, z);
    }

    assertEquals(stone, chunk.getBlockId(0, 10, 0));
    assertEquals(stone, chunk.getBlockId(15, 10, 15));
  }

  @Test
  void brutalChunkStressTest() {

    java.util.Random random = new java.util.Random();

    short stone = Blocks.STONE.getId();
    short dirt = Blocks.DIRT.getId();

    for (int i = 0; i < 20000; i++) {

      int x = random.nextInt(ChunkData.WIDTH);
      int y = random.nextInt(ChunkData.HEIGHT);
      int z = random.nextInt(ChunkData.DEPTH);

      short id;

      int r = random.nextInt(3);

      if (r == 0) id = Blocks.AIR.getId();
      else if (r == 1) id = stone;
      else id = dirt;

      chunk.setBlockId(id, x, y, z);

      // verify block stored correctly
      assertEquals(id, chunk.getBlockId(x, y, z));

      // verify heightmap correctness
      int height = chunk.getHeightValue(x, z);

      for (int yy = ChunkData.HEIGHT - 1; yy >= 0; yy--) {

        if (chunk.getBlockId(x, yy, z) != Blocks.AIR.getId()) {
          assertEquals(yy, height);
          break;
        }

        if (yy == 0) {
          assertEquals(0, height);
        }
      }
    }
  }

  @Test
  void testHeightMapWholeChunk() {

    short stone = Blocks.STONE.getId();

    for (int x = 0; x < ChunkData.WIDTH; x++) {
      for (int z = 0; z < ChunkData.DEPTH; z++) {

        int height = (x + z) % 50;

        chunk.setBlockId(stone, x, height, z);

        assertEquals(height, chunk.getHeightValue(x, z));
      }
    }
  }

  @Test
  void testClearResetsEverything() {

    short stone = Blocks.STONE.getId();

    for (int x = 0; x < ChunkData.WIDTH; x++)
      for (int z = 0; z < ChunkData.DEPTH; z++) chunk.setBlockId(stone, 10, x, z);

    chunk.clear();

    for (int x = 0; x < ChunkData.WIDTH; x++)
      for (int z = 0; z < ChunkData.DEPTH; z++) {
        assertEquals(Blocks.AIR.getId(), chunk.getBlockId(x, 0, z));
        assertEquals(0, chunk.getHeightValue(x, z));
      }
  }

  @Test
  void testGetBlockReturnsAir() {

    BlockType block = chunk.getBlock(5, 5, 5);

    assertEquals(Blocks.AIR, block);
  }

  @Test
  void testBlockAccessPerformance() {

    long start = System.nanoTime();

    for (int i = 0; i < 1_000_000; i++) {
      chunk.getBlockId(1, 1, 1);
    }

    long end = System.nanoTime();

    assertTrue(end - start < 100_000_000); // <100ms
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "1, 1", "-1, -1", "-2, -3", "5, -4"})
  void testChunkBoundsParameterized(int chunkX, int chunkZ) {

    ChunkData chunk = new ChunkData(chunkX, chunkZ);

    Bounds bounds = chunk.getChunkBounds();

    int expectedMinX = chunkX * ChunkData.WIDTH;
    int expectedMinZ = chunkZ * ChunkData.DEPTH;

    int expectedMaxX = expectedMinX + ChunkData.WIDTH;
    int expectedMaxZ = expectedMinZ + ChunkData.DEPTH;

    assertEquals(expectedMinX, bounds.getMin().x);
    assertEquals(expectedMinZ, bounds.getMin().z);

    assertEquals(expectedMaxX, bounds.getMax().x);
    assertEquals(expectedMaxZ, bounds.getMax().z);
    assertEquals(ChunkData.HEIGHT, bounds.getMax().y);
  }

  @Test
  void testRawDataLength() {
    int expectedLength = ChunkData.WIDTH * ChunkData.DEPTH * ChunkData.HEIGHT;
    assertEquals(expectedLength, chunk.getRawBlockData().length);
  }

  @Test
  void testRecalculateHeightMapAfterManualSet() {
    short stone = Blocks.STONE.getId();

    // Manually inject data without using setBlockId (bypassing auto-update)
    short[] rawData = new short[ChunkData.WIDTH * ChunkData.HEIGHT * ChunkData.DEPTH];
    // Set a block at y=50
    int index = 0 + ChunkData.WIDTH * (50 + ChunkData.HEIGHT * 0);
    rawData[index] = stone;

    chunk.setBlockData(rawData);

    // Heightmap is still 0 because we bypassed setBlockId
    assertEquals(0, chunk.getHeightValue(0, 0));

    // Now trigger manual recalculation
    chunk.recalculateHeightMap();

    assertEquals(
        50, chunk.getHeightValue(0, 0), "Heightmap should be 50 after manual recalculation");
  }

  @Test
  void testHeightMapRemovalWithGapBelow() {
    short stone = Blocks.STONE.getId();

    // Create a stack with a gap: Stone at Y=5 and Y=10
    chunk.setBlockId(stone, 0, 5, 0);
    chunk.setBlockId(stone, 0, 10, 0);
    assertEquals(10, chunk.getHeightValue(0, 0));

    // Remove the lower block (Y=5) - Heightmap must stay at 10
    chunk.setBlockId(Blocks.AIR.getId(), 0, 5, 0);
    assertEquals(
        10,
        chunk.getHeightValue(0, 0),
        "Heightmap should not change when a lower block is removed");

    // Remove the top block (Y=10) - Heightmap must drop to 0 (because Y=5 is also gone)
    chunk.setBlockId(Blocks.AIR.getId(), 0, 10, 0);
    assertEquals(
        0,
        chunk.getHeightValue(0, 0),
        "Heightmap should drop to 0 if no blocks are left in column");
  }

  @Test
  void testHeightMapDropToIntermediateBlock() {
    short stone = Blocks.STONE.getId();

    // Stack: Y=5, Y=10
    chunk.setBlockId(stone, 0, 5, 0);
    chunk.setBlockId(stone, 0, 10, 0);

    // Remove only the top one
    chunk.setBlockId(Blocks.AIR.getId(), 0, 10, 0);

    assertEquals(
        5, chunk.getHeightValue(0, 0), "Heightmap should drop to the next available block at Y=5");
  }

  @Test
  void testSetBlockDataInvalidLength() {

    short[] invalid = new short[10];

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          chunk.setBlockData(invalid);
        });
  }

  @Test
  void testSetBlockDataNull() {
    assertThrows(
        NullPointerException.class,
        () -> {
          chunk.setBlockData(null);
        });
  }

  @Test
  void testHeightValueOutOfBounds() {
    assertEquals(0, chunk.getHeightValue(-1, 0));
    assertEquals(0, chunk.getHeightValue(16, 0));
    assertEquals(0, chunk.getHeightValue(0, 16));
  }

  @Test
  void testSetHeightValueOutOfBounds() {
    // This should just do nothing and not crash the program
    chunk.setHeightValue(100, 16, 0);
    assertEquals(0, chunk.getHeightValue(16, 0));
  }

  @Test
  void testSetBlockOutOfBoundsDoesNothing() {

    chunk.setBlockId((short) 5, -1, 0, 0);
    chunk.setBlockId((short) 5, 16, 0, 0);

    assertEquals(Blocks.AIR.getId(), chunk.getBlockId(0, 0, 0));
  }

  @Test
  void testReplaceBlockSameHeight() {

    short stone = Blocks.STONE.getId();
    short dirt = Blocks.DIRT.getId();

    chunk.setBlockId(stone, 0, 10, 0);
    chunk.setBlockId(dirt, 0, 10, 0);

    assertEquals(10, chunk.getHeightValue(0, 0));
  }

  @Test
  void testRemoveAboveHeightMapDoesNothing() {

    chunk.setBlockId(Blocks.STONE.getId(), 0, 10, 0);

    chunk.setBlockId(Blocks.AIR.getId(), 0, 20, 0);

    assertEquals(10, chunk.getHeightValue(0, 0));
  }

  @Test
  void testNegativeChunkCoordinateBounds() {

    ChunkData chunk = new ChunkData(-1, -2);

    Bounds bounds = chunk.getChunkBounds();

    assertEquals(-16, bounds.getMin().x);
    assertEquals(-32, bounds.getMin().z);
  }

  @Test
  void testChunkCoordinates() {
    assertEquals(1, chunk.getChunkX());
    assertEquals(1, chunk.getChunkZ());
  }

  @Test
  void heightMapInvariantTest() {

    short stone = Blocks.STONE.getId();

    for (int x = 0; x < ChunkData.WIDTH; x++) {
      for (int z = 0; z < ChunkData.DEPTH; z++) {

        // random stack
        for (int y = 0; y < ChunkData.HEIGHT; y++) {

          if (Math.random() < 0.3) {
            chunk.setBlockId(stone, x, y, z);
          }
        }

        // calculate true height
        int trueHeight = 0;

        for (int y = ChunkData.HEIGHT - 1; y >= 0; y--) {
          if (chunk.getBlockId(x, y, z) != Blocks.AIR.getId()) {
            trueHeight = y;
            break;
          }
        }

        assertEquals(trueHeight, chunk.getHeightValue(x, z));
      }
    }
  }

  @Test
  void massiveRandomMutationTest() {

    Random random = new Random();

    short stone = Blocks.STONE.getId();
    short dirt = Blocks.DIRT.getId();

    for (int i = 0; i < 100000; i++) {

      int x = random.nextInt(ChunkData.WIDTH);
      int y = random.nextInt(ChunkData.HEIGHT);
      int z = random.nextInt(ChunkData.DEPTH);

      short id;

      int r = random.nextInt(3);

      if (r == 0) id = Blocks.AIR.getId();
      else if (r == 1) id = stone;
      else id = dirt;

      chunk.setBlockId(id, x, y, z);

      assertEquals(id, chunk.getBlockId(x, y, z));
    }

    // verify entire heightmap afterwards
    for (int x = 0; x < ChunkData.WIDTH; x++) {
      for (int z = 0; z < ChunkData.DEPTH; z++) {

        int trueHeight = 0;

        for (int y = ChunkData.HEIGHT - 1; y >= 0; y--) {
          if (chunk.getBlockId(x, y, z) != Blocks.AIR.getId()) {
            trueHeight = y;
            break;
          }
        }

        assertEquals(trueHeight, chunk.getHeightValue(x, z));
      }
    }
  }

  @Test
  void memoryLayoutIntegrityTest() {

    short counter = 1;

    for (int x = 0; x < ChunkData.WIDTH; x++) {
      for (int y = 0; y < ChunkData.HEIGHT; y++) {
        for (int z = 0; z < ChunkData.DEPTH; z++) {

          chunk.setBlockId(counter, x, y, z);

          counter++;
        }
      }
    }

    counter = 1;

    for (int x = 0; x < ChunkData.WIDTH; x++) {
      for (int y = 0; y < ChunkData.HEIGHT; y++) {
        for (int z = 0; z < ChunkData.DEPTH; z++) {

          assertEquals(counter, chunk.getBlockId(x, y, z));

          counter++;
        }
      }
    }
  }

  @Test
  void columnCollapseTest() {

    short stone = Blocks.STONE.getId();

    // build tower
    for (int y = 0; y < ChunkData.HEIGHT; y++) {
      chunk.setBlockId(stone, 0, y, 0);
    }

    // remove top-down
    for (int y = ChunkData.HEIGHT - 1; y >= 0; y--) {

      chunk.setBlockId(Blocks.AIR.getId(), 0, y, 0);

      int expected = y - 1;

      if (expected < 0) expected = 0;

      assertEquals(expected, chunk.getHeightValue(0, 0));
    }
  }
}
