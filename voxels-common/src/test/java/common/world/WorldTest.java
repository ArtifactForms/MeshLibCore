package common.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import common.game.block.Blocks;

class WorldTest {

  private World world;

  @BeforeEach
  void setUp() {
    world = new World();
  }

  @Test
  void testAddAndGetChunk() {
    ChunkData chunk = new ChunkData(5, -2);
    world.addChunk(chunk);

    assertEquals(chunk, world.getChunk(5, -2));
    assertNull(world.getChunk(0, 0), "Should return null for non-existent chunk");
  }

  @Test
  void testGetBlockInDifferentChunks() {
    // Create two adjacent chunks
    ChunkData chunk0 = new ChunkData(0, 0);
    ChunkData chunk1 = new ChunkData(1, 0);

    // Place stone at the edge of chunk 0 (x=15) and start of chunk 1 (x=16 in world)
    chunk0.setBlockId(Blocks.STONE.getId(), 15, 64, 0);
    chunk1.setBlockId(Blocks.DIRT.getId(), 0, 64, 0);

    world.addChunk(chunk0);
    world.addChunk(chunk1);

    assertEquals(Blocks.STONE, world.getBlock(15, 64, 0));
    assertEquals(Blocks.DIRT, world.getBlock(16, 64, 0));
  }

  @Test
  void testNegativeCoordinates() {
    // This is the "Final Boss" of world coordination
    // World X: -1 should be Chunk X: -1, Local X: 15
    ChunkData chunkNeg = new ChunkData(-1, -1);
    world.addChunk(chunkNeg);

    world.setBlock(-1, 10, -1, Blocks.STONE.getId());

    assertEquals(
        Blocks.STONE.getId(),
        chunkNeg.getBlockId(15, 10, 15),
        "World -1 should map to local 15 in chunk -1");
    assertEquals(Blocks.STONE, world.getBlock(-1, 10, -1));
  }

  @ParameterizedTest
  @CsvSource({
    "0, 0, 0, 0", // Origin: World 0 -> Chunk 0, Local 0
    "15, 0, 0, 15", // World 15 -> Chunk 0, Local 15
    "16, 0, 1, 0", // Start Chunk 1: Welt 16 -> Chunk 1, Local 0
    "-1, 0, -1, 15", // Negative: World -1 -> Chunk -1, Local 15
    "-16, 0, -1, 0" // Negative: World -16 -> Chunk -1, Local 0
  })
  void testCoordinateTranslation(int worldX, int worldZ, int expectedChunkX, int expectedLocalX) {
    ChunkData chunk = new ChunkData(expectedChunkX, worldZ / 16); // simplified Z for this test
    world.addChunk(chunk);

    world.setBlock(worldX, 64, worldZ, Blocks.STONE.getId());

    assertEquals(Blocks.STONE.getId(), chunk.getBlockId(expectedLocalX, 64, 0));
  }

  @Test
  void testChunkKeyPacking() {
    int x = -500;
    int z = 123456;
    long key = World.getChunkKey(x, z);

    assertEquals(x, World.unpackChunkX(key));
    assertEquals(z, World.unpackChunkZ(key));
  }

  @Test
  void testYOutOfBounds() {
    // Test MIN_Y and MAX_Y limits
    assertEquals(Blocks.AIR, world.getBlock(0, -1, 0));
    assertEquals(Blocks.AIR, world.getBlock(0, 500, 0));

    // Ensure setBlock doesn't crash on bad Y
    world.setBlock(0, -1, 0, Blocks.STONE.getId());
    world.setBlock(0, 500, 0, Blocks.STONE.getId());
  }

  @Test
  void testIsSolid() {
    world.addChunk(new ChunkData(0, 0));
    world.setBlock(5, 5, 5, Blocks.STONE.getId());

    assertTrue(world.isSolid(5, 5, 5));
    assertFalse(world.isSolid(0, 0, 0));
    assertFalse(world.isSolid(100, 100, 100)); // No chunk here
  }

