package engine.scene.light;

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

}