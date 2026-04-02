package common.world;

/**
 * Represents the mutable time state of a world.
 *
 * <p>The time system is based on a continuously increasing {@code worldTime} value measured in
 * ticks. A full day-night cycle is defined by {@link WorldTime#DAY_LENGTH}.
 *
 * <h2>Concepts</h2>
 *
 * <ul>
 *   <li><b>World Time</b>: Absolute time in ticks since world start. Always {@code >= 0}.
 *   <li><b>Day</b>: The number of full day cycles that have passed.
 *   <li><b>Time of Day</b>: The current position within the day cycle in ticks.
 *   <li><b>Normalized Time of Day</b>: A value in {@code [0.0, 1.0)} representing the relative
 *       progress within the current day.
 * </ul>
 *
 * <h2>Time Model</h2>
 *
 * <pre>
 * worldTime = day * DAY_LENGTH + timeOfDay
 * </pre>
 *
 * <p>The class does not impose any interpretation of time (e.g. sunrise, noon). Rendering systems
 * are responsible for mapping normalized time to visuals.
 *
 * <h2>Thread Safety</h2>
 *
 * <p>This class is <b>not thread-safe</b>.
 */
public class WorldTimeState {

  /**
   * Absolute world time in ticks.
   *
   * <p>Must always be {@code >= 0}.
   */
  private long worldTime;

  /**
   * Advances the world time by one tick.
   *
   * <p>This increments the absolute time and may cause the day counter to increase if a day
   * boundary is crossed.
   */
  public void tick() {
    worldTime++;
  }

  /**
   * Returns the current day index.
   *
   * <p>The day is calculated as:
   *
   * <pre>
   * day = worldTime / DAY_LENGTH
   * </pre>
   *
   * @return the number of full days elapsed since time {@code 0}
   */
  public long getDay() {
    return worldTime / WorldTime.DAY_LENGTH;
  }

  /**
   * Returns the normalized progress of the current day.
   *
   * <p>The value is in the range {@code [0.0, 1.0)} and is calculated as:
   *
   * <pre>
   * (worldTime % DAY_LENGTH) / DAY_LENGTH
   * </pre>
   *
   * <p>Example interpretation (depends on renderer):
   *
   * <ul>
   *   <li>{@code 0.0} → start of day (e.g. sunrise)
   *   <li>{@code 0.25} → morning / noon
   *   <li>{@code 0.5} → sunset
   *   <li>{@code 0.75} → night
   * </ul>
   *
   * @return normalized time of day in the range {@code [0.0, 1.0)}
   */
  public float getTimeOfDayNormalized() {
    return (worldTime % WorldTime.DAY_LENGTH) / (float) WorldTime.DAY_LENGTH;
  }

  /**
   * Returns the current time within the day cycle in ticks.
   *
   * <p>The value is always in the range {@code [0, DAY_LENGTH)}.
   *
   * @return time of day in ticks
   */
  public long getTimeOfDay() {
    return worldTime % WorldTime.DAY_LENGTH;
  }

  /**
   * Sets the time within the current day cycle.
   *
   * <p>The provided value is normalized using {@link Math#floorMod(long, long)} to ensure it falls
   * within {@code [0, DAY_LENGTH)}.
   *
   * <p><b>The current day is preserved.</b> Only the time within the day is changed.
   *
   * <pre>
   * newWorldTime = currentDay * DAY_LENGTH + normalizedTimeOfDay
   * </pre>
   *
   * @param timeOfDay the desired time of day in ticks (may be negative or exceed {@code
   *     DAY_LENGTH})
   */
  public void setTimeOfDay(long timeOfDay) {
    long normalized = Math.floorMod(timeOfDay, WorldTime.DAY_LENGTH);
    long day = worldTime / WorldTime.DAY_LENGTH;
    worldTime = day * WorldTime.DAY_LENGTH + normalized;
  }

  /**
   * Returns the absolute world time in ticks.
   *
   * @return world time (always {@code >= 0})
   */
  public long getWorldTime() {
    return worldTime;
  }

  /**
   * Sets the absolute world time.
   *
   * <p>This directly overrides both the current day and time of day.
   *
   * @param worldTime the new world time in ticks (must be {@code >= 0})
   * @throws IllegalArgumentException if {@code worldTime < 0}
   */
  public void setWorldTime(long worldTime) {
    if (worldTime < 0) {
      throw new IllegalArgumentException("World Time cannot be negative.");
    }
    this.worldTime = worldTime;
  }
}
