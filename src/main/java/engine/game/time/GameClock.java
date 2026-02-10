package engine.game.time;

/**
 * Represents a controllable game-time clock.
 *
 * <p>{@code GameClock} converts real frame time (tpf) into game time, supporting pausing and time
 * scaling.
 *
 * <p>This class is engine-agnostic and deterministic:
 *
 * <ul>
 *   <li>No system time access
 *   <li>No rendering or update hooks
 * </ul>
 *
 * <p>The engine (or an adapter) is responsible for calling {@link #update(float)}.
 */
public final class GameClock {

  private float time = 0f;
  private float scale = 1f;
  private boolean paused = false;

  /**
   * Advances the game clock.
   *
   * @param tpf real time per frame (usually from engine)
   */
  public void update(float tpf) {
    if (paused) return;
    time += tpf * scale;
  }

  /** @return total elapsed game time in seconds */
  public float getTime() {
    return time;
  }

  /** Sets the time scale (e.g. 0.5 = slow motion, 2.0 = fast forward). */
  public void setScale(float scale) {
    this.scale = Math.max(0f, scale);
  }

  /** @return current time scale */
  public float getScale() {
    return scale;
  }

  /** Pauses game time progression. */
  public void pause() {
    paused = true;
  }

  /** Resumes game time progression. */
  public void resume() {
    paused = false;
  }

  /** @return whether the clock is currently paused */
  public boolean isPaused() {
    return paused;
  }

  /** Resets the clock to zero. */
  public void reset() {
    time = 0f;
  }
}
