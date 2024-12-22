package engine.render;

import math.Color;
import workspace.ui.Graphics;

/**
 * Represents a material with lighting and shader properties for 3D rendering.
 * <p>
 * A Material defines how a 3D object interacts with light, determining its
 * visual appearance under various lighting conditions. It encapsulates
 * attributes such as ambient, diffuse, and specular light coefficients,
 * shininess, and base color, which collectively define how light reflects off
 * the object's surface.
 * </p>
 * <p>
 * This class includes predefined materials for common use cases (e.g., default
 * white, black, or other colors) and supports the creation of custom materials
 * with specific properties through the use of {@link Builder}.
 * </p>
 * <p>
 * The material properties can control effects like reflectivity, surface
 * roughness, and color, enabling diverse visual representations for 3D meshes
 * in a rendering engine.
 * </p>
 */
public class Material {

	/**
	 * Default white material with a base white color and standard light
	 * coefficients.
	 */
	public static final Material DEFAULT_WHITE = new Material(Color.WHITE);

	/**
	 * Default black material with a base black color and standard light
	 * coefficients.
	 */
	public static final Material DEFAULT_BLACK = new Material(Color.BLACK);

	/**
	 * Default red material with a base red color and standard light coefficients.
	 */
	public static final Material DEFAULT_RED = new Material(Color.RED);

	/**
	 * Default green material with a base green color and standard light
	 * coefficients.
	 */
	public static final Material DEFAULT_GREEN = new Material(Color.GREEN);

	/**
	 * Default blue material with a base blue color and standard light
	 * coefficients.
	 */
	public static final Material DEFAULT_BLUE = new Material(Color.BLUE);

	/**
	 * Metallic silver material with a shiny silver appearance.
	 */
	public static final Material METALLIC_SILVER_MATERIAL = MaterialFactory
	    .createMetallicSilver();

	/**
	 * Metallic gold material with a shiny gold appearance.
	 */
	public static final Material GOLDEN_METALLIC_MATERIAL = MaterialFactory
	    .createGoldenMetallic();

	/**
	 * Clear glass material with a transparent appearance.
	 */
	public static final Material GLASS_CLEAR_MATERIAL = MaterialFactory
	    .createGlassClear();

	/**
	 * Stone grey material with a matte grey appearance.
	 */
	public static final Material STONE_GREY_MATERIAL = MaterialFactory
	    .createStoneGrey();

	/**
	 * Water material with a reflective blue appearance.
	 */
	public static final Material WATER_MATERIAL = MaterialFactory.createWater();

	private boolean useLighting;

	/**
	 * Base color for the material.
	 */
	private final Color color;

	/**
	 * Ambient light coefficient (R, G, B).
	 */
	private final float[] ambient;

	/**
	 * Diffuse light coefficient (R, G, B).
	 */
	private final float[] diffuse;

	/**
	 * Specular light coefficient (R, G, B).
	 */
	private final float[] specular;

	/**
	 * Shininess factor for specular highlights.
	 */
	private final float shininess;

	/**
	 * Constructor to set the base color of the material.
	 *
	 * @param color The base color of the material.
	 */
	public Material(Color color) {
		this(new Builder().setColor(color));
	}

	private Material(Builder builder) {
		this.useLighting = builder.useLighting;
		this.color = builder.color;
		this.ambient = builder.ambient;
		this.diffuse = builder.diffuse;
		this.specular = builder.specular;
		this.shininess = builder.shininess;
	}

	/**
	 * Builder class to facilitate the creation of custom materials with specific
	 * lighting and shader properties.
	 */
	public static class Builder {

		private boolean useLighting = true;

		private Color color = new Color(1, 1, 1); // Default color is white

		private float[] ambient = new float[] { 0.2f, 0.2f, 0.2f };

		private float[] diffuse = new float[] { 1.0f, 1.0f, 1.0f };

		private float[] specular = new float[] { 1.0f, 1.0f, 1.0f };

		private float shininess = 10.0f;

		/**
		 * Sets the base color of the material.
		 *
		 * @param color The desired base color.
		 * @return The builder instance for chaining.
		 */
		public Builder setColor(Color color) {
			this.color = color;
			return this;
		}

		/**
		 * Sets the ambient light coefficient of the material.
		 *
		 * @param ambient The desired ambient light coefficient (R, G, B).
		 * @return The builder instance for chaining.
		 */
		public Builder setAmbient(float[] ambient) {
			this.ambient = ambient;
			return this;
		}

		/**
		 * Sets the diffuse light coefficient of the material.
		 *
		 * @param diffuse The desired diffuse light coefficient (R, G, B).
		 * @return The builder instance for chaining.
		 */
		public Builder setDiffuse(float[] diffuse) {
			this.diffuse = diffuse;
			return this;
		}

		/**
		 * Sets the specular light coefficient of the material.
		 *
		 * @param specular The desired specular light coefficient (R, G, B).
		 * @return The builder instance for chaining.
		 */
		public Builder setSpecular(float[] specular) {
			this.specular = specular;
			return this;
		}

		/**
		 * Sets the shininess value of the material.
		 *
		 * @param shininess The shininess factor for specular highlights.
		 * @return The builder instance for chaining.
		 */
		public Builder setShininess(float shininess) {
			this.shininess = shininess;
			return this;
		}

		public Builder setUseLighting(boolean useLighting) {
			this.useLighting = useLighting;
			return this;
		}

		/**
		 * Builds and returns the Material instance with the set properties.
		 *
		 * @return A new instance of {@link Material}.
		 */
		public Material build() {
			return new Material(this);
		}
	}

	/**
	 * Applies this material's properties to the provided rendering context.
	 *
	 * @param g The {@link Graphics} instance to apply this material to.
	 */
	public void apply(Graphics g) {
		g.setMaterial(this);
	}

	/**
	 * Releases this material's properties from the rendering context, useful for
	 * cleaning up shaders or material-specific settings.
	 *
	 * @param g The {@link Graphics} instance from which this material will be
	 *          unbound.
	 */
	public void release(Graphics g) {
		// Logic for releasing or resetting rendering context goes here
	}

	public boolean isUseLighting() {
		return useLighting;
	}

	/**
	 * Retrieves the base color of the material.
	 *
	 * @return The {@link Color} representing the base color of the material.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Retrieves the ambient light coefficient of the material.
	 *
	 * @return An array representing the ambient light coefficient (R, G, B).
	 */
	public float[] getAmbient() {
		return ambient;
	}

	/**
	 * Retrieves the diffuse light coefficient of the material.
	 *
	 * @return An array representing the diffuse light coefficient (R, G, B).
	 */
	public float[] getDiffuse() {
		return diffuse;
	}

	/**
	 * Retrieves the specular light coefficient of the material.
	 *
	 * @return An array representing the specular light coefficient (R, G, B).
	 */
	public float[] getSpecular() {
		return specular;
	}

	/**
	 * Retrieves the shininess factor of the material.
	 *
	 * @return The shininess factor that controls specular highlights.
	 */
	public float getShininess() {
		return shininess;
	}

}