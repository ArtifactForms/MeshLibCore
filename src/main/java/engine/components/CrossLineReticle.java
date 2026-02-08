/**
 * The CrossLineReticle class represents a visual reticle consisting of cross lines rendered on a
 * plane. It is designed to be part of a 3D scene and implements the {@link RenderableComponent}
 * interface for rendering capabilities.
 *
 * <p>The reticle is created as a textured plane using a {@link Mesh3D} and is configurable with
 * parameters like radius, thickness, and color. The texture is generated dynamically.
 */
package engine.components;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureManager;
import math.Color;
import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreatorUV;
import mesh.modifier.transform.RotateXModifier;
import workspace.ui.Graphics;

public class CrossLineReticle extends AbstractComponent implements RenderableComponent {

  /** The radius of the reticle, defining its size. */
  private int radius;

  /** The thickness of the cross lines. */
  private int thickness;

  /** The color of the cross lines. */
  private Color color;

  /** The mesh used to represent the plane of the reticle. */
  private Mesh3D mesh;

  /** The texture used for rendering the reticle. */
  private Texture texture;

  /** Creates a default CrossLineReticle with a radius of 9, thickness of 2, and white color. */
  public CrossLineReticle() {
    this(9, 2, Color.WHITE);
  }

  /**
   * Creates a CrossLineReticle with the specified radius, thickness, and color.
   *
   * @param radius The radius of the reticle.
   * @param thickness The thickness of the cross lines.
   * @param color The color of the cross lines.
   */
  public CrossLineReticle(int radius, int thickness, Color color) {
    this.radius = radius;
    this.color = color;
    this.thickness = thickness;
    this.mesh = new PlaneCreatorUV(radius).create();
    this.mesh.apply(new RotateXModifier(-Mathf.HALF_PI));
    this.texture = createTexture();
  }

  /**
   * Renders the reticle onto the provided {@link Graphics} context.
   *
   * @param g The graphics context used for rendering.
   */
  @Override
  public void render(Graphics g) {
    float centerX = g.getWidth() / 2.0f;
    float centerY = g.getHeight() / 2.0f;
    g.pushMatrix();
    g.translate(centerX, centerY);
    g.bindTexture(texture, 0);
    g.fillFaces(mesh);
    g.unbindTexture(0);
    g.popMatrix();
  }

  /**
   * Creates a {@link Texture} for the reticle using a dynamically generated {@link BufferedImage}.
   *
   * @return The generated texture.
   */
  private Texture createTexture() {
    BufferedImage image = createTextureImage();
    Texture texture = TextureManager.getInstance().createTexture(image);
    texture.setFilterMode(FilterMode.POINT);
    return texture;
  }

  /**
   * Generates a {@link BufferedImage} containing the cross lines of the reticle.
   *
   * @return The generated image.
   */
  private BufferedImage createTextureImage() {
    int size = radius + radius;
    BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = (Graphics2D) image.getGraphics();
    g2d.setColor(new java.awt.Color(this.color.getRGBA()));
    g2d.fillRect(radius - (thickness / 2), 0, thickness, size);
    g2d.fillRect(0, radius - (thickness / 2), size, thickness);
    return image;
  }

  /**
   * Called during each update cycle. This reticle does not require updates.
   *
   * @param tpf The time per frame in seconds.
   */
  @Override
  public void onUpdate(float tpf) {}

  /** Called when the component is attached to a {@link engine.SceneNode}. */
  @Override
  public void onAttach() {}

  /** Called when the component is detached from a {@link engine.SceneNode}. */
  @Override
  public void onDetach() {}
}
