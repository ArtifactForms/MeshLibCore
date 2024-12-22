package engine.scene.light;

import math.Color;
import math.Vector3f;

/**
 * Represents a point light source in a 3D scene.
 *
 * <p>A point light simulates a light-emitting point in space, radiating light uniformly in all
 * directions. It is characterized by its position, color, intensity, and range. This class is ideal
 * for simulating localized light sources such as lightbulbs, torches, or other small light emitters
 * in a 3D environment.
 *
 * <pre>
 * Key Characteristics of a point light:
 * Position: A 3D vector representing the spatial location of the light in
 * the scene.
 * Color: The color of light the point light emits. Represented by an instance
 * of {@link math.Color}.
 * Intensity: A non-negative float value that defines how bright the light
 * appears.
 * Range: The maximum distance at which the light's effect is visible, beyond
 * which the light has no influence.
 * </pre>
 *
 * Usage: This class provides methods to dynamically configure light properties, such as changing
 * the light's intensity, range, or color at runtime. Integration with rendering systems can be
 * accomplished via the {@link LightRenderer}.
 */
public class PointLight implements Light {

  /** The color of the light emitted by the point light source. */
  private Color color;

  /** The 3D position of the point light source within the scene. */
  private Vector3f position;

  /** The intensity of the light emitted by the point light source. */
  private float intensity;

  /** The maximum distance at which the light's effect can influence objects. */
  private float range;

  /**
   * Creates a new PointLight instance with default settings.
   *
   * <p>This constructor initializes the point light with the following default values: - Color:
   * White (RGB(255, 255, 255)). - Position: (0, 0, 0). - Intensity: 1.0. - Range: 10.0.
   */
  public PointLight() {
    this(Color.WHITE, new Vector3f(0, 0, 0), 1.0f, 10.0f);
  }

  /**
   * Creates a new PointLight instance with specified parameters.
   *
   * @param color The color of the light. Must not be null.
   * @param position The 3D position of the light source in the scene. Must not be null.
   * @param intensity The intensity of the light. Must be a non-negative value.
   * @param range The maximum distance of the light's effect. Must be non-negative.
   * @throws IllegalArgumentException if any argument is invalid (e.g., null values or negative
   *     numbers).
   */
  public PointLight(Color color, Vector3f position, float intensity, float range) {
    setColor(color);
    setPosition(position);
    setIntensity(intensity);
    setRange(range);
  }

  /**
   * Gets the maximum range at which the light's effect is felt.
   *
   * @return The range of the light's effect in world units.
   */
  public float getRange() {
    return range;
  }

  /**
   * Sets the maximum range of the light's influence in the scene.
   *
   * @param range The new range value. Must be non-negative.
   * @throws IllegalArgumentException if the provided range is less than 0.
   */
  public void setRange(float range) {
    if (range < 0) {
      throw new IllegalArgumentException("Range must be non-negative.");
    }
    this.range = range;
  }

  /**
   * Gets the current intensity of the light.
   *
   * @return The intensity value, a non-negative float.
   */
  public float getIntensity() {
    return intensity;
  }

  /**
   * Sets the intensity of the light source.
   *
   * @param intensity The new intensity value to apply. Must be non-negative.
   * @throws IllegalArgumentException if intensity is less than 0.
   */
  public void setIntensity(float intensity) {
    if (intensity < 0) {
      throw new IllegalArgumentException("Intensity must be non-negative.");
    }
    this.intensity = intensity;
  }

  /**
   * Gets the color of the light emitted by the point light source.
   *
   * @return The current {@link math.Color} of the point light.
   */
  @Override
  public Color getColor() {
    return color;
  }

  /**
   * Sets the color of the light source.
   *
   * @param color The new color value for the light source. Must not be null.
   * @throws IllegalArgumentException if color is null.
   */
  public void setColor(Color color) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null.");
    }
    this.color = color;
  }

  /**
   * Gets the 3D position of the light source.
   *
   * @return The current position of the light as a {@link math.Vector3f}.
   */
  public Vector3f getPosition() {
    return position;
  }

  /**
   * Sets the 3D position of the light source within the 3D scene.
   *
   * @param position The new position value to set. Must not be null.
   * @throws IllegalArgumentException if position is null.
   */
  public void setPosition(Vector3f position) {
    if (position == null) {
      throw new IllegalArgumentException("Position cannot be null.");
    }
    this.position = position;
  }

  /**
   * Gets the type of light.
   *
   * @return The type of the light, represented as `LightType.POINT`.
   */
  @Override
  public LightType getType() {
    return LightType.POINT;
  }

  /**
   * Renders this point light source using the provided renderer.
   *
   * @param renderer The renderer responsible for rendering the light in the 3D scene.
   */
  @Override
  public void render(LightRenderer renderer) {
    renderer.render(this);
  }

  /**
   * Generates a string representation of this {@link PointLight}.
   *
   * @return A string describing the current state of this point light.
   */
  @Override
  public String toString() {
    return "PointLight [color="
        + color
        + ", position="
        + position
        + ", intensity="
        + intensity
        + ", range="
        + range
        + "]";
  }
}
