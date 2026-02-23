package engine.scene.light;

import math.Color;
import math.Vector3f;

/**
 * Represents a directional light source in a 3D scene.
 *
 * <p>A directional light simulates light emitted from a distant source, such as the sun or moon.
 * Unlike point lights or spotlights, directional lights have no specific position, and their light
 * rays travel in a uniform direction throughout the scene. This makes them ideal for creating
 * consistent lighting over large areas.
 *
 * <pre>
 * Key characteristics of a directional light:
 * - The light's direction is defined by a normalized vector.
 * - It emits light uniformly in the specified direction, without attenuation
 *   (intensity does not decrease with distance).
 * - It is commonly used to simulate natural light sources like sunlight during
 *   the day or moonlight at night.
 * </pre>
 *
 * This class provides methods to configure the light's direction, color, and intensity, as well as
 * integration with rendering systems via the {@link LightRenderer}.
 */
public class DirectionalLight implements Light {

  /** The color of the light emitted by the directional light source. */
  private Color color;

  /** The direction of the light source. */
  private Vector3f direction;

  /** The intensity of the light emitted by the directional light source. */
  private float intensity;

  /**
   * Creates a new DirectionalLight instance with default settings.
   *
   * <p>This constructor initializes the light with the following defaults: - Color: White light.
   * RGB(255, 255, 255) - Direction: A downward-facing vector (0, 1, 0), simulating overhead light.
   * - Intensity: 1.0 (full strength).
   */
  public DirectionalLight() {
    this(new Color(1, 1, 1, 1), new Vector3f(0, 1, 0), 1.0f);
  }

  /**
   * Creates a new DirectionalLight instance with the specified color and direction.
   *
   * <p>This constructor initializes the light with the given color and direction, and a default
   * intensity of 1.0. The provided direction vector is normalized during initialization to ensure
   * consistent light behavior.
   *
   * @param color The color of the light emitted by the directional light source. Represents the RGB
   *     values of the light's color. This parameter cannot be null.
   * @param direction The direction of the light source. This vector determines the direction in
   *     which the light rays travel. The provided vector is automatically normalized during
   *     construction, ensuring the direction's magnitude is always 1. This parameter cannot be
   *     null.
   * @throws IllegalArgumentException if the direction or color is null.
   */
  public DirectionalLight(Color color, Vector3f direction) {
    this(color, direction, 1.0f);
  }

  /**
   * Creates a new DirectionalLight instance.
   *
   * @param color The color of the light emitted by the directional light source. Represents the RGB
   *     values of the light's color. This parameter cannot be null.
   * @param direction The direction of the light source. This vector determines the direction in
   *     which the light rays travel. The provided vector is automatically normalized during
   *     construction, ensuring the direction's magnitude is always 1. This parameter cannot be
   *     null.
   * @param intensity The intensity of the light emitted by the directional light source. This value
   *     must be non-negative.
   * @throws IllegalArgumentException if the direction or color is null, or if the intensity is
   *     negative.
   */
  public DirectionalLight(Color color, Vector3f direction, float intensity) {
    setColor(color);
    setDirection(direction);
    setIntensity(intensity);
  }

  /**
   * Gets the direction of the light source.
   *
   * @return The direction of the light source.
   * @see #setDirection(Vector3f)
   */
  public Vector3f getDirection() {
    return direction;
  }

  /**
   * Sets the direction of the directional light source.
   *
   * <p>The provided vector is normalized to ensure the light's direction always has a magnitude of
   * 1, which maintains consistent light behavior. This method validates that the input is not null
   * to avoid runtime errors.
   *
   * @param direction The new direction vector for the light source. This vector defines the
   *     direction in which the light rays travel.
   * @throws IllegalArgumentException if the provided direction vector is null.
   */
  public void setDirection(Vector3f direction) {
    if (direction == null) throw new IllegalArgumentException("Direction cannot be null.");
    this.direction = direction.normalize();
  }

