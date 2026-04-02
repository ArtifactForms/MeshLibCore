package common.world;

/**
 * Utility class providing constants and helper methods for handling
 * world time in ticks.
 * <p>
 * The time system follows a fixed day cycle with a length of {@value #DAY_LENGTH} ticks.
 * Specific times of day are represented as constants within this range.
 * <p>
 * This class is not instantiable.
 */
public final class WorldTime {

  /**
   * Total number of ticks that make up a full day cycle.
   */
  public static final long DAY_LENGTH = 24000;

  /**
   * Represents early daytime (morning).
   */
  public static final long DAY = 1000;

  /**
   * Represents midday.
   */
  public static final long NOON = 6000;

  /**
   * Represents sunset.
   */
  public static final long SUNSET = 12000;

  /**
   * Represents the beginning of night.
   */
  public static final long NIGHT = 13000;

  /**
   * Represents midnight.
   */
  public static final long MIDNIGHT = 18000;

  /**
   * Represents sunrise.
   */
  public static final long SUNRISE = 23000;

  /**
   * Private constructor to prevent instantiation.
   */
  private WorldTime() {
    // Utility class
  }

  /**
   * Converts a textual time keyword into its corresponding tick value.
   * <p>
   * The comparison is case-insensitive and ignores leading and trailing whitespace.
   *
   * <h3>Supported keywords:</h3>
   * <ul>
   *   <li>{@code "day"} → {@link #DAY}</li>
   *   <li>{@code "noon"} → {@link #NOON}</li>
   *   <li>{@code "sunset"} → {@link #SUNSET}</li>
   *   <li>{@code "night"} → {@link #NIGHT}</li>
   *   <li>{@code "midnight"} → {@link #MIDNIGHT}</li>
   *   <li>{@code "sunrise"} → {@link #SUNRISE}</li>
   * </ul>
   *
   * @param keyword the time keyword to parse (must not be {@code null})
   * @return the corresponding time in ticks
   * @throws IllegalArgumentException if the keyword is {@code null}, empty, or not recognized
   */
  public static long getTicksFromKeyword(String keyword) {
    if (keyword == null) {
      throw new IllegalArgumentException("Keyword cannot be null.");
    }

    return switch (keyword.trim().toLowerCase()) {
      case "day" -> DAY;
      case "noon" -> NOON;
      case "sunset" -> SUNSET;
      case "night" -> NIGHT;
      case "midnight" -> MIDNIGHT;
      case "sunrise" -> SUNRISE;
      default -> throw new IllegalArgumentException("Invalid keyword: " + keyword);
    };
  }
}