package engine.gfx;

import engine.components.Geometry;
import engine.scene.SceneNode;
import mesh.Mesh3D;

/**
 * {@code AnimatedSprite} is a lightweight helper for building and updating frame-based sprite
 * animations using a {@link TextureAtlas}.
 *
 * <p>An animated sprite is composed of:
 *
 * <ul>
 *   <li>a {@link Sprite} for geometry + material setup
 *   <li>a sequence of {@link Frame} definitions (row/column indices)
 *   <li>a fixed per-frame duration
 * </ul>
 *
 * <p>The animation is driven externally by calling {@link #update(float)} once per frame (typically
 * from a game or scene update loop).
 *
 * <h2>Coordinate System</h2>
 *
 * <p>By default, frames use the atlas' bottom-left origin ({@link TextureAtlas#getUV(int, int)}).
 *
 * <p>If your atlas uses a top-left origin (common in 2D tools and pixel art), enable {@link
 * #topLeft(boolean)} before calling {@link #build()}.
 *
 * <h2>Lifecycle</h2>
 *
 * <ol>
 *   <li>Configure size, frames, timing, and looping
 *   <li>Call {@link #build()} once to create the scene node
 *   <li>Call {@link #update(float)} every frame to advance animation
 * </ol>
 *
 * <p>This class intentionally:
 *
 * <ul>
 *   <li>Does not manage scene attachment
 *   <li>Does not own a clock or timing source
 *   <li>Does not handle sprite orientation or transforms
 * </ul>
 *
 * <p>It is designed to be simple, predictable, and easy to integrate into higher-level animation or
 * component systems.
 */
public final class AnimatedSprite {

  private final TextureAtlas atlas;

  private Frame[] frames = new Frame[0];
  private float frameDuration = 0.1f;
  private boolean loop = true;
  private boolean topLeft = false;

  private int frameIndex = 0;
  private float accumulator = 0f;

  private Sprite sprite;
  private Mesh3D mesh;

  private float width = 1f;
  private float height = 1f;

  private AnimatedSprite(TextureAtlas atlas) {
    this.atlas = atlas;
  }

  /**
   * Creates a new {@code AnimatedSprite} builder for the given atlas.
   *
   * @param atlas the texture atlas used for all frames
   * @return a new animated sprite instance
   */
  public static AnimatedSprite from(TextureAtlas atlas) {
    return new AnimatedSprite(atlas);
  }

  /**
   * Sets a uniform width and height for the sprite geometry.
   *
   * @param size sprite size in world units
   * @return this instance for chaining
   */
  public AnimatedSprite size(float size) {
    this.width = size;
    this.height = size;
    return this;
  }

  /**
   * Sets a non-uniform width and height for the sprite geometry.
   *
   * @param width sprite width in world units
   * @param height sprite height in world units
   * @return this instance for chaining
   */
  public AnimatedSprite size(float width, float height) {
    this.width = width;
    this.height = height;
    return this;
  }

  /**
   * Defines the animation frame sequence.
   *
   * @param frames ordered list of frames to play
   * @return this instance for chaining
   */
  public AnimatedSprite frames(Frame... frames) {
    this.frames = frames;
    return this;
  }

  /**
   * Sets the duration of each frame.
   *
   * @param seconds time per frame in seconds
   * @return this instance for chaining
   */
  public AnimatedSprite frameDuration(float seconds) {
    this.frameDuration = seconds;
    return this;
  }

  /**
   * Enables or disables looping.
   *
   * @param loop {@code true} to loop the animation
   * @return this instance for chaining
   */
  public AnimatedSprite loop(boolean loop) {
    this.loop = loop;
    return this;
  }

  /**
   * Sets whether frame coordinates are interpreted using a top-left origin.
   *
   * <p>This must be set before calling {@link #build()}.
   *
   * @param topLeft {@code true} to use top-left atlas coordinates
   * @return this instance for chaining
   */
  public AnimatedSprite topLeft(boolean topLeft) {
    this.topLeft = topLeft;
    return this;
  }

  /**
   * Builds the animated sprite and returns the corresponding scene node.
   *
   * <p>This method:
   *
   * <ul>
   *   <li>Creates the underlying {@link Sprite}
   *   <li>Initializes geometry and UVs
   *   <li>Applies the first animation frame
   * </ul>
   *
   * @return a scene node containing the animated sprite geometry
   * @throws IllegalStateException if no frames were defined
   */
  public SceneNode build() {
    if (frames.length == 0) {
      throw new IllegalStateException("AnimatedSprite requires at least one frame");
    }

    sprite = Sprite.from(atlas).size(width, height);

    if (topLeft) {
      sprite.tileTopLeft(frames[0].row(), frames[0].col());
    } else {
      sprite.tile(frames[0].row(), frames[0].col());
    }

    SceneNode node = sprite.build();

    Geometry geo = node.getComponent(Geometry.class);
    this.mesh = geo.getMesh();

    applyFrame(0);
    return node;
  }

  /**
   * Advances the animation based on elapsed time.
   *
   * @param deltaSeconds time since last update in seconds
   */
  public void update(float deltaSeconds) {
    if (frames.length <= 1) return;

    accumulator += deltaSeconds;

    while (accumulator >= frameDuration) {
      accumulator -= frameDuration;
      advanceFrame();
    }
  }

  private void advanceFrame() {
    frameIndex++;

    if (frameIndex >= frames.length) {
      frameIndex = loop ? 0 : frames.length - 1;
    }

    applyFrame(frameIndex);
  }

  private void applyFrame(int index) {
    Frame f = frames[index];
    UVRect uv = topLeft ? atlas.getUVTopLeft(f.row(), f.col()) : atlas.getUV(f.row(), f.col());

    mesh.setUvCoordinate(0, uv.uMin, uv.vMin);
    mesh.setUvCoordinate(1, uv.uMax, uv.vMin);
    mesh.setUvCoordinate(2, uv.uMax, uv.vMax);
    mesh.setUvCoordinate(3, uv.uMin, uv.vMax);
  }
}
