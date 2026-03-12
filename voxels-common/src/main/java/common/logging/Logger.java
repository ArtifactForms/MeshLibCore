package common.logging;

public interface Logger {

  void info(String message);

  void warn(String message);

  void error(String message, Throwable throwable);

  void debug(String message);
}
