package engine.application;

/**
 * Encapsulates configuration settings for an application, including dimensions, fullscreen mode,
 * and title. Provides default values and validation for each setting.
 */
public class ApplicationSettings {

  private static final String DEFAULT_TITLE = "Untitled-Application";

  private int width;

  private int height;

  private boolean fullscreen;

  private String title;

  private boolean useGamePadInput;

  /**
   * Constructs an ApplicationSettings instance with default values:
   *
   * <ul>
   *   <li>Width: 1024
   *   <li>Height: 768
   *   <li>Fullscreen: false
   *   <li>Title: "Untitled-Application"
   * </ul>
   */
  public ApplicationSettings() {
    this.width = 1024;
    this.height = 768;
    this.fullscreen = false;
    this.title = DEFAULT_TITLE;
    this.useGamePadInput = false;
  }

  /**
   * Creates and returns an ApplicationSettings instance with default values.
   *
   * <ul>
   *   <li>Width: 1024
   *   <li>Height: 768
   *   <li>Fullscreen: false
   *   <li>Title: "Untitled-Application"
   * </ul>
   *
   * @return A new {@link ApplicationSettings} instance with default values.
   */
  public static ApplicationSettings defaultSettings() {
    return new ApplicationSettings();
  }

  /**
   * Gets the width of the application window.
   *
   * @return The width in pixels.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Sets the width of the application window.
   *
   * @param width The width in pixels. Must be greater than 0.
   * @throws IllegalArgumentException if the width is less than or equal to 0.
   */
  public void setWidth(int width) {
    if (width <= 0) {
      throw new IllegalArgumentException("Width must be greater than 0.");
    }
    this.width = width;
  }

  /**
   * Gets the height of the application window.
   *
   * @return The height in pixels.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Sets the height of the application window.
   *
   * @param height The height in pixels. Must be greater than 0.
   * @throws IllegalArgumentException if the height is less than or equal to 0.
   */
  public void setHeight(int height) {
    if (height <= 0) {
      throw new IllegalArgumentException("Height must be greater than 0.");
    }
    this.height = height;
  }

  /**
   * Checks if the application is set to fullscreen mode.
   *
   * @return {@code true} if fullscreen mode is enabled, {@code false} otherwise.
   */
  public boolean isFullscreen() {
    return fullscreen;
  }

  /**
   * Sets whether the application should run in fullscreen mode.
   *
   * @param fullscreen {@code true} to enable fullscreen mode, {@code false} to disable it.
   */
  public void setFullscreen(boolean fullscreen) {
    this.fullscreen = fullscreen;
  }

  /**
   * Gets the title of the application window.
   *
   * @return The title as a {@link String}.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the application window.
   *
   * @param title The title of the application window. Cannot be {@code null} or empty.
   * @throws IllegalArgumentException if the title is {@code null} or empty.
   */
  public void setTitle(String title) {
    if (title == null || title.isEmpty()) {
      throw new IllegalArgumentException("Title cannot be null or empty.");
    }
    this.title = title;
  }

  public boolean isUseGamePadInput() {
    return useGamePadInput;
  }

  public void setUseGamePadInput(boolean useGamePadInput) {
    this.useGamePadInput = useGamePadInput;
  }
}
