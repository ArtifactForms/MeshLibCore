package engine.scene.light;

import workspace.ui.Graphics;

/**
 * Interface for rendering various light sources in a 3D scene.
 * <p>
 * This interface establishes a contract for rendering different types of light
 * sources in a 3D environment. It provides specific rendering methods for each
 * type of light, such as {@link PointLight}, {@link DirectionalLight}, and
 * {@link SpotLight}. Implementations of this interface handle the actual
 * rendering logic for these light types within a 3D graphics or game engine.
 * </p>
 */
public interface LightRenderer {

	/**
	 * Sets the graphics context for the light renderer.
	 * <p>
	 * This method initializes the rendering environment by associating the given
	 * {@link Graphics} instance with the light renderer. The graphics context is
	 * responsible for rendering commands, shader bindings, and light
	 * computations. Implementations can use this context to issue rendering
	 * commands for different light types or configure the rendering pipeline as
	 * needed.
	 * </p>
	 *
	 * @param g The {@link Graphics} instance to be used by the light renderer.
	 *          Must not be null.
	 */
	void setGraphics(Graphics g);

	/**
	 * Renders a generic light source.
	 * <p>
	 * This method is a catch-all for rendering any light source that implements
	 * the {@link Light} interface. Specific rendering logic for the light type
	 * may be determined by the implementation.
	 * </p>
	 *
	 * @param light The light source to render. Must not be null.
	 */
	void render(Light light);

	/**
	 * Renders a spotlight.
	 * <p>
	 * This method is responsible for rendering a spotlight with specific
	 * directionality, cone angles, and attenuation effects. Spotlights are used
	 * to simulate focused beams of light, such as those from flashlights, lamps,
	 * or theater lighting.
	 * </p>
	 *
	 * @param light The spotlight to render. Must not be null.
	 */
	void render(SpotLight light);

	/**
	 * Renders a point light source.
	 * <p>
	 * This method handles the rendering of a point light, which emits light
	 * uniformly in all directions from a single point in 3D space. Point lights
	 * are commonly used to simulate small localized light sources such as light
	 * bulbs or torches.
	 * </p>
	 *
	 * @param light The point light source to render. Must not be null.
	 */
	void render(PointLight light);

	/**
	 * Renders a directional light source.
	 * <p>
	 * This method handles rendering for a directional light, which simulates
	 * light coming from a distant, uniform direction (e.g., sunlight or
	 * moonlight). Directional lights are ideal for simulating natural light
	 * sources that do not have an attenuation effect based on distance.
	 * </p>
	 *
	 * @param light The directional light source to render. Must not be null.
	 */
	void render(DirectionalLight light);

	/**
	 * Renders an ambient light source.
	 * <p>
	 * This method handles the rendering of ambient light, which provides uniform
	 * illumination across the entire scene without directionality or position.
	 * Ambient light is used to simulate indirect lighting and ensures that
	 * objects are visible even when not directly lit by other light sources.
	 * </p>
	 *
	 * @param light The ambient light source to render. Must not be null.
	 */
	void render(AmbientLight light);

}