  @Test
  void testCrossChunkSetBlock() {

    ChunkData chunk0 = new ChunkData(0, 0);
    ChunkData chunk1 = new ChunkData(1, 0);

    world.addChunk(chunk0);
    world.addChunk(chunk1);

    world.setBlock(15, 10, 0, Blocks.STONE.getId());
    world.setBlock(16, 10, 0, Blocks.DIRT.getId());

    assertEquals(Blocks.STONE.getId(), chunk0.getBlockId(15, 10, 0));
    assertEquals(Blocks.DIRT.getId(), chunk1.getBlockId(0, 10, 0));
  }

  @Test
  void testNegativeBoundaryCrossing() {

    ChunkData chunkNeg = new ChunkData(-1, 0);
    ChunkData chunk0 = new ChunkData(0, 0);

    world.addChunk(chunkNeg);
    world.addChunk(chunk0);

    world.setBlock(-1, 10, 0, Blocks.STONE.getId());
    world.setBlock(0, 10, 0, Blocks.DIRT.getId());

    assertEquals(Blocks.STONE.getId(), chunkNeg.getBlockId(15, 10, 0));
    assertEquals(Blocks.DIRT.getId(), chunk0.getBlockId(0, 10, 0));
  }

  @Test
  void testLargeCoordinates() {

    int worldX = 100000;
    int worldZ = -100000;

    int chunkX = worldX >> 4;
    int chunkZ = worldZ >> 4;

    ChunkData chunk = new ChunkData(chunkX, chunkZ);
    world.addChunk(chunk);

    world.setBlock(worldX, 20, worldZ, Blocks.STONE.getId());

    assertEquals(Blocks.STONE, world.getBlock(worldX, 20, worldZ));
  }

  @Test
  void testMissingChunkAccess() {

    assertEquals(Blocks.AIR, world.getBlock(1000, 10, 1000));

    world.setBlock(1000, 10, 1000, Blocks.STONE.getId());

    // No chunk created → still air
    assertEquals(Blocks.AIR, world.getBlock(1000, 10, 1000));
  }

  @Test
  void worldChunkConsistencyTest() {

    for (int cx = -1; cx <= 1; cx++)
      for (int cz = -1; cz <= 1; cz++) world.addChunk(new ChunkData(cx, cz));

    world.setBlock(0, 10, 0, Blocks.STONE.getId());
    world.setBlock(16, 10, 0, Blocks.DIRT.getId());
    world.setBlock(-1, 10, 0, Blocks.STONE.getId());

    assertEquals(Blocks.STONE, world.getBlock(0, 10, 0));
    assertEquals(Blocks.DIRT, world.getBlock(16, 10, 0));
    assertEquals(Blocks.STONE, world.getBlock(-1, 10, 0));
  }

  @Test
  void randomWorldMutationTest() {

    java.util.Random random = new java.util.Random();

    for (int cx = -2; cx <= 2; cx++)
      for (int cz = -2; cz <= 2; cz++) world.addChunk(new ChunkData(cx, cz));

    short[] ids = {Blocks.AIR.getId(), Blocks.STONE.getId(), Blocks.DIRT.getId()};

    for (int i = 0; i < 10000; i++) {

      int x = random.nextInt(ChunkData.WIDTH * 5) - ChunkData.WIDTH * 2;
      int y = random.nextInt(100);
      int z = random.nextInt(ChunkData.DEPTH * 5) - ChunkData.DEPTH * 2;

      short id = ids[random.nextInt(ids.length)];

      world.setBlock(x, y, z, id);

      ChunkData chunk = world.getChunkAt(x, y, z);

      if (chunk != null) {
        assertEquals(id, world.getBlock(x, y, z).getId());
      }
    }
  }

