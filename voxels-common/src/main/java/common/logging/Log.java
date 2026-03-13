package common.logging;

/**
 * Static wrapper acting as the entry point for the logging system. This class decouples the
 * application logic from the specific logger implementation.
 */
public class Log {

  // Default to a simple implementation so we don't get NullPointerExceptions
  private static Logger instance = new ConsoleLogger("");

  /**
   * Replaces the current logger implementation (e.g., to switch from Console to File).
   *
   * @param logger Implementation of the Logger interface.
   */
  public static void setImplementation(Logger logger) {
    if (logger != null) {
      instance = logger;
    }
  }

  public static void info(String message) {
    instance.info(message);
  }

  public static void warn(String message) {
    instance.warn(message);
  }

  public static void debug(String message) {
    instance.debug(message);
  }

  /** Logs an error with an optional exception stack trace. */
  public static void error(String message, Throwable throwable) {
    instance.error(message, throwable);
  }

  // Overload for errors without a Throwable
  public static void error(String message) {
    instance.error(message, null);
  }
}
