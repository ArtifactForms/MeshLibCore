package server.util;

public class DurationParser {

  public static long parseToMillis(String input) {
    if (input == null || input.isEmpty()) return -1;

    try {
      long value = Long.parseLong(input.substring(0, input.length() - 1));
      char unit = input.charAt(input.length() - 1);

      return switch (unit) {
        case 's' -> value * 1000;
        case 'm' -> value * 60 * 1000;
        case 'h' -> value * 60 * 60 * 1000;
        case 'd' -> value * 24 * 60 * 60 * 1000;
        default -> -1;
      };
    } catch (Exception e) {
      return -1;
    }
  }

  public static boolean isDuration(String input) {
    return input != null && input.matches("\\d+[smhd]");
  }
}