  @Test
  void worldReferenceTest() {

    World world = new World();
    ReferenceWorld ref = new ReferenceWorld();

    java.util.Random random = new java.util.Random();

    for (int cx = -3; cx <= 3; cx++)
      for (int cz = -3; cz <= 3; cz++) world.addChunk(new ChunkData(cx, cz));

    short[] ids = {Blocks.AIR.getId(), Blocks.STONE.getId(), Blocks.DIRT.getId()};

    for (int i = 0; i < 50000; i++) {

      int x = random.nextInt(200) - 100;
      int y = random.nextInt(80);
      int z = random.nextInt(200) - 100;

      short id = ids[random.nextInt(ids.length)];

      world.setBlock(x, y, z, id);

      if (world.getChunkAt(x, y, z) != null) {
        ref.set(x, y, z, id);
      }

      assertEquals(
          ref.get(x, y, z),
          world.getBlock(x, y, z).getId(),
          "Mismatch at " + x + "," + y + "," + z);
    }
  }

  @Test
  void crossChunkConsistencyTest() {

    World world = new World();

    for (int cx = -1; cx <= 1; cx++)
      for (int cz = -1; cz <= 1; cz++) world.addChunk(new ChunkData(cx, cz));

    short stone = Blocks.STONE.getId();

    int[] coords = {-16, -1, 0, 15, 16, 31};

    for (int x : coords) {
      for (int z : coords) {

        world.setBlock(x, 10, z, stone);

        ChunkData chunk = world.getChunkAt(x, 10, z);

        if (chunk != null) {

          assertEquals(
              stone, world.getBlock(x, 10, z).getId(), "Mismatch at world " + x + ",10," + z);

          int lx = Math.floorMod(x, ChunkData.WIDTH);
          int lz = Math.floorMod(z, ChunkData.DEPTH);

          assertEquals(
              stone,
              chunk.getBlockId(lx, 10, lz),
              "Local mismatch at chunk (" + chunk.getChunkX() + "," + chunk.getChunkZ() + ")");
        }
      }
    }
  }

  @Test
  void millionBlockStressTest() {

    World world = new World();
    ReferenceWorld ref = new ReferenceWorld();

    java.util.Random random = new java.util.Random(1234);

    // große Chunk Area
    for (int cx = -8; cx <= 8; cx++)
      for (int cz = -8; cz <= 8; cz++) world.addChunk(new ChunkData(cx, cz));

    short[] ids = {Blocks.AIR.getId(), Blocks.STONE.getId(), Blocks.DIRT.getId()};

    for (int i = 0; i < 1_000_000; i++) {

      int x = random.nextInt(512) - 256;
      int y = random.nextInt(80);
      int z = random.nextInt(512) - 256;

      short id = ids[random.nextInt(ids.length)];

      world.setBlock(x, y, z, id);

      if (world.getChunkAt(x, y, z) != null) {
        ref.set(x, y, z, id);
      }

      short expected = ref.get(x, y, z);
      short actual = world.getBlock(x, y, z).getId();

      assertEquals(
          expected, actual, "Mismatch at " + x + "," + y + "," + z + " after iteration " + i);
    }
  }

  @Test
  void millionBlockStressWithIntegritySweep() {

    World world = new World();
    ReferenceWorld ref = new ReferenceWorld();

    java.util.Random random = new java.util.Random(1234);

    // Large Chunk Area
    for (int cx = -8; cx <= 8; cx++)
      for (int cz = -8; cz <= 8; cz++) world.addChunk(new ChunkData(cx, cz));

    short[] ids = {Blocks.AIR.getId(), Blocks.STONE.getId(), Blocks.DIRT.getId()};

    // -----------------------
    // Phase 1: Random Stress
    // -----------------------

    for (int i = 0; i < 1_000_000; i++) {

      int x = random.nextInt(512) - 256;
      int y = random.nextInt(80);
      int z = random.nextInt(512) - 256;

      short id = ids[random.nextInt(ids.length)];

      world.setBlock(x, y, z, id);

      if (world.getChunkAt(x, y, z) != null) {
        ref.set(x, y, z, id);
      }
    }

    // -----------------------
    // Phase 2: Integrity Sweep
    // -----------------------

    for (int x = -256; x < 256; x++)
      for (int z = -256; z < 256; z++)
        for (int y = 0; y < 80; y++) {

          short expected = ref.get(x, y, z);
          short actual = world.getBlock(x, y, z).getId();

          assertEquals(expected, actual, "Integrity mismatch at " + x + "," + y + "," + z);
        }
  }

