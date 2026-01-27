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
   * @return A new {@link ApplicationSettings} instance with default values.
   */
  public static ApplicationSettings defaultSettings() {
    return new ApplicationSettings();
  }

  /** @return The width in pixels. */
  public int getWidth() {
    return width;
  }

  /**
   * Sets the width of the application window.
   *
   * @param width The width in pixels. Must be greater than 0.
   * @return this {@link ApplicationSettings} instance for chaining.
   * @throws IllegalArgumentException if the width is less than or equal to 0.
   */
  public ApplicationSettings setWidth(int width) {
    if (width <= 0) {
      throw new IllegalArgumentException("Width must be greater than 0.");
    }
    this.width = width;
    return this;
  }

  /** @return The height in pixels. */
  public int getHeight() {
    return height;
  }

  /**
   * Sets the height of the application window.
   *
   * @param height The height in pixels. Must be greater than 0.
   * @return this {@link ApplicationSettings} instance for chaining.
   * @throws IllegalArgumentException if the height is less than or equal to 0.
   */
  public ApplicationSettings setHeight(int height) {
    if (height <= 0) {
      throw new IllegalArgumentException("Height must be greater than 0.");
    }
    this.height = height;
    return this;
  }

  /** @return {@code true} if fullscreen mode is enabled. */
  public boolean isFullscreen() {
    return fullscreen;
  }

  /**
   * Sets whether the application should run in fullscreen mode.
   *
   * @param fullscreen {@code true} to enable fullscreen mode, {@code false} to disable it.
   * @return this {@link ApplicationSettings} instance for chaining.
   */
  public ApplicationSettings setFullscreen(boolean fullscreen) {
    this.fullscreen = fullscreen;
    return this;
  }

  /** @return The title of the application window. */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the application window.
   *
   * @param title The title of the application window. Cannot be {@code null} or empty.
   * @return this {@link ApplicationSettings} instance for chaining.
   * @throws IllegalArgumentException if the title is {@code null} or empty.
   */
  public ApplicationSettings setTitle(String title) {
    if (title == null || title.isEmpty()) {
      throw new IllegalArgumentException("Title cannot be null or empty.");
    }
    this.title = title;
    return this;
  }

  /** @return {@code true} if gamepad input is enabled. */
  public boolean isUseGamePadInput() {
    return useGamePadInput;
  }

  /**
   * Enables or disables gamepad input.
   *
   * @param useGamePadInput {@code true} to enable gamepad input.
   * @return this {@link ApplicationSettings} instance for chaining.
   */
  public ApplicationSettings setUseGamePadInput(boolean useGamePadInput) {
    this.useGamePadInput = useGamePadInput;
    return this;
  }
}
