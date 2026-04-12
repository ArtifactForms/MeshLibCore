package common.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class WorldTimeStateTest {

  private WorldTimeState time;

  @BeforeEach
  void setUp() {
    time = new WorldTimeState();
  }

  // ------------------------------------
  // Default state
  // ------------------------------------

  @Test
  void worldTimeShouldBeZeroByDefault() {
    assertEquals(0, time.getWorldTime());
  }

  @Test
  void dayShouldBeZeroByDefault() {
    assertEquals(0, time.getDay());
  }

  // ------------------------------------
  // Tick behavior
  // ------------------------------------

  @Test
  void tickShouldIncreaseWorldTimeByOne() {
    time.tick();
    assertEquals(1, time.getWorldTime());
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 2, 3, 60, 120, 8234})
  void tickShouldAccumulateCorrectly(long tickCount) {
    for (long i = 0; i < tickCount; i++) {
      time.tick();
    }
    assertEquals(tickCount, time.getWorldTime());
  }

  @Test
  void tickShouldWrapToNextDayWhenEndOfDayReached() {
    time.setWorldTime(WorldTime.DAY_LENGTH - 1);

    time.tick();

    assertEquals(1, time.getDay());
    assertEquals(0, time.getTimeOfDay());
  }

  // ------------------------------------
  // World time setter
  // ------------------------------------

  @ParameterizedTest
  @ValueSource(longs = {0, 45, 223, 100000234})
  void setWorldTimeShouldStoreExactValue(long worldTime) {
    time.setWorldTime(worldTime);
    assertEquals(worldTime, time.getWorldTime());
  }

  @Test
  void setWorldTimeShouldThrowExceptionWhenNegative() {
    assertThrows(IllegalArgumentException.class, () -> time.setWorldTime(-1));
  }

  // ------------------------------------
  // Normalized / edge cases
  // ------------------------------------

  @Test
  void normalizedMatchesTimeOfDay() {
    time.setWorldTime(12345);

    float normalized = time.getTimeOfDayNormalized();
    long timeOfDay = time.getTimeOfDay();

    assertEquals(timeOfDay / (float) WorldTime.DAY_LENGTH, normalized);
  }

  @Test
  void normalizedAtStartOfDayIsZero() {
    time.setWorldTime(0);
    assertEquals(0.0f, time.getTimeOfDayNormalized());
  }

  @Test
  void normalizedAtEndOfDayIsAlmostOne() {
    time.setWorldTime(WorldTime.DAY_LENGTH - 1);

    float value = time.getTimeOfDayNormalized();

    assertTrue(value > 0.999f);
    assertTrue(value < 1.0f);
  }

  @Test
  void normalizedWrapsCorrectlyAcrossDays() {
    time.setWorldTime(WorldTime.DAY_LENGTH + 1200);

    float expected = 1200f / WorldTime.DAY_LENGTH;

    assertEquals(expected, time.getTimeOfDayNormalized());
  }

  // ------------------------------------
  // Exceptions / Edge cases
  // ------------------------------------

  @Test
  void setTimeOfDayWrapsLargeValues() {
    time.setWorldTime(0);

    time.setTimeOfDay(WorldTime.DAY_LENGTH * 3 + 42);

    assertEquals(42, time.getTimeOfDay());
  }

  @Test
  void setTimeOfDayDoesNotChangeDay() {
    time.setWorldTime(WorldTime.DAY_LENGTH * 5 + 123);

    time.setTimeOfDay(42);

    assertEquals(5, time.getDay());
  }

  @Test
  void setTimeOfDayWithNegativeWrapsCorrectly() {
    time.setWorldTime(WorldTime.DAY_LENGTH); // day = 1

    time.setTimeOfDay(-1);

    assertEquals(1, time.getDay());
    assertEquals(WorldTime.DAY_LENGTH - 1, time.getTimeOfDay());
  }

  @Test
  void largeWorldTimeShouldCalculateDayAndTimeCorrectly() {
    long manyDays = 1_000_000L * WorldTime.DAY_LENGTH;
    long offset = 1234L;
    time.setWorldTime(manyDays + offset);

    assertEquals(1_000_000L, time.getDay());
    assertEquals(offset, time.getTimeOfDay());
  }

  @Test
  void tickCrossesDayBoundaryCorrectly() {
    time.setWorldTime(WorldTime.DAY_LENGTH - 1);

    time.tick();

    assertEquals(1, time.getDay());
    assertEquals(0, time.getTimeOfDay());
  }

  @Test
  void timeOfDayWrapsForLargeWorldTime() {
    long big = WorldTime.DAY_LENGTH * 1000 + 1234;

    time.setWorldTime(big);

    assertEquals(1234, time.getTimeOfDay());
  }

  // ------------------------------------
  // Day calculation
  // ------------------------------------

  @Test
  void getDayShouldReturnCorrectDayForExactDayBoundaries() {
    time.setWorldTime(WorldTime.DAY_LENGTH);
    assertEquals(1, time.getDay());

    time.setWorldTime(WorldTime.DAY_LENGTH * 2);
    assertEquals(2, time.getDay());
  }

  @Test
  void getDayShouldIncreaseOnlyAfterFullDayPassed() {
    time.setWorldTime(WorldTime.DAY_LENGTH * 2 + 1);
    assertEquals(2, time.getDay());
  }

  // ------------------------------------
  // Time of day
  // ------------------------------------

  @Test
  void getTimeOfDayShouldWrapWithinDayLength() {
    time.setWorldTime(WorldTime.DAY_LENGTH + 500);
    assertEquals(500, time.getTimeOfDay());
  }

  // ------------------------------------
  // setTimeOfDay behavior
  // ------------------------------------

  @Test
  void setTimeOfDayShouldPreserveCurrentDay() {
    time.setWorldTime(WorldTime.DAY_LENGTH * 2 + 123);

    time.setTimeOfDay(WorldTime.NOON);

    assertEquals(2, time.getDay());
    assertEquals(WorldTime.NOON, time.getTimeOfDay());
  }

  // ------------------------------------
  // Normalized time of day
  // ------------------------------------

  @Test
  void normalizedTimeShouldBeZeroAtStartOfDay() {
    time.setWorldTime(0);
    assertEquals(0.0f, time.getTimeOfDayNormalized());
  }

  @Test
  void normalizedTimeShouldBeHalfAtMidDay() {
    time.setWorldTime(WorldTime.DAY_LENGTH / 2);
    assertEquals(0.5f, time.getTimeOfDayNormalized());
  }

  @Test
  void normalizedTimeShouldBeCloseToOneAtEndOfDay() {
    time.setWorldTime(WorldTime.DAY_LENGTH - 1);

    float expected = (WorldTime.DAY_LENGTH - 1) / (float) WorldTime.DAY_LENGTH;
    assertEquals(expected, time.getTimeOfDayNormalized());
  }

  @Test
  void normalizedTimeShouldWrapAfterFullDay() {
    time.setWorldTime(WorldTime.DAY_LENGTH);

    assertEquals(0.0f, time.getTimeOfDayNormalized());
  }

  @Test
  void normalizedTimeShouldWrapCorrectlyForArbitraryValue() {
    time.setWorldTime(WorldTime.DAY_LENGTH + 6000);

    assertEquals(6000f / WorldTime.DAY_LENGTH, time.getTimeOfDayNormalized());
  }
}
