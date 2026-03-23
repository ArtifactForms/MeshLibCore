package common.world;

public class WorldTime {

  public static final long DAY_LENGTH = 24000;

  public static final long DAY = 1000;
  public static final long NOON = 6000;
  public static final long SUNSET = 12000;
  public static final long NIGHT = 13000;
  public static final long MID_NIGHT = 18000;
  public static final long SUNRISE = 23000;

  /**
   * Helper to convert a string keyword to a tick value.
   * Returns -1 if no keyword matches.
   */
  public static long getTicksFromKeyword(String keyword) {
    return switch (keyword.toLowerCase()) {
      case "day" -> DAY;
      case "noon" -> NOON;
      case "sunset" -> SUNSET;
      case "night" -> NIGHT;
      case "midnight" -> MID_NIGHT;
      case "sunrise" -> SUNRISE;
      default -> -1;
    };
  }
}