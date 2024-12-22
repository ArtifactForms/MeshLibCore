package engine.scene.light;

import math.Color;

/**
 * Interface for defining light sources within a 3D scene.
 *
 * <p>This interface serves as a contract for all light types (e.g., PointLight, DirectionalLight,
 * SpotLight) by defining essential behaviors and properties that any light source should possess.
 * It provides mechanisms to query a light's type, retrieve its color, and delegate rendering logic
 * to a given renderer.
 */
public interface Light {

  /**
   * Gets the color of the light emitted by the light source.
   *
   * @return The {@link Color} object representing the light's color. The color should define the
   *     RGB components that determine the light's hue and saturation.
   */
  Color getColor();

  /**
   * Gets the type of the light source.
   *
   * @return The {@link LightType} that identifies the specific type of light (e.g., POINT,
   *     DIRECTIONAL, or SPOT) this instance represents.
   */
  LightType getType();

  /**
   * Gets the light source using the provided renderer to draw the light's effects.
   *
   * <p>This method allows the implementation to delegate rendering logic to the given {@link
   * LightRenderer}. The rendering logic could involve adding effects like shadows, light rays, or
   * other visual representations specific to the light's type.
   *
   * @param renderer The {@link LightRenderer} implementation responsible for rendering this light's
   *     effects in the scene.
   */
  void render(LightRenderer renderer);
}
