package engine.scene.loading;

import engine.scene.Scene;
import math.Mathf;

/**
 * A {@code SceneLoadJob} represents a single executable loading task responsible for building a
 * {@link Scene}.
 *
 * <p>The job delegates scene construction to a {@link SceneFactory} and tracks loading progress as
 * reported by the factory.
 *
 * <p>This class is designed to be executed on a background thread. Progress updates are stored in a
 * {@code volatile} field to allow safe observation from other threads.
 */
public class SceneLoadJob implements LoadTask {

  private final SceneFactory factory;
  private volatile float progress;
  private Scene result;

  /**
   * Creates a new load job for the given scene factory.
   *
   * @param factory the factory used to build the scene
   * @throws IllegalArgumentException if {@code factory} is null
   */
  public SceneLoadJob(SceneFactory factory) {
    this.factory = factory;
  }

  /**
   * Executes the scene loading process.
   *
   * <p>The factory is invoked and provided with a progress callback. Progress values reported by
   * the factory are clamped to the range {@code [0, 1]}.
   *
   * <p>When loading completes, progress is set to {@code 1}.
   */
  @Override
  public void run() {
    result = factory.build(p -> progress = Mathf.clamp01(p));
    progress = 1f;
  }

  /**
   * Returns the current loading progress.
   *
   * <p>The returned value is guaranteed to be within the range {@code [0, 1]}.
   *
   * @return the current progress of the load job
   */
  @Override
  public float getProgress() {
    return progress;
  }

  /**
   * Returns the loaded scene.
   *
   * <p>This method should only be called after the load job has completed.
   *
   * @return the resulting {@link Scene}, or {@code null} if loading has not finished
   */
  public Scene getResult() {
    return result;
  }
}
