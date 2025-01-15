package engine.render;

import engine.resources.Texture;
import math.Color;
import workspace.ui.Graphics;

/**
 * Represents a material with lighting and shader properties for 3D rendering.
 *
 * <p>A Material defines how a 3D object interacts with light, determining its visual appearance
 * under various lighting conditions. It encapsulates attributes such as ambient, diffuse, and
 * specular light coefficients, shininess, and base color, which collectively define how light
 * reflects off the object's surface.
 *
 * <p>This class includes predefined materials for common use cases (e.g., default white, black, or
 * other colors) and supports the creation of custom materials with specific properties.
 *
 * <p>The material properties can control effects like reflectivity, surface roughness, and color,
 * enabling diverse visual representations for 3D meshes in a rendering engine.
 */
public class Material {

  /** Default white material with a base white color and standard light coefficients. */
  public static final Material DEFAULT_WHITE = new Material(Color.WHITE);

  /** Default black material with a base black color and standard light coefficients. */
  public static final Material DEFAULT_BLACK = new Material(Color.BLACK);

  /** Default red material with a base red color and standard light coefficients. */
  public static final Material DEFAULT_RED = new Material(Color.RED);

  /** Default green material with a base green color and standard light coefficients. */
  public static final Material DEFAULT_GREEN = new Material(Color.GREEN);

  /** Default blue material with a base blue color and standard light coefficients. */
  public static final Material DEFAULT_BLUE = new Material(Color.BLUE);

  /** Metallic silver material with a shiny silver appearance. */
  public static final Material METALLIC_SILVER_MATERIAL = MaterialFactory.createMetallicSilver();

  /** Metallic gold material with a shiny gold appearance. */
  public static final Material GOLDEN_METALLIC_MATERIAL = MaterialFactory.createGoldenMetallic();

  /** Clear glass material with a transparent appearance. */
  public static final Material GLASS_CLEAR_MATERIAL = MaterialFactory.createGlassClear();

  /** Stone grey material with a matte grey appearance. */
  public static final Material STONE_GREY_MATERIAL = MaterialFactory.createStoneGrey();

  /** Water material with a reflective blue appearance. */
  public static final Material WATER_MATERIAL = MaterialFactory.createWater();

  /** Indicates whether this material should use lighting effects during rendering. */
  private boolean useLighting;

  /**
   * The name of the material, used to identify and reference the material in rendering pipelines
   * and material libraries.
   *
   * <p>This name is often defined in material definition files (e.g., MTL files for OBJ models) and
   * is used to associate textures and shading properties with specific parts of a 3D model. Having
   * a descriptive name can help streamline asset management and debugging within a rendering
   * engine.
   */
  private String name;

  /** Base color for the material. */
  private Color color;

  /** Ambient light coefficient (R, G, B), controlling the material's reaction to ambient light. */
  private float[] ambient;

  /** Diffuse light coefficient (R, G, B), controlling how the material reflects diffuse light. */
  private float[] diffuse;

  /**
   * Specular light coefficient (R, G, B), controlling the shininess and reflections on the surface.
   */
  private float[] specular;

  /**
   * Shininess factor for specular highlights, controlling the size and intensity of reflections.
   */
  private float shininess;

  /** The diffuse texture map (map_Kd) of the material. */
  private Texture diffuseTexture;

  /** Default constructor that initializes the material with a base white color. */
  public Material() {
    this(Color.WHITE);
  }

  /**
   * Constructor to set the base color of the material.
   *
   * @param color The base color of the material.
   * @throws IllegalArgumentException If the color is null.
   */
  public Material(Color color) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null.");
    }
    this.color = color;
    this.name = "";
    this.useLighting = true;
    this.ambient = new float[] {0.2f, 0.2f, 0.2f};
    this.diffuse = new float[] {1.0f, 1.0f, 1.0f};
    this.specular = new float[] {1.0f, 1.0f, 1.0f};
    this.shininess = 10.0f;
  }

  /**
   * Applies this material's properties to the provided rendering context.
   *
   * <p>This method sets the material's color, lighting properties, and binds any associated
   * textures to the rendering context.
   *
   * @param g The {@link Graphics} instance to apply this material to.
   */
  public void apply(Graphics g) {
    g.setMaterial(this);

    if (diffuseTexture != null) {
      g.bindTexture(diffuseTexture, 0); // Bind to texture unit 0
    }
  }

  /**
   * Releases this material's properties from the rendering context, useful for cleaning up shaders
   * or material-specific settings.
   *
   * <p>This method unbinds textures and resets any material properties set during the rendering
   * process to ensure the rendering context is restored to a neutral state.
   *
   * @param g The {@link Graphics} instance from which this material will be unbound.
   */
  public void release(Graphics g) {
    // Logic for releasing or resetting rendering context goes here
  }

  /**
   * Checks whether lighting effects are enabled for this material.
   *
   * @return {@code true} if lighting is used; {@code false} otherwise.
   */
  public boolean isUseLighting() {
    return useLighting;
  }

  /**
   * Sets whether this material should use lighting effects during rendering.
   *
   * @param useLighting {@code true} to enable lighting; {@code false} to disable.
   */
  public void setUseLighting(boolean useLighting) {
    this.useLighting = useLighting;
  }

  /**
   * Returns the name of the material.
   *
   * @return The name of the material as a String.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the material.
   *
   * @param name The new name of the material.
   * @throws IllegalArgumentException If the name is null.
   */
  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Material name cannot be null.");
    }
    this.name = name;
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
   * Sets the base color of the material.
   *
   * @param color The new base color to set.
   * @throws IllegalArgumentException If color is null.
   */
  public void setColor(Color color) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null.");
    }
    this.color = color;
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
   * Sets the ambient light coefficient of the material.
   *
   * @param ambient An array representing the new ambient light coefficient (R, G, B).
   */
  public void setAmbient(float[] ambient) {
    this.ambient = ambient;
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
   * Sets the diffuse light coefficient of the material.
   *
   * @param diffuse An array representing the new diffuse light coefficient (R, G, B).
   */
  public void setDiffuse(float[] diffuse) {
    this.diffuse = diffuse;
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
   * Sets the specular light coefficient of the material.
   *
   * @param specular An array representing the new specular light coefficient (R, G, B).
   */
  public void setSpecular(float[] specular) {
    this.specular = specular;
  }

  /**
   * Retrieves the shininess factor of the material.
   *
   * @return The shininess factor that controls specular highlights.
   */
  public float getShininess() {
    return shininess;
  }

  /**
   * Sets the shininess factor of the material.
   *
   * @param shininess The new shininess factor to set.
   */
  public void setShininess(float shininess) {
    this.shininess = shininess;
  }

  /**
   * Returns the diffuse texture map (map_Kd) of the material.
   *
   * @return The diffuse texture map as {@link Texture}.
   */
  public Texture getDiffuseTexture() {
    return diffuseTexture;
  }

  /**
   * Sets the diffuse texture map (map_Kd) of the material.
   *
   * @param diffuseTexture The new diffuse texture map to set.
   */
  public void setDiffuseTexture(Texture diffuseTexture) {
    this.diffuseTexture = diffuseTexture;
  }
}
