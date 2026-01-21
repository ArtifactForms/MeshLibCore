package engine.demos.skybox;

import engine.components.StaticGeometry;
import engine.render.Material;
import engine.resources.Resources;
import engine.resources.Texture;
import engine.resources.TextureManager;
import engine.scene.SceneNode;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreatorUV;

/**
 * The {@code SkyBox} class represents a 3D cube-shaped skybox used in a scene. It provides a
 * textured cube that surrounds the scene, simulating the appearance of a distant environment such
 * as the sky, clouds, or space.
 *
 * <p>The skybox is implemented as a {@link SceneNode} with static geometry and a texture applied to
 * the cube's faces.
 */
public class SkyBox extends SceneNode {

  private float radius;
  /**
   * Constructs a {@code SkyBox} with the specified radius and uses the default skybox texture.
   *
   * <p>This constructor is a convenience method that allows you to create a skybox with a given
   * radius without needing to specify a texture. It automatically applies the default skybox
   * texture.
   *
   * @param radius the radius of the skybox (half the size of its edge length)
   */
  public SkyBox(float radius) {
    this(radius, Resources.DEFAULT_SKY_BOX_TEXTURE);
  }

  /**
   * Constructs a {@code SkyBox} with the specified radius and texture.
   *
   * @param radius the radius of the skybox (half the size of its edge length)
   * @param texturePath the path to the texture to be applied to the skybox
   */
  public SkyBox(float radius, String texturePath) {
    this.radius = radius;
    createGeometry(texturePath);
  }

  /**
   * Creates the geometry and material for the skybox and adds it to this node.
   *
   * @param texturePath the path to the texture to be applied to the skybox
   */
  private void createGeometry(String texturePath) {
    Mesh3D mesh = new CubeCreatorUV(radius).create();
    StaticGeometry geometry = new StaticGeometry(mesh, createMaterial(texturePath));
    addComponent(geometry);
  }

  /**
   * Creates a material for the skybox with the specified texture. Lighting is disabled for the
   * material to ensure it is displayed as-is.
   *
   * @param texturePath the path to the texture to be applied to the skybox
   * @return the created {@link Material} instance
   */
  private Material createMaterial(String texturePath) {
    Texture texture = TextureManager.getInstance().loadTexture(texturePath);
    Material material = new Material();
    material.setUseLighting(false);
    material.setDiffuseTexture(texture);
    return material;
  }

  /**
   * Returns the radius of the skybox.
   *
   * @return the radius of the skybox
   */
  public float getRadius() {
    return radius;
  }
}
