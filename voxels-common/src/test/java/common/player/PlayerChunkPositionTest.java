package common.player;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.world.ChunkData;

public class PlayerChunkPositionTest {

  private PlayerData player;

  @BeforeEach
  void setup() {
    player = new PlayerData(UUID.randomUUID(), "Test-Player");
  }

  @Test
  void testOrigin() {
    player.setPosition(0f, 0, 0);
    assertEquals(0, player.getChunkX());
    assertEquals(0, player.getChunkZ());
  }

  @Test
  void testBlockBoundaryPositive() {
    player.setPosition(0.4999f, 0, 0);
    assertEquals(0, player.getChunkX());

    player.setPosition(0.500001f, 0, 0);
    assertEquals(0, player.getChunkX()); // still same chunk
  }

  @Test
  void testBlockBoundaryNegative() {
    player.setPosition(-0.4999f, 0, 0);
    assertEquals(0, player.getChunkX());

    player.setPosition(-0.500001f, 0, 0);
    assertEquals(-1, player.getChunkX());
  }

  @Test
  void testChunkBoundaryPositive() {
    float size = ChunkData.WIDTH;

    player.setPosition(size - 0.5001f, 0, 0);
    assertEquals(0, player.getChunkX());

    player.setPosition(size - 0.4999f, 0, 0);
    assertEquals(1, player.getChunkX());
  }

  @Test
  void testChunkBoundaryNegative() {
    float size = ChunkData.WIDTH;

    player.setPosition(-size + 0.4999f, 0, 0);
    assertEquals(-1, player.getChunkX());

    player.setPosition(-size + 0.5001f, 0, 0);
    assertEquals(-1, player.getChunkX());
  }

  @Test
  void testExactChunkBoundaries() {
    float size = ChunkData.WIDTH;

    player.setPosition(size - 0.5001f, 0, 0);
    assertEquals(0, player.getChunkX());

    player.setPosition(size - 0.5f, 0, 0);
    assertEquals(1, player.getChunkX());
  }

  @Test
  void testLargePositive() {
    player.setPosition(1000f, 0, 0);
    assertEquals((int) Math.floor((1000f + 0.5f) / ChunkData.WIDTH), player.getChunkX());
  }

  @Test
  void testLargeNegative() {
    player.setPosition(-1000f, 0, 0);
    assertEquals((int) Math.floor((-1000f + 0.5f) / ChunkData.WIDTH), player.getChunkX());
  }

  @Test
  void testZAxis() {
    player.setPosition(0, 0, -0.500001f);
    assertEquals(-1, player.getChunkZ());

    player.setPosition(0, 0, -0.4999f);
    assertEquals(0, player.getChunkZ());
  }

  @Test
  void testYIndependence() {
    player.setPosition(10.0f, 0.0f, 10.0f);
    int chunkX = player.getChunkX();

    player.setPosition(10.0f, 500.0f, 10.0f);
    assertEquals(chunkX, player.getChunkX());

    player.setPosition(10.0f, -100.0f, 10.0f);
    assertEquals(chunkX, player.getChunkX());
  }

  @Test
  void testExtremeValues() {
    float extreme = 1_000_000.0f;
    player.setPosition(extreme, 0, 0);

    int expected = (int) Math.floor((extreme + 0.5f) / ChunkData.WIDTH);
    assertEquals(expected, player.getChunkX());
  }

  @Test
  void testChunkChangeTrigger() {
    float size = ChunkData.WIDTH;

    player.setPosition(0, 0, 0);
    int first = player.getChunkX();

    player.setPosition(size * 2, 0, 0);
    int second = player.getChunkX();

    assertEquals(first + 2, second);
  }

  // -------------------------------------------------------
  // SWEEP
  // -------------------------------------------------------

  @Test
  void testSweepAcrossZero() {
    float size = ChunkData.WIDTH;

    for (float x = -2.0f; x <= 2.0f; x += 0.001f) {
      player.setPosition(x, 0, 0);

      int expected = (int) Math.floor((x + 0.5f) / size);
      assertEquals(expected, player.getChunkX(), "Failed at x=" + x);
    }
  }

  @Test
  void testSweepAcrossZeroZ() {
    float size = ChunkData.DEPTH;

    for (float z = -2.0f; z <= 2.0f; z += 0.001f) {
      player.setPosition(0, 0, z);

      int expected = (int) Math.floor((z + 0.5f) / size);
      assertEquals(expected, player.getChunkZ(), "Failed at z=" + z);
    }
  }

  @Test
  void testSweep2D() {
    float sizeX = ChunkData.WIDTH;
    float sizeZ = ChunkData.DEPTH;

    for (float x = -2.0f; x <= 2.0f; x += 0.25f) {
      for (float z = -2.0f; z <= 2.0f; z += 0.25f) {
        player.setPosition(x, 0, z);

        int expectedX = (int) Math.floor((x + 0.5f) / sizeX);
        int expectedZ = (int) Math.floor((z + 0.5f) / sizeZ);

        assertEquals(expectedX, player.getChunkX(), "Failed at x=" + x);
        assertEquals(expectedZ, player.getChunkZ(), "Failed at z=" + z);
      }
    }
  }
}
