package server.util;

public class TimeFormatter {

  public static String formatRemaining(long millis) {
    long seconds = millis / 1000;

    long days = seconds / 86400;
    seconds %= 86400;

    long hours = seconds / 3600;
    seconds %= 3600;

    long minutes = seconds / 60;

    if (days > 0) return days + "d";
    if (hours > 0) return hours + "h";
    if (minutes > 0) return minutes + "m";
    return seconds + "s";
  }
}
