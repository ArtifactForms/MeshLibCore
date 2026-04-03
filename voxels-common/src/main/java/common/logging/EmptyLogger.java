package common.logging;

public class EmptyLogger implements Logger {

  @Override
  public void info(String message) {
    // Do nothing
  }

  @Override
  public void warn(String message) {
    // Do nothing
  }

  @Override
  public void error(String message, Throwable throwable) {
    // Do nothing
  }

  @Override
  public void debug(String message) {
    // Do nothing
  }
}
