package engine.scene.light;

import math.Color;

/**
 * Represents an ambient light source in a 3D scene.
 *
 * <p>Ambient light provides uniform, non-directional illumination throughout the entire 3D scene.
 * Unlike other types of lights, such as point lights or directional lights, ambient light does not
 * have a specific position or direction, and it affects all objects equally, regardless of their
 * location or orientation. This is ideal for simulating indirect or global lighting effects that
 * ensure objects are visible even when not directly illuminated by other light sources.
 *
 * <pre>
 * Key Characteristics of an ambient light:
 * - Color: Defined by an instance of {@link math.Color},
 *   representing the light's hue.
 * - No position: Ambient light is global, meaning it affects all parts of the
 *   scene uniformly.
 * - Non-directional: Ambient light lacks a specific directional focus and has
 *   equal influence everywhere.
 * </pre>
 *
 * Usage: This class allows you to dynamically configure the ambient light's color during runtime.
 * Integration with rendering systems can be achieved via the {@link LightRenderer}.
 */
public class AmbientLight implements Light {

  /** Ambient color. */
  private Color color;

  /** Constructs a new AmbientLight with a default color of white. */
  public AmbientLight() {
    this(Color.WHITE);
  }

  /**
   * Constructs a new AmbientLight with the specified color.
   *
   * @param color the color of the ambient light. Must not be null.
   * @throws IllegalArgumentException if the color is null.
   */
  public AmbientLight(Color color) {
    setColor(color);
  }

  /**
   * Gets the type of the light.
   *
   * @return the light type, which is {@link LightType#AMBIENT}.
   */
  @Override
  public LightType getType() {
    return LightType.AMBIENT;
  }

  /**
   * Renders the ambient light using the specified {@link LightRenderer}.
   *
   * @param renderer the renderer to use for rendering the light.
   */
  @Override
  public void render(LightRenderer renderer) {
    renderer.render(this);
  }

  /**
   * Gets the color of the ambient light.
   *
   * @return the color of the light. Never null.
   */
  @Override
  public Color getColor() {
    return color;
  }

  /**
   * Sets the color of the ambient light.
   *
   * @param color the new color for the ambient light. Must not be null.
   * @throws IllegalArgumentException if the color is null.
   */
  public void setColor(Color color) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null.");
    }
    this.color = color;
  }
}
