package engine.gfx;

import engine.components.Geometry;
import engine.render.Material;
import engine.scene.SceneNode;
import math.Color;
import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.transform.RotateModifier;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TransformAxis;
import mesh.modifier.transform.TranslateModifier;

/**
 * {@code Sprite} is a small builder-style helper for creating a textured quad (billboard-style
 * plane) from a {@link TextureAtlas}.
 *
 * <p>A sprite represents a <strong>single static tile</strong> from an atlas and produces a {@link
 * SceneNode} containing:
 *
 * <ul>
 *   <li>a quad mesh oriented in the XY plane
 *   <li>a {@link Material} with the atlas texture applied
 *   <li>UV coordinates pointing to one atlas tile
 * </ul>
 *
 * <h2>Coordinate System</h2>
 *
 * <p>By default, tile coordinates use a bottom-left origin ({@link TextureAtlas#getUV(int, int)}).
 *
 * <p>If your atlas is authored with a top-left origin (common for pixel art and 2D tools), use
 * {@link #tileTopLeft(int, int)} instead.
 *
 * <p>This makes sprites easy to place in world space using standard transforms without additional
 * offsets.
 *
 * <h2>Design Notes</h2>
 *
 * <ul>
 *   <li>This class does not manage animation (see {@link AnimatedSprite})
 *   <li>No scene or update logic is included
 *   <li>Intended as a low-level engine utility
 * </ul>
 */
public final class Sprite {

  private final TextureAtlas atlas;
  private int row;
  private int col;
  private float width = 1f;
  private float height = 1f;
  private boolean topLeft = false;

  /**
   * Creates a new sprite builder for the given texture atlas.
   *
   * @param atlas the texture atlas to sample from
   */
  public Sprite(TextureAtlas atlas) {
    this.atlas = atlas;
  }

  /**
   * Selects a tile using bottom-left atlas coordinates.
   *
   * @param row tile row (bottom-left origin)
   * @param col tile column
   * @return this instance for chaining
   */
  public Sprite tile(int row, int col) {
    this.row = row;
    this.col = col;
    this.topLeft = false;
    return this;
  }

  /**
   * Selects a tile using top-left atlas coordinates.
   *
   * <p>This is a convenience wrapper around {@link TextureAtlas#getUVTopLeft(int, int)}.
   *
   * @param row tile row (top-left origin)
   * @param col tile column
   * @return this instance for chaining
   */
  public Sprite tileTopLeft(int row, int col) {
    this.row = row;
    this.col = col;
    this.topLeft = true;
    return this;
  }

  /**
   * Sets a uniform width and height for the sprite.
   *
   * @param size sprite size in world units
   * @return this instance for chaining
   */
  public Sprite size(float size) {
    this.width = size;
    this.height = size;
    return this;
  }

  /**
   * Sets a non-uniform width and height for the sprite.
   *
   * @param width sprite width in world units
   * @param height sprite height in world units
   * @return this instance for chaining
   */
  public Sprite size(float width, float height) {
    this.width = width;
    this.height = height;
    return this;
  }

  /**
   * Builds the sprite and returns it as a {@link SceneNode}.
   *
   * <p>The resulting node contains a {@link Geometry} component with:
   *
   * <ul>
   *   <li>a quad mesh
   *   <li>UVs mapped to the selected atlas tile
   *   <li>a default white material using the atlas texture
   * </ul>
   *
   * @return a scene node representing the sprite
   */
  public SceneNode build() {
    UVRect uv = topLeft ? atlas.getUVTopLeft(row, col) : atlas.getUV(row, col);

    Mesh3D mesh = new PlaneCreator(0.5f).create();

    new RotateModifier(Mathf.HALF_PI, TransformAxis.Y).modify(mesh);
    new RotateModifier(Mathf.HALF_PI, TransformAxis.X).modify(mesh);
    new ScaleModifier(width, height, 1).modify(mesh);
    new TranslateModifier(width * 0.5f, height * 0.5f, 0).modify(mesh);

    mesh.addUvCoordinate(uv.uMin, uv.vMin);
    mesh.addUvCoordinate(uv.uMax, uv.vMin);
    mesh.addUvCoordinate(uv.uMax, uv.vMax);
    mesh.addUvCoordinate(uv.uMin, uv.vMax);

    mesh.getFaceAt(mesh.getFaceCount() - 1).setUvIndices(0, 1, 2, 3);

    Material material = new Material(Color.WHITE);
    material.setDiffuseTexture(atlas.getTexture());

    return new SceneNode("Sprite", new Geometry(mesh, material));
  }

  /**
   * Convenience factory method.
   *
   * @param atlas the texture atlas to sample from
   * @return a new sprite builder
   */
  public static Sprite from(TextureAtlas atlas) {
    return new Sprite(atlas);
  }
}
