package engine.scene.light;

import math.Color;
import math.Vector3f;

/**
 * Represents a spotlight in a 3D scene.
 *
 * <p>A spotlight emits light in a cone shape, with a defined position, direction, cone angle, and
 * concentration (center bias). This class models the essential properties of a spotlight, allowing
 * users to specify its behavior and appearance in a 3D environment.
 *
 * <pre>
 * Key properties include:
 * - Position: The 3D coordinates where the spotlight is located.
 * - Direction: The orientation direction the spotlight points towards.
 * - Color: The color of the emitted light.
 * - Angle: The cone angle, defining the spread of the light in radians.
 * - Concentration: The exponent controlling how focused the spotlight
 *   is on its center.
 * </pre>
 *
 * This class supports both a default spotlight configuration and customizable initialization via
 * its constructors. Input values are validated to ensure realistic and meaningful spotlight
 * behavior.
 */
public class SpotLight implements Light {

  /** 45° in radians, the default cone angle for a standard spotlight. */
  private static final float DEFAULT_ANGLE = (float) Math.PI / 4;

  /** Default center bias value for the spotlight's cone. */
  private static final float DEFAULT_CONCENTRATION = 10.0f;

  /** The default position of the spotlight, located at the origin. */
  private static final Vector3f DEFAULT_POSITION = new Vector3f(0, 0, 0);

  /** The default direction for the spotlight, pointing along the negative Z-axis. */
  private static final Vector3f DEFAULT_DIRECTION = new Vector3f(0, 0, -1);

  /** The default color of the spotlight's emitted light (white light). */
  private static final Color DEFAULT_COLOR = Color.WHITE;

  /** The angle of the spotlight's cone in radians. */
  private float angle;

  /** Determines the spotlight's intensity concentration toward its center. */
  private float concentration;

  /** The position of the spotlight in 3D space. */
  private Vector3f position;

  /** The direction vector indicating the spotlight's orientation. */
  private Vector3f direction;

  /** The color of the emitted spotlight's light. */
  private Color color;

  /**
   * Default constructor initializes the spotlight with pre-defined defaults.
   *
   * <pre>
   * The defaults include:
   * - Position at (0,0,0).
   * - Direction pointing along the negative Z-axis.
   * - White color.
   * - A cone angle of 45° (π/4 radians).
   * - A concentration value of 10.0 (focused light)
   * </pre>
   */
  public SpotLight() {
    this(DEFAULT_POSITION, DEFAULT_DIRECTION, DEFAULT_COLOR, DEFAULT_CONCENTRATION, DEFAULT_ANGLE);
  }

  /**
   * Constructs a new SpotLight instance with the specified properties.
   *
   * <p>Initializes the spotlight with the provided position, direction, color, concentration, and
   * cone angle values. Each input is validated to ensure it adheres to acceptable ranges or
   * requirements.
   *
   * @param position The 3D position of the spotlight. Must not be null.
   * @param direction The direction the spotlight points towards. Must not be null.
   * @param color The emitted light's color. Must not be null.
   * @param concentration The center bias (intensity focus) of the spotlight cone. Must be
   *     non-negative.
   * @param angle The cone angle in radians. Must be greater than 0 and less than or equal to π
   *     radians.
   * @throws IllegalArgumentException if any of the following conditions are met: - `position` is
   *     null. - `direction` is null. - `color` is null. - `concentration` is negative. - `angle` is
   *     less than or equal to 0, or greater than π radians.
   */
  public SpotLight(
      Vector3f position, Vector3f direction, Color color, float concentration, float angle) {
    setPosition(position);
    setDirection(direction);
    setColor(color);
    setConcentration(concentration);
    setAngle(angle);
  }

  /**
   * Gets the angle of the spotlight cone.
   *
   * @return The cone's angle in radians.
   */
  public float getAngle() {
    return angle;
  }

  /**
   * Sets the cone angle, ensuring it is within valid physical limits.
   *
   * @param angle The new angle of the spotlight cone.
   * @throws IllegalArgumentException if the value is less than or equal to 0 or exceeds π radians.
   */
  public void setAngle(float angle) {
    if (angle <= 0 || angle > Math.PI) {
      throw new IllegalArgumentException("Angle must be between 0 and PI radians.");
    }
    this.angle = angle;
  }

  /**
   * Gets the concentration (center bias) of the spotlight's cone.
   *
   * @return The concentration value of the spotlight.
   */
  public float getConcentration() {
    return concentration;
  }

  /**
   * Sets the concentration value for the spotlight cone's focus.
   *
   * @param concentration The new concentration value.
   * @throws IllegalArgumentException if the value is negative.
   */
  public void setConcentration(float concentration) {
    if (concentration < 0) {
      throw new IllegalArgumentException("Concentration must be non-negative.");
    }
    this.concentration = concentration;
  }

  /**
   * Retrieves the direction vector of the spotlight.
   *
   * @return The current direction vector.
   */
  public Vector3f getDirection() {
    return direction;
  }

  /**
   * Sets the direction vector of the spotlight.
   *
   * @param direction The new direction vector.
   * @throws IllegalArgumentException if the provided vector is null.
   */
  public void setDirection(Vector3f direction) {
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null.");
    }
    this.direction = direction;
  }

  /**
   * Retrieves the position of the spotlight.
   *
   * @return The position vector.
   */
  public Vector3f getPosition() {
    return position;
  }

  /**
   * Sets the position of the spotlight in 3D space.
   *
   * @param position The new position vector.
   * @throws IllegalArgumentException if the provided vector is null.
   */
  public void setPosition(Vector3f position) {
    if (position == null) {
      throw new IllegalArgumentException("Position cannot be null.");
    }
    this.position = position;
  }

  /**
   * Retrieves the color of the spotlight's light.
   *
   * @return The spotlight's color.
   */
  @Override
  public Color getColor() {
    return color;
  }

  /**
   * Sets the color of the spotlight's emitted light.
   *
   * @param color The new color value.
   * @throws IllegalArgumentException if the provided color is null.
   */
  public void setColor(Color color) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null.");
    }
    this.color = color;
  }

  /**
   * Determines the type of light, specifically `LightType.SPOT`.
   *
   * @return The type of light.
   */
  @Override
  public LightType getType() {
    return LightType.SPOT;
  }

  /**
   * Renders the spotlight using the provided rendering system.
   *
   * <p>Delegates rendering logic to the specified {@link LightRenderer}.
   *
   * @param renderer The renderer responsible for spotlight rendering.
   */
  @Override
  public void render(LightRenderer renderer) {
    renderer.render(this);
  }

  /**
   * Provides a string representation of this spotlight instance for debugging.
   *
   * @return String describing the current state of the spotlight.
   */
  @Override
  public String toString() {
    return "SpotLight [angle="
        + angle
        + ", concentration="
        + concentration
        + ", position="
        + position
        + ", direction="
        + direction
        + ", color="
        + color
        + "]";
  }
}
