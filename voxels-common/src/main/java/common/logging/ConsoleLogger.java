package common.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Standard implementation of the {@link Logger} interface. Provides formatted console output with
 * timestamps, thread identification, and severity levels. *
 *
 * <p>This implementation uses ANSI escape codes for color-coded output in supported terminals
 * (Green for INFO, Yellow for WARN, Red for ERROR).
 */
public class ConsoleLogger implements Logger {

  // Formatting constants
  private static final DateTimeFormatter TIME_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  // ANSI Color Codes
  private static final String RESET = "\u001B[0m";
  private static final String GREEN = "\u001B[32m";
  private static final String YELLOW = "\u001B[33m";
  private static final String RED = "\u001B[31m";
  private static final String BLUE = "\u001B[34m";

  /**
   * Logs an informational message.
   *
   * @param message The message to log.
   */
  @Override
  public void info(String message) {
    log("INFO", GREEN, message, null);
  }

  /**
   * Logs a warning message to the error stream.
   *
   * @param message The message to log.
   */
  @Override
  public void warn(String message) {
    log("WARN", YELLOW, message, null);
  }

  /**
   * Logs a debug message. Can be toggled or filtered if needed.
   *
   * @param message The message to log.
   */
  @Override
  public void debug(String message) {
    log("DEBUG", BLUE, message, null);
  }

  /**
   * Logs an error message and the associated stack trace to the error stream.
   *
   * @param message The descriptive error message.
   * @param throwable The caught exception or error.
   */
  @Override
  public void error(String message, Throwable throwable) {
    log("ERROR", RED, message, throwable);
  }

  /**
   * Internal helper to format and print the log entry. * @param level The severity level string.
   *
   * @param color The ANSI color code for the level.
   * @param message The actual log content.
   * @param t Optional Throwable for error reporting.
   */
  private void log(String level, String color, String message, Throwable t) {
    String timestamp = LocalDateTime.now().format(TIME_FORMAT);
    String threadName = Thread.currentThread().getName();

    // Format: [2026-03-12 23:15:01] [Main] [INFO]: Server started.
    String output =
        String.format(
            "[%s] [%s] [%s%s%s]: %s", timestamp, threadName, color, level, RESET, message);

    if ("ERROR".equals(level) || "WARN".equals(level)) {
      System.err.println(output);
      if (t != null) {
        t.printStackTrace(System.err);
      }
    } else {
      System.out.println(output);
    }
  }
}