  @Test
  void testWorldHeightMapBridge() {
    ChunkData chunk = new ChunkData(0, 0);

    world.addChunk(chunk);
    world.setBlock(5, 20, 5, Blocks.STONE.getId());

    assertEquals(20, world.getHeightAt(5, 5));
    assertEquals(0, world.getHeightAt(100, 100));
  }

  @Test
  void testWorldTimeDefault() {
    assertEquals(0, world.getWorldTime());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 100, 111, 20123})
  void testWorldTimeTicked(int ticks) {
    for (int i = 0; i < ticks; i++) {
      world.tick();
    }
    assertEquals(ticks, world.getWorldTime());
  }

  @ParameterizedTest
  @CsvSource({
    "0, 0.0", // Start of the day
    "6000, 0.25", // Noon (at 24k ticks)
    "12000, 0.5", // Sunset
    "18000, 0.75", // Midnight
    "24000, 0.0", // Next day (Reset/Cycle)
    "36000, 0.5" // 1.5 days passed
  })
  void testGetTimeOfDay(long ticks, float expectedProgress) {
    world.setWorldTime(ticks);
    // delta 1e-5f to account for floating point inaccuracies
    assertEquals(expectedProgress, world.getTimeOfDayNormalized(), 1e-5f);
  }

  @Test
  void testTimeOfDayPrecision() {
    // Verify that worldTime + 1 actually increases the progress value
    world.setWorldTime(0);
    float t0 = world.getTimeOfDayNormalized();
    world.tick();
    float t1 = world.getTimeOfDayNormalized();

    assertTrue(t1 > t0, "Time of day should increase after tick");
    assertEquals(1.0f / WorldTime.DAY_LENGTH, t1, 1e-7f);
  }

  @Test
  void testLargeWorldTime() {
    // Prevent overflow or precision errors in calculation (theoretically relevant after years of
    // playtime)
    long hugeTime = WorldTime.DAY_LENGTH * 1000000L + 6000;
    world.setWorldTime(hugeTime);

    assertEquals(0.25f, world.getTimeOfDayNormalized(), 1e-5f);
  }

  @Test
  void testTickUpdatesWorldTime() {
    long initialTime = world.getWorldTime();
    world.tick();
    assertEquals(initialTime + 1, world.getWorldTime());
  }

  @Test
  void testDefaultName() {
    assertEquals("", world.getName());
  }

  @Test
  void testZeroLoadedChunksByDefault() {
    assertEquals(0, world.getLoadedChunksCount());
  }

  @Test
  void testGetChunkCountReturnsOneAfterAddingChunk() {
    ChunkData chunkData = new ChunkData(0, 0);
    world.addChunk(chunkData);
    assertEquals(1, world.getLoadedChunksCount());
  }

  @Test
  void testGetLoadedChunkCountReturnsOneAfterAddingChunksWithSameCoordinates() {
    ChunkData chunkData1 = new ChunkData(10, 20);
    ChunkData chunkData2 = new ChunkData(10, 20);
    world.addChunk(chunkData1);
    world.addChunk(chunkData2);
    assertEquals(1, world.getLoadedChunksCount());
  }

  @Test
  void testGetLoadedChunkCountAfterAddingAndRemovingChunk() {
    ChunkData chunkData1 = new ChunkData(8, -1);
    world.addChunk(chunkData1);
    world.removeChunk(8, -1);
    assertEquals(0, world.getLoadedChunksCount());
  }

  @Test
  void testGetChunkFromEmptyWorldReturnsNull() {
    ChunkData chunkData = world.getChunk(0, 0);
    assertNull(chunkData);
  }
}
