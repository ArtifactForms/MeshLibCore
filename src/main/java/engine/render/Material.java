package engine.render;

import engine.resources.Texture;
import math.Color;

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

  /**
   * Controls whether depth testing is enabled for this material.
   *
   * <p>If depth testing is enabled, fragments will be depth-tested against the depth buffer and may
   * be discarded if they are occluded by previously rendered geometry.
   *
   * <p>If disabled, the geometry will be rendered without depth comparison, causing it to appear on
   * top of other objects regardless of depth. This is commonly used for editor gizmos, debug
   * visuals, UI overlays, and helper geometry.
   *
   * <p>Default value is {@code true}.
   */
  private boolean depthTest;

  /**
   * Represents the opacity (dissolve) of the material.
   *
   * <p>The opacity value determines how transparent or opaque the material is. It corresponds to
   * the "d" property in the MTL file format.
   *
   * <p>The opacity value ranges from 0.0 to 1.0:
   *
   * <ul>
   *   <li><strong>0.0</strong> - Fully transparent.
   *   <li><strong>1.0</strong> - Fully opaque (default).
   *   <li>Values between 0.0 and 1.0 represent varying levels of transparency.
   * </ul>
   *
   * <p>If an opacity map (map_d) is present, it will override this value, allowing for per-pixel
   * transparency control using a texture.
   *
   * <p>Example usage in MTL file:
   *
   * <pre>
   * d 0.8                  # 80% visible, 20% transparent
   * map_d opacity_map.png  # Opacity texture map
   * </pre>
   */
  private float opacity;

  /** The diffuse texture map (map_Kd) of the material. */
  private Texture diffuseTexture;

  /** The opacity texture map (map_d) of the material */
  private Texture opacityMap;

  private boolean receiveShadows = false;

  private boolean castShadows = true;

  /**
   * The shading mode used for rendering this material.
   *
   * <p>Shading defines how surface normals are evaluated during lighting calculations (e.g. flat or
   * smooth shading). This affects the visual appearance of the material without altering the
   * underlying mesh geometry.
   *
   * <p>The default shading mode is {@link Shading#FLAT}.
   */
  private Shading shading = Shading.FLAT;

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
    this.opacity = 1.0f; // Fully opaque by default
    this.depthTest = true; // Depth test enabled by default
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
   * Returns the current opacity of the material.
   *
   * <p>The opacity determines how transparent or opaque the material is. A value of 1.0 means the
   * material is fully opaque, while a value of 0.0 means it is fully transparent.
   *
   * @return the opacity value, ranging from 0.0 (fully transparent) to 1.0 (fully opaque)
   */
  public float getOpacity() {
    return opacity;
  }

  /**
   * Sets the opacity (dissolve) of the material.
   *
   * <p>The opacity value must be between 0.0 and 1.0, inclusive:
   *
   * <ul>
   *   <li><strong>0.0</strong> - Fully transparent.
   *   <li><strong>1.0</strong> - Fully opaque.
   *   <li>Values between 0.0 and 1.0 represent varying levels of transparency.
   * </ul>
   *
   * <p>If an opacity map (map_d) is present, this value will act as a multiplier for the map.
   *
   * @param opacity the opacity value to set, ranging from 0.0 to 1.0
   * @throws IllegalArgumentException if the opacity value is outside the range [0.0, 1.0]
   */
  public void setOpacity(float opacity) {
    if (opacity < 0 || opacity > 1) {
      throw new IllegalArgumentException("Opacity must be between 0 and 1 inclusive.");
    }
    this.opacity = opacity;
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

  /**
   * Returns the opacity texture map (map_d) of the material.
   *
   * @return The opacity texture map as {@link Texture}.
   */
  public Texture getOpacityMap() {
    return opacityMap;
  }

  /**
   * Sets the opacity texture map (map_d) of the material.
   *
   * @param opacityMap The new opacity texture map to set.
   */
  public void setOpacityMap(Texture opacityMap) {
    this.opacityMap = opacityMap;
  }

  /**
   * Returns whether depth testing is enabled for this material.
   *
   * @return {@code true} if depth testing is enabled, {@code false} otherwise
   */
  public boolean isDepthTest() {
    return depthTest;
  }

  /**
   * Enables or disables depth testing for this material.
   *
   * <p>This flag only describes the desired render state. The actual graphics API state change is
   * performed by the {@link workspace.ui.Graphics} implementation when the material is applied.
   *
   * @param depthTest {@code true} to enable depth testing, {@code false} to disable it
   */
  public void setDepthTest(boolean depthTest) {
    this.depthTest = depthTest;
  }

  public boolean isReceiveShadows() {
    return receiveShadows;
  }

  public void setReceiveShadows(boolean receiveShadows) {
    this.receiveShadows = receiveShadows;
  }

  public boolean isCastShadows() {
    return castShadows;
  }

  public void setCastShadows(boolean castShadows) {
    this.castShadows = castShadows;
  }

  /**
   * Returns the shading mode used by this material.
   *
   * @return the current shading mode
   */
  public Shading getShading() {
    return shading;
  }

  /**
   * Sets the shading mode for this material.
   *
   * <p>Changing the shading mode influences how lighting is computed for surfaces rendered with
   * this material, but does not modify mesh data such as vertex positions or normals.
   *
   * @param shading the shading mode to use
   * @throws IllegalArgumentException if {@code shading} is {@code null}
   */
  public void setShading(Shading shading) {
    if (shading == null) {
      throw new IllegalArgumentException("Shading cannot be null.");
    }
    this.shading = shading;
  }
}
