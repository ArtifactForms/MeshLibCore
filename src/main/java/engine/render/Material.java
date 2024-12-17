package engine.render;

import math.Color;
import workspace.ui.Graphics;

/**
 * Represents a material with lighting and shader properties for rendering 3D
 * meshes.
 * <p>
 * A Material defines the visual appearance of a 3D object under lighting
 * conditions. It includes attributes such as base color, ambient, diffuse, and
 * specular light coefficients, and shininess, which together determine how
 * light interacts with the object's surface.
 * </p>
 * <p>
 * This class provides predefined materials for common use cases (e.g., default
 * white, metallic, glass) and allows custom material creation with specific
 * properties.
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
	public static final Material METALLIC_SILVER_MATERIAL = new Material(
	    new Color(0.75f, 0.75f, 0.75f),
	    new float[] { 0.2f, 0.2f, 0.2f },
	    new float[] { 1.0f, 1.0f, 1.0f },
	    new float[] { 1.0f, 1.0f, 1.0f },
	    50.0f);

	/**
	 * Metallic gold material with a shiny gold appearance.
	 */
	public static final Material GOLDEN_METALLIC_MATERIAL = new Material(
	    new Color(1.0f, 0.84f, 0.0f), 
	    new float[] { 0.3f, 0.3f, 0.3f },
	    new float[] { 1.0f, 0.84f, 0.0f },
	    new float[] { 1.0f, 1.0f, 0.5f },
	    100.0f);

	/**
	 * Clear glass material with a transparent appearance.
	 */
	public static final Material GLASS_CLEAR_MATERIAL = new Material(
	    new Color(0.7f, 0.9f, 1.0f), 
	    new float[] { 0.3f, 0.3f, 0.3f },
	    new float[] { 0.8f, 0.8f, 0.8f },
	    new float[] { 1.0f, 1.0f, 1.0f }, 5.0f);

	/**
	 * Stone grey material with a matte grey appearance.
	 */
	public static final Material STONE_GREY_MATERIAL = 
			new Material(
	    new Color(0.5f, 0.5f, 0.5f), 
	    new float[] { 0.4f, 0.4f, 0.4f },
	    new float[] { 0.6f, 0.6f, 0.6f }, 
	    new float[] { 0.2f, 0.2f, 0.2f },
	    10.0f);

	/**
	 * Water material with a reflective blue appearance.
	 */
	public static final Material WATER_MATERIAL = new Material(
	    new Color(0.0f, 0.5f, 1.0f), 
	    new float[] { 0.1f, 0.3f, 0.5f },
	    new float[] { 0.3f, 0.5f, 0.7f }, 
	    new float[] { 0.2f, 0.2f, 0.6f }, 2.0f);

	/**
	 * Base color for the material.
	 */
	private Color color; 

	/**
	 * Ambient light coefficient (R, G, B).
	 */
	private float[] ambient;

	/**
	 * Diffuse light coefficient (R, G, B).
	 */
	private float[] diffuse;

	/**
	 *  Specular light coefficient (R, G, B).
	 */
	private float[] specular;

	/**
	 * Shininess factor for specular highlights.
	 */
	private float shininess;
	
	/**
	 * Private constructor for creating a material with specific properties.
	 *
	 * @param color     The base color of the material.
	 * @param ambient   The ambient light coefficient (R, G, B).
	 * @param diffuse   The diffuse light coefficient (R, G, B).
	 * @param specular  The specular light coefficient (R, G, B).
	 * @param shininess The shininess factor for specular highlights.
	 */
	private Material(Color color, float[] ambient, float[] diffuse,
	    float[] specular, float shininess) {
		this.color = color;
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.shininess = shininess;
	}

	/**
	 * Default constructor initializes the material with default lighting
	 * properties.
	 */
	public Material() {
		this.color = new Color(1, 1, 1);
		this.ambient = new float[] { 0.2f, 0.2f, 0.2f };
		this.diffuse = new float[] { 1.0f, 1.0f, 1.0f };
		this.specular = new float[] { 1.0f, 1.0f, 1.0f };
		this.shininess = 10.0f;
	}

	/**
	 * Constructor to set the base color of the material.
	 *
	 * @param color The base color of the material.
	 */
	public Material(Color color) {
		this();
		this.color = color;
	}

	/**
	 * Applies this material's properties to the given rendering context.
	 *
	 * @param g The {@link Graphics} instance to which the material will be
	 *          applied.
	 */
	public void apply(Graphics g) {
		g.setMaterial(this);
	}

	/**
	 * Releases the material or resets properties in the rendering context. Useful
	 * for unbinding shaders or clearing material-specific settings.
	 *
	 * @param g The {@link Graphics} instance from which the material is released.
	 */
	public void release(Graphics g) {
		// Logic to unbind shader or reset material-related properties
	}

	/**
	 * Gets the base color of the material.
	 *
	 * @return The base {@link Color} of the material.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Gets the ambient light coefficient of the material.
	 *
	 * @return An array representing the ambient light coefficient (R, G, B).
	 */
	public float[] getAmbient() {
		return ambient;
	}

	/**
	 * Gets the diffuse light coefficient of the material.
	 *
	 * @return An array representing the diffuse light coefficient (R, G, B).
	 */
	public float[] getDiffuse() {
		return diffuse;
	}

	/**
	 * Gets the specular light coefficient of the material.
	 *
	 * @return An array representing the specular light coefficient (R, G, B).
	 */
	public float[] getSpecular() {
		return specular;
	}

	/**
	 * Gets the shininess factor of the material.
	 *
	 * @return The shininess factor for specular highlights.
	 */
	public float getShininess() {
		return shininess;
	}
	
}