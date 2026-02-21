package engine.components;

import engine.render.Graphics;
import math.Color;

/**
 * CinematicBlackBarsRenderer is responsible for rendering cinematic black bars on the top and
 * bottom of the screen. It supports smooth fade-in and fade-out animations for cinematic effects
 * during transitions.
 *
 * <p>This component renders black bars with configurable speed and handles dynamic animations like
 * appearing or disappearing smoothly over time. The fade effects are controlled by the speed of
 * transition and a constant bar size (in pixels).
 */
public class CinematicBlackBarsRenderer extends AbstractComponent implements RenderableComponent {

  /** Default height (in pixels) of the cinematic bars at full fade-in. */
  private static final int DEFAULT_TARGET_BAR_HEIGHT = 200;

  /** Default speed at which the fade-in and fade-out effects occur (in pixels per second). */
  private static final float DEFAULT_FADE_SPEED = 70.0f;

  /** Indicates whether the black bars are currently fading in. */
  private boolean fadingIn;

  /** Indicates whether the black bars are currently fading out. */
  private boolean fadingOut;

  /** Current visual size of the black bars (interpolated over time). */
  private float currentSize;

  /** The speed at which fade animations are applied (in pixels/second). */
  private float fadeSpeed;

  /** The target bar height at full fade-in (in pixels). */
  private float targetBarHeight;

  /**
   * Default constructor for initializing the renderer instance.
   *
   * <p>This sets the default fade speed and target bar height to predefined constants.
   */
  public CinematicBlackBarsRenderer() {
    this.fadeSpeed = DEFAULT_FADE_SPEED;
    this.targetBarHeight = DEFAULT_TARGET_BAR_HEIGHT;
  }

  /**
   * Constructor with configurable fade speed and target bar height.
   *
   * @param fadeSpeed Speed at which fade animations will play (in pixels/second).
   * @param targetBarHeight The target height of the cinematic bars at full fade-in.
   */
  public CinematicBlackBarsRenderer(float fadeSpeed, float targetBarHeight) {
    this.fadeSpeed = fadeSpeed;
    this.targetBarHeight = targetBarHeight;
  }

  /**
   * Updates the animation state over time.
   *
   * <p>This method advances the fade-in and fade-out animations by incrementally updating the
   * `currentSize` value based on elapsed time and speed. Stops animations when they reach their
   * final states.
   *
   * @param tpf Time per frame (time elapsed since the last frame in seconds).
   */
  @Override
  public void onUpdate(float tpf) {
    if (!isFading()) {
      return;
    }
    if (fadingIn) {
      animateFadeIn(tpf);
    }
    if (fadingOut) {
      animateFaceOut(tpf);
    }
  }

  /**
   * Handles the fade-in animation by incrementally increasing the size of the black bars over time
   * until the target bar height is reached.
   *
   * @param tpf Time per frame (time elapsed since the last frame in seconds).
   */
  private void animateFadeIn(float tpf) {
    currentSize += fadeSpeed * tpf;
    if (currentSize >= targetBarHeight) {
      currentSize = targetBarHeight;
      fadingIn = false;
    }
  }

  /**
   * Handles the fade-out animation by decrementally decreasing the size of the black bars over time
   * until their visual size is zero.
   *
   * @param tpf Time per frame (time elapsed since the last frame in seconds).
   */
  private void animateFaceOut(float tpf) {
    currentSize -= fadeSpeed * tpf;
    if (currentSize <= 0) {
      currentSize = 0;
      fadingOut = false;
    }
  }

  /**
   * Determines if a fade animation (either in or out) is currently active.
   *
   * @return {@code true} if either fading in or fading out is active, {@code false} otherwise.
   */
  public boolean isFading() {
    return fadingIn || fadingOut;
  }

  /**
   * Starts the fade-in animation.
   *
   * <p>This stops any ongoing fade-out animation and sets the internal state to begin the fade-in
   * effect from the current position.
   */
  public void fadeIn() {
    if (fadingIn) return;
    fadingIn = true;
    fadingOut = false;
  }

  /**
   * Starts the fade-out animation.
   *
   * <p>This stops any ongoing fade-in animation and sets the internal state to begin the fade-out
   * effect from the current position.
   */
  public void fadeOut() {
    if (fadingOut) return;
    fadingOut = true;
    fadingIn = false;
  }

  /**
   * Renders the cinematic black bars at the top and bottom of the screen.
   *
   * <p>The rendering uses the current interpolated size value (`currentSize`) to draw smooth black
   * bars transitioning in and out. This rendering respects the screen's current dimensions and is
   * dynamically updated during animation.
   *
   * @param g The graphics context used to perform the rendering.
   */
  @Override
  public void render(Graphics g) {
    g.setColor(Color.BLACK);
    renderTopBar(g);
    renderBottomBar(g);
  }

  /**
   * Renders the top cinematic black bar using the current animation state.
   *
   * @param g The graphics context used for rendering.
   */
  private void renderTopBar(Graphics g) {
    g.fillRect(0, 0, g.getWidth(), (int) currentSize);
  }

  /**
   * Renders the bottom cinematic black bar using the current animation state.
   *
   * @param g The graphics context used for rendering.
   */
  private void renderBottomBar(Graphics g) {
    g.fillRect(0, g.getHeight() - (int) currentSize, g.getWidth(), (int) currentSize);
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