  /**
   * Gets the color of the light emitted by the directional light source.
   *
   * @return The color of the light.
   * @see #setColor(Color)
   */
  @Override
  public Color getColor() {
    return color;
  }

  /**
   * Sets the color of the directional light source.
   *
   * <p>This method updates the light's emitted color. It validates that the provided color is not
   * null to ensure the light's color is always valid.
   *
   * @param color The new color of the light to set. Represents the RGB values of the light's color.
   * @throws IllegalArgumentException if the provided color is null.
   */
  public void setColor(Color color) {
    if (color == null) throw new IllegalArgumentException("Color cannot be null.");
    this.color = color;
  }

  /**
   * Sets the color of the directional light using individual RGB components.
   *
   * <p>This method updates the internal {@link Color} instance by modifying its red, green, and
   * blue components directly. It does not replace the {@code Color} object reference, but instead
   * mutates the existing instance.
   *
   * <p>The provided values are expected to be within the valid color range defined by the {@link
   * Color} class (normalized 0.0–1.0).
   *
   * @param r The red component of the light color.
   * @param g The green component of the light color.
   * @param b The blue component of the light color.
   * @throws IllegalStateException if the internal color instance is null.
   * @see #setColor(Color)
   * @see #setColor(Vector3f)
   */
  public void setColor(float r, float g, float b) {
    if (color == null) {
      throw new IllegalStateException("Color instance is not initialized.");
    }
    color.set(r, g, b);
  }

  /**
   * Sets the color of the directional light using a {@link Vector3f}.
   *
   * <p>The vector's x, y, and z components represent the red, green, and blue channels of the light
   * color. This method updates the internal {@link Color} instance without replacing it.
   *
   * <p>This overload is particularly useful for mathematical operations such as interpolation
   * (e.g., linear interpolation between day and night colors in a day-night cycle system).
   *
   * @param color A vector containing RGB values (x = red, y = green, z = blue).
   * @throws IllegalArgumentException if the provided vector is null.
   * @throws IllegalStateException if the internal color instance is null.
   * @see #setColor(Color)
   * @see #setColor(float, float, float)
   */
  public void setColor(Vector3f color) {
    if (color == null) {
      throw new IllegalArgumentException("Color vector cannot be null.");
    }
    if (this.color == null) {
      throw new IllegalStateException("Color instance is not initialized.");
    }
    this.color.set(color.x, color.y, color.z);
  }

  /**
   * Gets the intensity of the light emitted by the directional light source.
   *
   * @return The intensity of the light.
   * @see #setIntensity(float)
   */
  public float getIntensity() {
    return intensity;
  }

  /**
   * Sets the intensity of the light emitted by the directional light source.
   *
   * <p>The intensity value determines how bright the light appears in the scene. This method
   * ensures that the value is non-negative, as negative intensity does not make logical sense in
   * this context.
   *
   * @param intensity The new intensity value to set for the light source. Must be non-negative to
   *     represent valid light brightness.
   * @throws IllegalArgumentException if the provided intensity is negative.
   */
  public void setIntensity(float intensity) {
    if (intensity < 0) throw new IllegalArgumentException("Intensity must be non-negative.");
    this.intensity = intensity;
  }

  /**
   * Gets the type of the light source.
   *
   * @return The type of the light source, which is `LightType.DIRECTIONAL`.
   */
  @Override
  public LightType getType() {
    return LightType.DIRECTIONAL;
  }

  /**
   * Renders the directional light source using the provided renderer.
   *
   * @param renderer The renderer to use for rendering the light source.
   */
  @Override
  public void render(LightRenderer renderer) {
    renderer.render(this);
  }

  /**
   * Provides a string representation of this directional light instance for debugging.
   *
   * @return String describing the current state of the directional light.
   */
  @Override
  public String toString() {
    return "DirectionalLight [color="
        + color
        + ", direction="
        + direction
        + ", intensity="
        + intensity
        + "]";
  }
}
