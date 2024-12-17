package engine.render;

import math.Color;

/**
 * Factory class for creating predefined Material instances using a builder
 * pattern.
 * <p>
 * This class provides various predefined material configurations, such as
 * metallic silver, golden metallic, clear glass, stone grey, and water effects.
 * Each material can be created through the builder pattern to encapsulate
 * lighting properties like ambient, diffuse, specular colors, and shininess
 * levels.
 * </p>
 * <p>
 * The goal of these predefined materials is to simplify common rendering use
 * cases, such as simulating metals, glass, stone, and water effects, by
 * abstracting their creation and configuration logic.
 * </p>
 * <p>
 * Each static method in this class returns a fully built Material object, ready
 * for use in rendering pipelines or graphics engines.
 * </p>
 */
public class MaterialFactory {

	/**
	 * Creates a metallic silver material with preset properties.
	 * <p>
	 * This material has a diffuse color representing silver, with ambient,
	 * diffuse, and specular properties tuned for a metallic effect.
	 * </p>
	 * 
	 * @return A {@link Material} instance configured as metallic silver.
	 */
	public static Material createMetallicSilver() {
		return new Material.Builder()
				.setColor(new Color(0.75f, 0.75f, 0.75f))
		    .setAmbient(new float[] { 0.2f, 0.2f, 0.2f })
		    .setDiffuse(new float[] { 1.0f, 1.0f, 1.0f })
		    .setSpecular(new float[] { 1.0f, 1.0f, 1.0f })
		    .setShininess(50.0f)
		    .build();
	}

	/**
	 * Creates a golden metallic material with preset properties.
	 * <p>
	 * This material mimics the reflective, shiny properties of gold.
	 * </p>
	 * 
	 * @return A {@link Material} instance configured as golden metallic.
	 */
	public static Material createGoldenMetallic() {
		return new Material.Builder()
				.setColor(new Color(1.0f, 0.84f, 0.0f))
		    .setAmbient(new float[] { 0.3f, 0.3f, 0.3f })
		    .setDiffuse(new float[] { 1.0f, 0.84f, 0.0f })
		    .setSpecular(new float[] { 1.0f, 1.0f, 0.5f })
		    .setShininess(100.0f)
		    .build();
	}

	/**
	 * Creates a clear glass-like material with preset properties.
	 * <p>
	 * This material simulates the transparency and reflection characteristics of
	 * clear glass.
	 * </p>
	 * 
	 * @return A {@link Material} instance configured as clear glass.
	 */
	public static Material createGlassClear() {
		return new Material.Builder()
				.setColor(new Color(0.7f, 0.9f, 1.0f))
		    .setAmbient(new float[] { 0.3f, 0.3f, 0.3f })
		    .setDiffuse(new float[] { 0.8f, 0.8f, 0.8f })
		    .setSpecular(new float[] { 1.0f, 1.0f, 1.0f })
		    .setShininess(5.0f)
		    .build();
	}

	/**
	 * Creates a stone grey material with preset properties.
	 * <p>
	 * This material has a matte stone-like appearance, suitable for terrain,
	 * rocky surfaces, or architectural models.
	 * </p>
	 * 
	 * @return A {@link Material} instance configured as stone grey.
	 */
	public static Material createStoneGrey() {
		return new Material.Builder()
				.setColor(new Color(0.5f, 0.5f, 0.5f))
		    .setAmbient(new float[] { 0.4f, 0.4f, 0.4f })
		    .setDiffuse(new float[] { 0.6f, 0.6f, 0.6f })
		    .setSpecular(new float[] { 0.2f, 0.2f, 0.2f })
		    .setShininess(10.0f)
		    .build();
	}

	/**
	 * Creates a water-like material with preset properties.
	 * <p>
	 * This material simulates the transparency and light-reflective properties of
	 * water, incorporating a mix of blue and subtle reflective colors.
	 * </p>
	 * 
	 * @return A {@link Material} instance configured as water.
	 */
	public static Material createWater() {
		return new Material.Builder()
				.setColor(new Color(0.0f, 0.5f, 1.0f))
		    .setAmbient(new float[] { 0.1f, 0.3f, 0.5f })
		    .setDiffuse(new float[] { 0.3f, 0.5f, 0.7f })
		    .setSpecular(new float[] { 0.2f, 0.2f, 0.6f })
		    .setShininess(2.0f)
		    .build();
	}
	
}
